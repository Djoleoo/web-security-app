import { Component } from '@angular/core';
import { ClientRegistrationRequestCreation } from '../model/ClientRegistrationRequestCreation.model';
import { NotifierService } from 'angular-notifier';
import { RegistrationService } from '../registration.service';
import { Router } from '@angular/router';
import { environment } from 'src/env/environment';
import { Subscription } from 'rxjs';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { LoginTokens } from '../model/LoginTokens.model';
import { SingleSignOn } from '../model/SingleSignOn';
import { LoginService } from '../login.service';

@Component({
  selector: 'xp-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  request: ClientRegistrationRequestCreation = new ClientRegistrationRequestCreation();
  isSuccessfull: boolean = false;
  isPasswordHidden: boolean = true;
  isRepeatPasswordHidden: boolean = true;
  reCaptchaSiteKey: string = environment.reCaptchaSiteKey;
  googleUser: any;
  isLoggedIn: boolean = false;
  private authSubscription: Subscription;

  constructor(
    private notifier: NotifierService,
    private registrationService: RegistrationService,
    private router: Router,
    private tokenStorage: TokenStorage,
    private socialAuthService: SocialAuthService,
    private loginService: LoginService,
  ) {
    this.isLoggedIn = !!this.tokenStorage.getAccessToken();

    if (!this.isLoggedIn) {
      this.authSubscription = this.socialAuthService.authState.subscribe((user) => {
        if (user) {
          this.googleUser = user;
          console.log(this.googleUser);
          this.tryGoogleLogin();
        }
      });
    }
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  tryGoogleLogin() {
    if (!this.googleUser) return;
    let token = new SingleSignOn()
    token.token = this.googleUser.idToken;

    this.loginService.loginGoogle(token).subscribe(
      (response: LoginTokens) => {
        this.tokenStorage.saveAccessToken(response.accessToken);
        this.tokenStorage.saveRefreshToken(response.refreshToken);
        this.notifier.notify('success', "Successfully signed in with Google.");
        this.authSubscription.unsubscribe();
        this.navigate('/');
      },
      (error: any) => {
        if(error.error.statusCode == 401) {
        }
        else {
          this.notifier.notify('error', error.error.error);
        }
      }
    );
  }

  changeEntityType(isIndividual: boolean) {
    this.request.isIndividual = isIndividual;
  }

  changePackage(clientPackage: string) {
    this.request.clientPackage = clientPackage;
  }

  tryRequestRegistration() {
    const now: string = new Date().toISOString();
    this.request.dateTime = now;
    if(this.validate()) {
      if(this.request.isIndividual) {
        this.request.companyName = '';
        this.request.taxIdentificationNumber = '';
      }
      else {
        this.request.firstName = '';
        this.request.lastName = '';
      }

      this.registrationService.requestClientRegistration(this.request).subscribe(
        (response: any) => {
          this.notifier.notify('success', "Successfully requested sign up. Please wait for administrator's response.");
          this.isSuccessfull = true;
        },
        (error: any) => {
          this.notifier.notify('error', error.error.error);
        }
      );
    }
  }

  validate(): boolean {
    if (!this.request.email || !this.request.email.match(/^\S+@\S+\.\S+$/)) {
      this.notifier.notify('error', 'Invalid email format.');
      return false;
    }

    if (!this.request.password || !this.request.password.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid password format. Passwords must have at least 8 charcters with 1 upper case letter, 1 lower case letter, 1 number and 1 special character.');
      return false;
    }

    if (this.request.repeatPassword !=  this.request.password) {
      this.notifier.notify('error', 'Password and password confirmation must match.');
      return false;
    }

    if (!this.request.password || !this.request.password.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid email format. Passwords must have at least 8 charcters with 1 upper case letter, 1 lower case letter, 1 number and 1 special character.');
      return false;
    }

    if (this.request.isIndividual) {
      if (!this.request.firstName || !this.request.firstName.trim()) {
        this.notifier.notify('error', 'First name cannot be empty.');
        return false;
      }

      if (!this.request.lastName || !this.request.lastName.trim()) {
        this.notifier.notify('error', 'Last name cannot be empty.');
        return false;
      }
    } else {
      if (!this.request.companyName || !this.request.companyName.trim()) {
        this.notifier.notify('error', 'Company name cannot be empty.');
        return false;
      }
      if (!this.request.taxIdentificationNumber || !this.request.taxIdentificationNumber.trim()) {
        this.notifier.notify('error', 'Tax identification number cannot be empty.');
        return false;
      }
      if (!this.request.taxIdentificationNumber.match(/^1\d{8}$/)) {
        this.notifier.notify('error', 'Invalid Tax identification number format. Correct format: 1XXXXXXXX.');
        return false;
      }
    }

    if (!this.request.phoneNumber || !this.request.phoneNumber.match(/^\S*(?:\+?(\d{1,3}))?[-. (]*(\d{2,3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$/)) {
      this.notifier.notify('error', 'Invalid phone number format.');
      return false;
    }

    if (!this.request.country || !this.request.country.trim()) {
      this.notifier.notify('error', 'Country cannot be empty.');
      return false;
    }
    
    if (!this.request.city || !this.request.city.trim()) {
      this.notifier.notify('error', 'City cannot be empty.');
      return false;
    }

    if (!this.request.address || !this.request.address.trim()) {
      this.notifier.notify('error', 'Address cannot be empty.');
      return false;
    }

    if (!this.request.reCaptchaResponse) {
      this.notifier.notify('error', 'You must complete the reCAPTCHA.');
      return false;
    }

    return true;
  }

  toggleHidePassword() {
    this.isPasswordHidden = !this.isPasswordHidden;
  }

  toggleHideRepeatPassword() {
    this.isRepeatPasswordHidden = !this.isRepeatPasswordHidden;
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }

  resolveReCaptcha(captchaResponse: string) {
    this.request.reCaptchaResponse = captchaResponse;
    console.log(`Resolved captcha with response: ${this.request.reCaptchaResponse}`);
  }
}

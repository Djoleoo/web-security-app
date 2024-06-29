import { Component, OnDestroy, OnInit } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { Login } from '../model/Login.model';
import { LoginService } from '../login.service';
import { LoginTokens } from '../model/LoginTokens.model';
import { Router } from '@angular/router';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { FirstLoginDto } from '../model/FirstLoginDto.model';
import { Observable, Subscription, map, take } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { TwoFactorAuthenticationComponent } from '../two-factor-authentication/two-factor-authentication.component';
import { environment } from 'src/env/environment';
import { GoogleLoginProvider, SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
import { SingleSignOn } from '../model/SingleSignOn';

@Component({
  selector: 'xp-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy{
  credentials: Login = new Login();
  isPasswordHidden: boolean = true;
  firstLoginDto: FirstLoginDto | undefined;
  isFirstLogin:boolean;
  reCaptchaSiteKey: string = environment.reCaptchaSiteKey;
  googleUser: any;
  isLoggedIn: boolean = false;
  private authSubscription: Subscription;

  constructor(
    private notifier: NotifierService, 
    private loginService: LoginService,
    private router: Router,
    private tokenStorage: TokenStorage,
    private dialog: MatDialog,
    private socialAuthService: SocialAuthService,
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
        this.navigate('/home');
      },
      (error: any) => {
        if(error.error.statusCode == 401) {
          this.openTwoFactorAuthenticationDialog();
        }
        else {
          this.notifier.notify('error', error.error.error);
        }
      }
    );
  }
  
  tryLogin() {
    if(this.validate()) {
      this.loginService.login(this.credentials).subscribe(
        (response: LoginTokens) => {
          this.tokenStorage.saveAccessToken(response.accessToken);
          this.tokenStorage.saveRefreshToken(response.refreshToken);
          this.notifier.notify('success', "Successfully signed in.");
          this.checkIsFirstLogin().subscribe((isFirstLogin: boolean) => {
            if (!isFirstLogin) {
              this.navigate('/home');
            } else {
              this.navigate('/change-password');
            }
          });
        },
        (error: any) => {
          if(error.error.statusCode == 401) {
            this.openTwoFactorAuthenticationDialog();
          }
          else {
            this.notifier.notify('error', error.error.error);
          }
        }
      );
    }
  }

  validate(): boolean {
    if (!this.credentials.username || !this.credentials.username.match(/^\S+@\S+\.\S+$/)) {
      this.notifier.notify('error', 'Invalid email format.');
      return false;
    }

    if (!this.credentials.password || !this.credentials.password.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid password format. Passwords must have at least 8 charcters with 1 upper case letter, 1 lower case letter, 1 number and 1 special character.');
      return false;
    }

    if (!this.credentials.reCaptchaResponse) {
      this.notifier.notify('error', 'You must complete the reCAPTCHA.');
      return false;
    }

    return true;
  }

  toggleHidePassword() {
    this.isPasswordHidden = !this.isPasswordHidden;
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }

  checkIsFirstLogin(): Observable<boolean> {

    return this.loginService.getUserFirstLoginByUsername(this.credentials.username).pipe(
      map((response: FirstLoginDto) => {
        this.firstLoginDto = response;
        this.isFirstLogin=this.firstLoginDto.isFirstLogin;
        return this.firstLoginDto.isFirstLogin;
      })
    );
  }

  openTwoFactorAuthenticationDialog(): void {
    const dialogRef = this.dialog.open(TwoFactorAuthenticationComponent, {
      data: this.credentials
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

  resolveReCaptcha(captchaResponse: string) {
    this.credentials.reCaptchaResponse = captchaResponse;
    console.log(`Resolved captcha with response: ${this.credentials.reCaptchaResponse}`);
  }
}

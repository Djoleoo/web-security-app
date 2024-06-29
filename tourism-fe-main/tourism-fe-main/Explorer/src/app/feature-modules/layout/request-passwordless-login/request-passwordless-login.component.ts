import { Component } from '@angular/core';
import { PasswordlessLoginRequest } from '../model/PasswordlessLoginRequest.model';
import { NotifierService } from 'angular-notifier';
import { LoginService } from '../login.service';
import { environment } from 'src/env/environment';

@Component({
  selector: 'xp-request-passwordless-login',
  templateUrl: './request-passwordless-login.component.html',
  styleUrls: ['./request-passwordless-login.component.css']
})
export class RequestPasswordlessLoginComponent {
  credentials: PasswordlessLoginRequest = new PasswordlessLoginRequest();
  isSuccessfull: boolean = false;
  reCaptchaSiteKey: string = environment.reCaptchaSiteKey;

  constructor(
    private notifier: NotifierService, 
    private loginService: LoginService
  ) {}

  
  tryLogin() {
    if(this.validate()) {
      this.loginService.requestPasswordlessLogin(this.credentials).subscribe(
        (response: any) => {
          this.notifier.notify('success', "Successfully requested passwordless sign in. To continue please visit the link we sent to your email.");
          this.isSuccessfull = true;
        },
        (error: any) => {
          this.notifier.notify('error', error.error.error);
        }
      );
    }
  }

  validate(): boolean {
    if (!this.credentials.username || !this.credentials.username.match(/^\S+@\S+\.\S+$/)) {
      this.notifier.notify('error', 'Invalid email format.');
      return false;
    }

    if (!this.credentials.reCaptchaResponse) {
      this.notifier.notify('error', 'You must complete the reCAPTCHA.');
      return false;
    }

    return true;
  }

  resolveReCaptcha(captchaResponse: string) {
    this.credentials.reCaptchaResponse = captchaResponse;
    console.log(`Resolved captcha with response: ${this.credentials.reCaptchaResponse}`);
  }
}

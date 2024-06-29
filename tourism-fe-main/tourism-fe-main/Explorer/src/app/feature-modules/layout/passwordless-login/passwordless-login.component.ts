import { Component } from '@angular/core';
import { PasswordlessLogin } from '../model/PasswordlessLogin.model';
import { LoginService } from '../login.service';
import { ActivatedRoute } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { LoginTokens } from '../model/LoginTokens.model';
import { MatDialog } from '@angular/material/dialog';
import { TwoFactorAuthenticationPasswordlessComponent } from '../two-factor-authentication-passwordless/two-factor-authentication-passwordless.component';
import { environment } from 'src/env/environment';

@Component({
  selector: 'xp-passwordless-login',
  templateUrl: './passwordless-login.component.html',
  styleUrls: ['./passwordless-login.component.css']
})
export class PasswordlessLoginComponent {
  credentials: PasswordlessLogin = new PasswordlessLogin();
  display: String = 'CAPTCHA';
  displayMessage: String = 'Waiting for servers response...';
  reCaptchaSiteKey: string = environment.reCaptchaSiteKey;

  constructor(
    private loginService: LoginService, 
    private route: ActivatedRoute,
    private notifier: NotifierService,
    private tokenStorage: TokenStorage,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.credentials.token = this.route.snapshot.queryParams['token'];
    this.credentials.signature = this.route.snapshot.queryParams['signature'];
  }

  login() {
    this.loginService.loginPasswordless(this.credentials).subscribe(
      (response: LoginTokens) => {
        this.tokenStorage.saveAccessToken(response.accessToken);
        this.tokenStorage.saveRefreshToken(response.refreshToken);
        this.notifier.notify('success', "Successfully signed in without password.");
        this.display = 'SUCCESS';
        this.displayMessage = "Successfully signed in without password.";
      },
      (error: any) => {
        console.log(error)
        if(error.error.statusCode == 403) {
          this.display = "FORBIDDEN";
          this.displayMessage = error.error.error;
          this.notifier.notify('error', error.error.error);
        }
        else if(error.error.statusCode == 401) {
          this.display = "FORBIDDEN";
          this.displayMessage = "Two factor authentication required.";
          this.openTwoFactorAuthenticationDialog();
        }
        else {
          this.notifier.notify('error', error.error.error);
        }
      }
    );
  }

  openTwoFactorAuthenticationDialog(): void {
    const dialogRef = this.dialog.open(TwoFactorAuthenticationPasswordlessComponent, {
      data: this.credentials
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.notifier.notify('success', "Successfully signed in without password.");
        this.display = 'SUCCESS';
        this.displayMessage = "Successfully signed in without password.";
      }
    });
  }

  resolveReCaptcha(captchaResponse: string) {
    this.credentials.reCaptchaResponse = captchaResponse;
    console.log(`Resolved captcha with response: ${this.credentials.reCaptchaResponse}`);
    if(this.isReCaptchaSolved()) {
      this.login();
    }
  }

  isReCaptchaSolved(): boolean {
    const isSolved = this.credentials.reCaptchaResponse && this.credentials.reCaptchaResponse != '' ? true : false;
    return isSolved;
  }
}

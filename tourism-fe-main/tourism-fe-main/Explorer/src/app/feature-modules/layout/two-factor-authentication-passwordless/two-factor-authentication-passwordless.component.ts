import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NotifierService } from 'angular-notifier';
import { LoginService } from '../login.service';
import { Login } from '../model/Login.model';
import { LoginTokens } from '../model/LoginTokens.model';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { Observable, map } from 'rxjs';
import { FirstLoginDto } from '../model/FirstLoginDto.model';
import { Router } from '@angular/router';
import { PasswordlessLogin } from '../model/PasswordlessLogin.model';

@Component({
  selector: 'xp-two-factor-authentication-passwordless',
  templateUrl: './two-factor-authentication-passwordless.component.html',
  styleUrls: ['./two-factor-authentication-passwordless.component.css']
})
export class TwoFactorAuthenticationPasswordlessComponent {
  code: number;
  firstLoginDto: FirstLoginDto | undefined;
  isFirstLogin: boolean;
  isSuccessfull: boolean = false;

  constructor(
    private loginService: LoginService,
    private notifier: NotifierService,
    public dialogRef: MatDialogRef<TwoFactorAuthenticationPasswordlessComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: PasswordlessLogin,
    private tokenStorage: TokenStorage,
    private router: Router,
  ) {}

  login() {
    this.loginService.loginPasswordlessTwoFactorAuthentication(this.data).subscribe(
      (response: LoginTokens) => {
        this.tokenStorage.saveAccessToken(response.accessToken);
        this.tokenStorage.saveRefreshToken(response.refreshToken);
        this.isSuccessfull = true;
        this.close();
      },
      (error: any) => {
        this.notifier.notify('error', error.error.error);
      }
    );
  }

  close() {
    this.dialogRef.close(this.isSuccessfull);
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }
}

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

@Component({
  selector: 'xp-two-factor-authentication',
  templateUrl: './two-factor-authentication.component.html',
  styleUrls: ['./two-factor-authentication.component.css']
})
export class TwoFactorAuthenticationComponent {
  code: number;
  firstLoginDto: FirstLoginDto | undefined;
  isFirstLogin:boolean;

  constructor(
    private loginService: LoginService,
    private notifier: NotifierService,
    public dialogRef: MatDialogRef<TwoFactorAuthenticationComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: Login,
    private tokenStorage: TokenStorage,
    private router: Router,
  ) {}

  login() {
    this.loginService.loginTwoFactorAuthentication(this.data).subscribe(
      (response: LoginTokens) => {
        this.tokenStorage.saveAccessToken(response.accessToken);
        this.tokenStorage.saveRefreshToken(response.refreshToken);
        this.notifier.notify('success', "Successfully signed in.");
        this.checkIsFirstLogin().subscribe((isFirstLogin: boolean) => {
          if (!isFirstLogin) {
            this.navigate('/');
          } else {
            this.navigate('/change-password');
          }
          this.close();
        });
      },
      (error: any) => {
        this.notifier.notify('error', error.error.error);
      }
    );
  }

  close() {
    this.dialogRef.close();
  }

  checkIsFirstLogin(): Observable<boolean> {
    return this.loginService.getUserFirstLoginByUsername(this.data.username).pipe(
      map((response: FirstLoginDto) => {
        this.firstLoginDto = response;
        this.isFirstLogin=this.firstLoginDto.isFirstLogin;
        return this.firstLoginDto.isFirstLogin;
      })
    );
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }
}


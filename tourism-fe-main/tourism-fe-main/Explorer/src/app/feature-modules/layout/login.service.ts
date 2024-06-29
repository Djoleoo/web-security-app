import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/env/environment';
import { Login } from './model/Login.model';
import { Observable } from 'rxjs';
import { LoginTokens } from './model/LoginTokens.model';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { PasswordlessLoginRequest } from './model/PasswordlessLoginRequest.model';
import { PasswordlessLogin } from './model/PasswordlessLogin.model';
import { FirstLoginDto } from './model/FirstLoginDto.model';
import { TwoFactorAuthenticationStatus } from './model/TwoFactorAuthenticationStatus.model';
import { TwoFactorAuthenticationSetupCompletion } from './model/TwoFactorAuthenticationSetupCompletion.model';
import { SingleSignOn } from './model/SingleSignOn';

@Injectable({
  providedIn: 'root'
})
export class LoginService {


  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenStorage: TokenStorage
  ) { }

  login(credentials: Login): Observable<LoginTokens> {
    var url = environment.apiHost + "login";
    return this.http.post<LoginTokens>(url, credentials);
  }

  loginGoogle(token: SingleSignOn): Observable<LoginTokens> {
    var url = environment.apiHost + "login/google";
    return this.http.post<LoginTokens>(url, token);
  }

  loginTwoFactorAuthentication(credentials: Login): Observable<LoginTokens> {
    var url = environment.apiHost + "login/two-factor-authentication";
    return this.http.post<LoginTokens>(url, credentials);
  }

  requestPasswordlessLogin(credentials: PasswordlessLoginRequest): Observable<any> {
    var url = environment.apiHost + "login/request-passwordless";
    return this.http.post<any>(url, credentials);
  }

  loginPasswordless(credentials: PasswordlessLogin): Observable<LoginTokens> {
    var url = environment.apiHost + "login/passwordless";
    return this.http.post<LoginTokens>(url, credentials);
  }

  loginPasswordlessTwoFactorAuthentication(credentials: PasswordlessLogin): Observable<LoginTokens> {
    var url = environment.apiHost + "login/passwordless/two-factor-authentication";
    return this.http.post<LoginTokens>(url, credentials);
  }

  logout() {
    this.tokenStorage.clear();
  }

  getUserFirstLoginByUsername(username: string): Observable<FirstLoginDto> {
    var url = environment.apiHost+"login/users/"+username;
    return this.http.get<FirstLoginDto>(url);
  }

  changePassword(username: string, newPassword: string): Observable<any> {
    let params = new HttpParams().set('username', username).set('newPassword', newPassword);
    var url = environment.apiHost + 'login/changePassword'
    return this.http.post(url, null, { params: params });
  }

  check2FactorAuthentication(): Observable<TwoFactorAuthenticationStatus> {
    var url = environment.apiHost + 'login/check-2-factor-authentication'
    return this.http.get<TwoFactorAuthenticationStatus>(url);
  }

  setup2FactorAuthentication(): Observable<any> {
    var url = environment.apiHost + 'login/setup-2-factor-authentication'
    return this.http.post<any>(url, {});
  }

  enable2FactorAuthentication(code: TwoFactorAuthenticationSetupCompletion): Observable<any> {
    var url = environment.apiHost + 'login/enable-2-factor-authentication'
    return this.http.put<any>(url, code);
  }

  disable2FactorAuthentication(): Observable<any> {
    var url = environment.apiHost + 'login/disable-2-factor-authentication'
    return this.http.put<any>(url, {});
  }
}

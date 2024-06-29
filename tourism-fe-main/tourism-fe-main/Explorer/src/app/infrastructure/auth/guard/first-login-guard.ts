import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { LoginService } from 'src/app/feature-modules/layout/login.service';
import { FirstLoginDto } from 'src/app/feature-modules/layout/model/FirstLoginDto.model';

@Injectable({
  providedIn: 'root'
})
export class FirstLoginGuard implements CanActivate {

  constructor(
    private loginService: LoginService,
    private tokenStorage: TokenStorage,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> | Promise<boolean> | boolean {
    // Check if the user is logged in for the first time
    return this.loginService.getUserFirstLoginByUsername(this.tokenStorage.getUsername()).toPromise().then((FirstLoginDto) => {
      if (FirstLoginDto?.isFirstLogin) {
        // If it's the first login, redirect to change password
        this.router.navigate(['/change-password']);
        return false; // Prevent further navigation
      } else {
        return true; // Allow navigation
      }
    }).catch(() => {
      // Handle error, for example, token expiration
      this.loginService.logout;
      this.router.navigate(['/login']);
      return false; // Prevent further navigation
    });
  }
}
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorage } from '../jwt/token.service';
import { NotifierService } from 'angular-notifier';

@Injectable({
  providedIn: 'root'
})
export class ClientAuthGuard implements CanActivate {

  constructor(
    private notifier: NotifierService,
    private router: Router,
    private tokenStorage: TokenStorage
  ) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
        
    const token = this.tokenStorage.getAccessToken();
    if (!token) {
      this.router.navigate(['/unauthenticated-access']);
      this.notifier.notify('error', 'You must sign in first to access this page.')
      return false;
    }

    const role = this.tokenStorage.getRole();
    if(role != 'CLIENT') {
      this.router.navigate(['/unauthorized-access']);
      this.notifier.notify('error', 'You are not allowed to access this page.')
      return false;
    }

    return true;
  }
}
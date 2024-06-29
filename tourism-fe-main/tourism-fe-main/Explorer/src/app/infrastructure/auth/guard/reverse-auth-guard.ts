import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorage } from '../jwt/token.service';
import { NotifierService } from 'angular-notifier';

@Injectable({
  providedIn: 'root'
})
export class ReverseAuthGuard implements CanActivate {

  constructor(
    private notifier: NotifierService,
    private router: Router,
    private tokenStorage: TokenStorage,
  ) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
        
    const token = this.tokenStorage.getAccessToken();
    if (token) {
      this.tokenStorage.clear();
    }

    return true;
  }
}
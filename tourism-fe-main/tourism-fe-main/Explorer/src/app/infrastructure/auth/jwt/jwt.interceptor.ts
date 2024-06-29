import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ACCESS_TOKEN } from '../../../shared/constants';
import { TokenStorage } from "./token.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private tokenStorage:TokenStorage) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem(ACCESS_TOKEN);
    
    // Check if the request URL matches a specific route that should not require an access token
    if (!accessToken || this.shouldUseRefreshToken(request.url)) {
      return next.handle(request);
    }

    const accessTokenRequest = request.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    return next.handle(accessTokenRequest);
  }

  private shouldUseRefreshToken(url: string): boolean {
    // Add logic here to determine if a specific route should use the refresh token instead of the access token
    // For example, you can check if the URL matches certain patterns or paths
    return url.includes('/refresh-token');
  }
}
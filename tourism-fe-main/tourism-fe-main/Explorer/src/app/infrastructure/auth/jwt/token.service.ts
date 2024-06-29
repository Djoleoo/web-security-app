import { Injectable } from '@angular/core';
import { ACCESS_TOKEN, REFRESH_TOKEN, USER } from '../../../shared/constants';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
  })
  export class TokenStorage {
    constructor(private jwtHelper: JwtHelperService,
                private http:HttpClient
    ) {}
  
    saveAccessToken(token: string): void {
      localStorage.removeItem(ACCESS_TOKEN);
      localStorage.setItem(ACCESS_TOKEN, token);
    }
  
    getAccessToken() {
      return localStorage.getItem(ACCESS_TOKEN);
    }

    saveRefreshToken(token: string): void {
      localStorage.removeItem(REFRESH_TOKEN);
      localStorage.setItem(REFRESH_TOKEN, token);
    }
  
    getRefreshToken() {
      return localStorage.getItem(REFRESH_TOKEN);
    }
  
    clear() {
      localStorage.removeItem(ACCESS_TOKEN);
      localStorage.removeItem(REFRESH_TOKEN);
      localStorage.removeItem(USER);
    }

    getUsername(): string {
      const decodedToken = this.getDecodedAccessToken();

      const username = decodedToken.username;
      return username;
    }

    getRole(): string {
      const decodedToken = this.getDecodedAccessToken();

      const username = decodedToken.role;
      console.log(username);
      return username;
    }

    getPackage(): string {
      const decodedToken = this.getDecodedAccessToken();

      const clientPackage = decodedToken.package;
      console.log(clientPackage);
      return clientPackage;
    }

    getType(): string {
      const decodedToken = this.getDecodedAccessToken();

      const type = decodedToken.type;
      console.log(type);
      return type;
    }

    getDecodedAccessToken(): any {
      const token = this.getAccessToken();
      if(token) {
        const decodedToken = this.jwtHelper.decodeToken(token);
        return decodedToken;
      }
      return "";
    }

    refreshAccessToken(refreshToken: string|null) {
      const refreshTokenUrl = 'https://localhost:443/api/login/refresh-token'; 
      refreshToken=this.getRefreshToken();
      // Make POST request to refresh access token
      return this.http
        .post<any>(refreshTokenUrl, { refreshToken: refreshToken })
        .toPromise()
        .then((response) => {
          // Extract new access token from response
          const accessToken = response.accessToken;
  
          // Save new access token
          this.saveAccessToken(accessToken);
  
          // Return the new access token
          return accessToken;
        })
        .catch((error) => {
          // Handle error
          console.error('Error refreshing access token:', error);
          throw error;
        });
    }
  }
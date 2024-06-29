import { Component } from '@angular/core';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';

@Component({
  selector: 'xp-refresh-token',
  templateUrl: './refresh-token.component.html',
  styleUrls: ['./refresh-token.component.css']
})
export class RefreshTokenComponent  {
  constructor(private tokenStorage: TokenStorage) {}

  ngOnInit(){
    console.log('Refresh token: ', this.tokenStorage.getRefreshToken())
  }
  refreshAccessToken() {
    const refreshToken = this.tokenStorage.getRefreshToken();
    if (refreshToken) {
      this.tokenStorage
        .refreshAccessToken(refreshToken)
        .then((newAccessToken) => {
          console.log('New access token:', newAccessToken);
        })
        .catch((error) => {
          console.error('Error refreshing access token:', this.tokenStorage.getAccessToken());
        });
    } else {
      console.error('Refresh token not found.');
    }
  }
}

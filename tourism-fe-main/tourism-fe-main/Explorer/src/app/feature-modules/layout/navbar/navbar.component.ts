import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { User } from 'src/app/infrastructure/auth/model/user.model';
import { ToggleTwoFactorAuthenticationComponent } from '../toggle-two-factor-authentication/toggle-two-factor-authentication.component';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  user: User | undefined;

  constructor(
    private tokenStorage: TokenStorage,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
  }

  isUserAuthenticated(): boolean {
    const token = this.tokenStorage.getAccessToken();
    
    if(token) {
      return true;
    }
    return false;
  }

  getCurrentURL(): string {
    const currentUrl = this.router.url;
    console.log('Current URL:', currentUrl);
    return currentUrl;
  }

  getRole(): string {
    const role = this.tokenStorage.getRole();

    return role;
  }

  openToggleTwoFactorAuthenticationDialog(): void {
    const dialogRef = this.dialog.open(ToggleTwoFactorAuthenticationComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}

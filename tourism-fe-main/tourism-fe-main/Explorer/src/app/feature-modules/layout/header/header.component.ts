import { Component } from '@angular/core';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { LoginService } from '../login.service';
import { MatDialog } from '@angular/material/dialog';
import { LogoutComponent } from '../logout/logout.component';
import { Router } from '@angular/router';

@Component({
  selector: 'xp-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  constructor(
    private tokenStorage: TokenStorage,
    private loginService: LoginService,
    private dialog: MatDialog
  ) {}

  getUsername(): string {
    const username = this.tokenStorage.getUsername();
    return username ? username : '';
  }

  logout() {
    const dialogRef = this.dialog.open(LogoutComponent, {});
  }
}

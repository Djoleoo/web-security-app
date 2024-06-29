import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  constructor(
    private notifier: NotifierService,
    public dialogRef: MatDialogRef<LogoutComponent>,
    private loginService: LoginService,
    private router: Router,
    private socialAuthService: SocialAuthService,
  ) {}

  async accept() {
    await this.navigate('/');
    this.notifier.notify('success', 'Successfully logged out.');
    window.location.reload();

    this.close();
    
    this.loginService.logout();
  }

  close() {
    this.dialogRef.close();
  }

  async navigate(route: string) {
    await this.router.navigate([route]);
  }
}

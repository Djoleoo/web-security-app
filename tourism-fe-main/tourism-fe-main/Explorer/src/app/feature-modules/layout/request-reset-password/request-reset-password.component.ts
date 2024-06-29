import { Component } from '@angular/core';
import { AdminService } from '../admin.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-request-reset-password',
  templateUrl: './request-reset-password.component.html',
  styleUrls: ['./request-reset-password.component.css']
})
export class RequestResetPasswordComponent {
  email: string = '';

  constructor(private adminService: AdminService, private notifier: NotifierService) {}

  onSubmit(): void {
    this.adminService.recoverPassword(this.email).subscribe(
      () => {
        console.log('Password recovery email sent');
        this.notifier.notify('succes', 'Check your email for password recovery steps.')
      },
      error => {
        console.error('Error recovering password:', error);
      }
    );
  }
}

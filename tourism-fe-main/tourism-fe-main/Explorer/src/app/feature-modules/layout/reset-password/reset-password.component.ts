import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../admin.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  newPassword: string = '';
  confirmPassword: string = '';
  token: string = '';
  passwordMismatch: boolean = false;

  constructor(private route: ActivatedRoute, private adminService: AdminService, private notifier: NotifierService,) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

  onSubmit(): void {
    this.checkPasswordMatch();
    if (this.validate()) {
      this.adminService.resetPassword(this.token, this.newPassword).subscribe(
        () => {
          console.log('Password reset successfully');
          this.notifier.notify('succes', 'Password changed.')
          // Handle success, e.g., show a success message
        },
        error => {
          console.error('Error resetting password:', error);
          // Handle error, e.g., show an error message
        }
      );
    }
  }

  validate(): boolean {
    if (!this.newPassword || !this.newPassword.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid password format. Passwords must have at least 8 characters with 1 upper case letter, 1 lower case letter, 1 number, and 1 special character.');
      return false;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.notifier.notify('error', 'Password and password confirmation must match.');
      return false;
    }

    return true;
  }

  checkPasswordMatch(): void {
    this.passwordMismatch = this.newPassword !== this.confirmPassword;
  }
}

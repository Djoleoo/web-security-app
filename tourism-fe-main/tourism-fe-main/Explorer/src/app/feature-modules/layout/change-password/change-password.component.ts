import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'xp-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  newPassword: string = '';
  repeatPassword: string = '';
  isPasswordHidden: boolean = true;
  isRepeatPasswordHidden: boolean = true;
  isSuccessfull: boolean = false;
  username:string ='';

  constructor(
    private notifier: NotifierService,
    private tokenStorage: TokenStorage,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    // Call getUsername() method from AuthService to get the username
    this.username = this.tokenStorage.getUsername();
  }

  tryChangePassword() {
    if (this.validate()) {
      // Call your service method here to change the password
      this.changePassword();
      this.notifier.notify('success', "Password changed successfully.");
      this.isSuccessfull = true;
      this.loginService.logout();
      this.router.navigate(['/sign-in']);
      
    }
  }

  validate(): boolean {
    if (!this.newPassword || !this.newPassword.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid password format. Passwords must have at least 8 characters with 1 upper case letter, 1 lower case letter, 1 number, and 1 special character.');
      return false;
    }

    if (this.newPassword !== this.repeatPassword) {
      this.notifier.notify('error', 'Password and password confirmation must match.');
      return false;
    }

    return true;
  }

  toggleHidePassword() {
    this.isPasswordHidden = !this.isPasswordHidden;
  }

  toggleHideRepeatPassword() {
    this.isRepeatPasswordHidden = !this.isRepeatPasswordHidden;
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }

  changePassword() {
    console.log("menja");
    this.loginService.changePassword(this.username, this.newPassword).subscribe(
      response => {
        console.log('Password changed successfully:', response);
        // Optionally, perform any additional actions after a successful password change
      },
      error => {
        console.error('Error changing password:', error);
        // Optionally, handle the error appropriately
      }
    );
  }
}

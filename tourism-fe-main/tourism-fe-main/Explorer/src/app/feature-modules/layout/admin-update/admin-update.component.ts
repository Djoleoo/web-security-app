import { Component } from '@angular/core';
import { AdminService } from '../admin.service';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { NotifierService } from 'angular-notifier';
import { Router } from '@angular/router';
import { PersonalInfo } from '../model/PersonalInfo.model';

@Component({
  selector: 'xp-admin-update',
  templateUrl: './admin-update.component.html',
  styleUrls: ['./admin-update.component.css']
})
export class AdminUpdateComponent {

  username: string = '';
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  country: string;
  phoneNumber: string;
  userInfo:PersonalInfo;

  constructor(private adminService: AdminService, 
              private tokenStorage: TokenStorage,
              private notifier: NotifierService,
              private route:Router) { }
  
  ngOnInit(): void {
    // Call getUsername() method from AuthService to get the username
    this.username = this.tokenStorage.getUsername();
    this.getUserInfo(this.username);
  }
  
  updateUser(firstName: string, lastName: string, address: string, city: string, country: string, phoneNumber: string): void {
    if(this.validate()){
    this.adminService.updateUser(this.username, firstName, lastName, address, city, country, phoneNumber)
      .subscribe(
        response => {
          //this.notifier.notify('success', "Successfully updated profile."); // Handle success response here
          // You can show a success message or perform any other actions on success
          this.route.navigate(['/']);
          
        },
        error => {
          this.handleError(error);// Handle error response here
          // You can show an error message or perform any other actions on error
        }
      );
    }
  }
  
  
  validate(): boolean {
    
    if (!this.firstName || !this.firstName.trim()) {
      this.notifier.notify('error', 'First name cannot be empty.');
      return false;
    }

    if (!this.lastName || !this.lastName.trim()) {
      this.notifier.notify('error', 'Last name cannot be empty.');
      return false;
    }
    
    

    if (!this.phoneNumber || !this.phoneNumber.match(/^(\+?\d{1,3})?[-. (]*(\d{2,3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$/)) {
      this.notifier.notify('error', 'Invalid phone number format.');
      return false;
    }

    if (!this.country || !this.country.trim()) {
      this.notifier.notify('error', 'Country cannot be empty.');
      return false;
    }
    
    if (!this.city || !this.city.trim()) {
      this.notifier.notify('error', 'City cannot be empty.');
      return false;
    }

    if (!this.address || !this.address.trim()) {
      this.notifier.notify('error', 'Address cannot be empty.');
      return false;
    }

    return true;
  }

  handleError(error: any): void {
    // Check if the error message matches the specific error
    if (error && error instanceof Object && error.name === 'HttpErrorResponse') {
      // Navigate to the desired page
      this.notifier.notify('success', 'Updated!');
      this.route.navigate(['/'])
    } else {
      // Handle other errors or display a notification
      this.notifier.notify('error', error.error.error);
    }
  }

  getUserInfo(username: string): void {
    this.adminService.getUserByUsername(username)
      .subscribe({
        next: (userInfo: PersonalInfo) => {
          this.userInfo = userInfo;
          //this.username=this.userInfo.username;
          this.firstName=this.userInfo.firstName;
          this.lastName=this.userInfo.lastName;
          this.address=this.userInfo.address;
          this.phoneNumber=this.userInfo.phoneNumber;
          this.city=this.userInfo.city;
          this.country=this.userInfo.country
        },
        error: (err) => {
          console.error('Error fetching user info:', err);
        }
      });
  }
}

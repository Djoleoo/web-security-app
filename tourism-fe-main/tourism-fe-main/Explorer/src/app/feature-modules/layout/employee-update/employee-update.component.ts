import { Component } from '@angular/core';
import { EmployeeService } from '../employee.service';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { NotifierService } from 'angular-notifier';
import { Router } from '@angular/router';
import { AdminService } from '../admin.service';
import { PersonalInfo } from '../model/PersonalInfo.model';

@Component({
  selector: 'xp-employee-update',
  templateUrl: './employee-update.component.html',
  styleUrls: ['./employee-update.component.css']
})
export class EmployeeUpdateComponent {
  username: string = '';
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  country: string;
  phoneNumber: string;
  userInfo:PersonalInfo;

  constructor(private adminService: EmployeeService, 
              private tokenStorage: TokenStorage,
              private route:Router,
              private notifier:NotifierService,
              private infoService:AdminService) { }
  
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
          this.route.navigate(['/'])// Handle success response here
          // You can show a success message or perform any other actions on success
        },
        error => {
          this.handleError(error); // Handle error response here
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
    this.infoService.getUserByUsername(username)
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



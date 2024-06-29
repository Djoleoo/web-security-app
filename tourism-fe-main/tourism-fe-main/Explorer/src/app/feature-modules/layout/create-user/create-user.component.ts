import { Component } from '@angular/core';
import { ClientRegistrationRequestCreation } from '../model/ClientRegistrationRequestCreation.model';
import { UserCreation } from '../model/UserCreation.model';
import { RegistrationService } from '../registration.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {
  user: UserCreation= new UserCreation('', '', '', '', '');

  constructor(private registrationService: RegistrationService,
    private notifier: NotifierService
  ){}

  onSubmit() {
    console.log('User created:', this.user);

    if(this.validate()){
      this.registrationService.adminCreatesUser(this.user).subscribe(
        response => {
          console.log('User created successfully:', response);
          // Optionally, perform any additional actions after successful user creation
        },
        error => {
          console.error('Error creating user:', error);
          // Optionally, handle the error appropriately
        }
      );
    }
    
  }

  validate(): boolean {
    if (!this.user.email || !this.user.email.match(/^\S+@\S+\.\S+$/)) {
      this.notifier.notify('error', 'Invalid email format.');
      return false;
    }

    if (!this.user.password || !this.user.password.match(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$/)) {
      this.notifier.notify('error', 'Invalid password format. Passwords must have at least 8 charcters with 1 upper case letter, 1 lower case letter, 1 number and 1 special character.');
      return false;
    }

    return true;
  }

}

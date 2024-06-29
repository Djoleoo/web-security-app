import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserActivation } from '../model/UserActivation.model';
import { RegistrationService } from '../registration.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-account-activation',
  templateUrl: './account-activation.component.html',
  styleUrls: ['./account-activation.component.css']
})
export class AccountActivationComponent implements OnInit{
  accountActivation: UserActivation = new UserActivation();
  display: String = 'LOADING';
  displayMessage: String = 'Waiting for servers response...';

  constructor(
    private registrationService: RegistrationService, 
    private route: ActivatedRoute,
    private notifier: NotifierService
  ) { }

  ngOnInit(): void {
    this.accountActivation.token = this.route.snapshot.queryParams['token'];
    this.accountActivation.signature = this.route.snapshot.queryParams['signature'];

    this.activateUser();
  }

  activateUser() {
    this.registrationService.activateUserAccount(this.accountActivation).subscribe(
      (response: any) => {
        this.notifier.notify('success', "Successfully activated your account.");
        this.display = 'SUCCESS';
        this.displayMessage = "Successfully activated your account.";
      },
      (error: any) => {
        console.log(error)
        if(error.error.statusCode == 403) {
          this.display = "FORBIDDEN";
          this.displayMessage = error.error.error;
        } else if(error.error.statusCode == 404) {
          this.display = "NOT FOUND";
          this.displayMessage = "User account was not found.";
        } else if(error.error.statusCode == 409) {
          this.display = "CONFLICT";
          this.displayMessage = "User account has already been activated.";
        }
        this.notifier.notify('error', error.error.error);
      }
    );
  }
}

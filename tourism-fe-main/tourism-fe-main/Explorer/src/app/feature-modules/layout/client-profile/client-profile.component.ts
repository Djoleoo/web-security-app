import { Component } from '@angular/core';
import { PersonalInfo } from '../model/PersonalInfo.model';
import { AdminService } from '../admin.service';
import { NotifierService } from 'angular-notifier';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { MatDialog } from '@angular/material/dialog';
import { AccountDeletionComponent } from '../account-deletion/account-deletion.component';

@Component({
  selector: 'xp-client-profile',
  templateUrl: './client-profile.component.html',
  styleUrls: ['./client-profile.component.css']
})
export class ClientProfileComponent {
  username: string = '';
  firstName: string = '';
  lastName: string = '';
  address: string = '';
  city: string = '';
  country: string = '';
  phoneNumber: string = '';
  clientPackage: string = '';
  type: string = '';
  companyName: string = '';
  taxIdentificationNumber: string = '';

  constructor(
    private tokenStorage: TokenStorage,
    private infoService:AdminService, 
    private notifier:NotifierService,
    private dialog: MatDialog,
  ) { }
  
  ngOnInit(): void {
    this.username = this.tokenStorage.getUsername();
    this.clientPackage = this.tokenStorage.getPackage();
    this.type = this.tokenStorage.getType();
    this.getUserInfo(this.username);
  }

  getUserInfo(username: string): void {
    this.infoService.getUserByUsername(username)
      .subscribe({
        next: (userInfo: PersonalInfo) => {
          this.firstName=userInfo.firstName;
          this.lastName=userInfo.lastName;
          this.address=userInfo.address;
          this.phoneNumber=userInfo.phoneNumber;
          this.city=userInfo.city;
          this.country=userInfo.country
          this.companyName=userInfo.companyName
          this.taxIdentificationNumber=userInfo.taxIdentificationNumber
        }
      });
  }

  openAccountDeletionDialog(): void {
    const dialogRef = this.dialog.open(AccountDeletionComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}

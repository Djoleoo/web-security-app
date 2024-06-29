import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NotifierService } from 'angular-notifier';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { RegistrationService } from '../registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'xp-account-deletion',
  templateUrl: './account-deletion.component.html',
  styleUrls: ['./account-deletion.component.css']
})
export class AccountDeletionComponent {
  confirmation: String = '';

  constructor(
    public dialogRef: MatDialogRef<AccountDeletionComponent>,
    private accountService: RegistrationService,
    private notifier: NotifierService,
    private tokenStorage: TokenStorage,
    private router: Router
  ) {}

  delete() {
    if(!this.validate()) {
      return;
    }

    this.accountService.deleteUserAccount().subscribe(
      (response: any) => {
        this.tokenStorage.clear();
        this.notifier.notify('success', "Successfully deleted user account.");
        this.navigate('/sign-in');
        this.close();
      },
      (error: any) => {
        this.notifier.notify('error', error.error.error);
      }
    );

  }

  validate(): boolean {
    if(this.confirmation == 'DELETE') {
      return true;
    }

    this.notifier.notify('error', "You have to confirm account deletion by tuping DELETE in the confirmation field.");
    return false;
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }

  close() {
    this.dialogRef.close();
  }
}

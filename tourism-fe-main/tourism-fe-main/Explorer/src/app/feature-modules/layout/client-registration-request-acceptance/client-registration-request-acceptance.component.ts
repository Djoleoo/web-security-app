import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-client-registration-request-acceptance',
  templateUrl: './client-registration-request-acceptance.component.html',
  styleUrls: ['./client-registration-request-acceptance.component.css']
})
export class ClientRegistrationRequestAcceptanceComponent {
  result = {
    accepted: false
  }

  constructor(
    public dialogRef: MatDialogRef<ClientRegistrationRequestAcceptanceComponent>, @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  accept() {
    this.result.accepted = true;

    this.close();
  }

  close() {
    this.dialogRef.close(this.result);
  }
}

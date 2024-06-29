import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-client-registration-request-rejection',
  templateUrl: './client-registration-request-rejection.component.html',
  styleUrls: ['./client-registration-request-rejection.component.css']
})
export class ClientRegistrationRequestRejectionComponent {
  result = {
    rejected: false,
    reason: ""
  }

  constructor(
    private notifier: NotifierService,
    public dialogRef: MatDialogRef<ClientRegistrationRequestRejectionComponent>, @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  reject() {
    if(this.result.reason == '') {
      this.notifier.notify('error', 'You must state a reason.');
      return;
    }

    this.result.rejected = true;

    this.close();
  }

  close() {
    this.dialogRef.close(this.result);
  }
}

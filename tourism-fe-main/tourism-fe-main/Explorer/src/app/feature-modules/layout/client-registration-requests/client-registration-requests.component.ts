import { Component, OnInit } from '@angular/core';
import { ClientRegistrationRequestView } from '../model/ClientRegistrationRequestView.model';
import { RegistrationService } from '../registration.service';
import { MatDialog } from '@angular/material/dialog';
import { ClientRegistrationRequestRejectionComponent } from '../client-registration-request-rejection/client-registration-request-rejection.component';
import { NotifierService } from 'angular-notifier';
import { ClientRegistrationRequestRejection } from '../model/ClientRegistrationRequestRejection.model';
import { ClientRegistrationRequestAcceptanceComponent } from '../client-registration-request-acceptance/client-registration-request-acceptance.component';

@Component({
  selector: 'xp-client-registration-requests',
  templateUrl: './client-registration-requests.component.html',
  styleUrls: ['./client-registration-requests.component.css']
})
export class ClientRegistrationRequestsComponent  implements OnInit{
  requests: ClientRegistrationRequestView[] = [];
  filter: String = 'ALL';
  requestId: number;

  constructor(
    private registrationService: RegistrationService,
    private dialog: MatDialog,
    private notifier: NotifierService
  ) {}

  ngOnInit(): void {
    this.getClientRegistrationRequests();
  }

  getClientRegistrationRequests() {
    this.registrationService.getClientRegistrationRequests().subscribe(
      (response: ClientRegistrationRequestView[]) => {
        this.requests = response;

        if(this.filter == 'PENDING') {
          this.requests = this.requests.filter(r => r.status == 'PENDING');
        } else if(this.filter == 'ACCEPTED') {
          this.requests = this.requests.filter(r => r.status == 'ACCEPTED');
        } else if(this.filter == 'REJECTED') {
          this.requests = this.requests.filter(r => r.status == 'REJECTED');
        }
      },
      (error: any) => {}
    );
  }

  getEnumName(name: String): String {
    if (!name) return name;
    const lowerCaseInput = name.toLowerCase();
    return lowerCaseInput.charAt(0).toUpperCase() + lowerCaseInput.slice(1);
  }

  formatDateTime(dateTime: string): string {
    const date = new Date(dateTime);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${day}.${month}.${year} ${hours}:${minutes}`;
  }

  changeFilter(filter: String) {
    this.filter = filter;
    this.getClientRegistrationRequests();
  }

  openRejectDialog(id: number): void {
    this.requestId = id;

    const dialogRef = this.dialog.open(ClientRegistrationRequestRejectionComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result.rejected) {
        this.rejectRequest(result.reason)
      }
    });
  }

  openAcceptDialog(id: number): void {
    this.requestId = id;

    const dialogRef = this.dialog.open(ClientRegistrationRequestAcceptanceComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result.accepted) {
        this.acceptRequest()
      }
    });
  }

  rejectRequest(reason: string) {
    let rejection = new ClientRegistrationRequestRejection();
    rejection.reason = reason;

    this.registrationService.rejectClientRegistrationRequest(this.requestId, rejection).subscribe(
      (response: any[]) => {
        this.notifier.notify('success', 'Successfully rejected a registration request.');
        this.getClientRegistrationRequests();
      },
      (error: any) => {}
    );
  }

  acceptRequest() {
    this.registrationService.acceptClientRegistrationRequest(this.requestId).subscribe(
      (response: any[]) => {
        this.notifier.notify('success', 'Successfully accepted a registration request.');
        this.getClientRegistrationRequests();
      },
      (error: any) => {}
    );
  }

  isRequestsEmpty(): boolean {
    if(this.requests.length == 0) {
      return true;
    }

    return false;
  }
}

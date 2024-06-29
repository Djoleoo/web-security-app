import { Component, OnInit } from '@angular/core';
import { AdvertisementRequest } from '../model/AdvertisementRequest.model';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { AdvertisementService } from '../advertisement.service';
import { NotifierService } from 'angular-notifier';
import { Advertisement } from '../model/Advertisement.model';

@Component({
  selector: 'xp-advertisement-request-list',
  templateUrl: './advertisement-request-list.component.html',
  styleUrls: ['./advertisement-request-list.component.css']
})
export class AdvertisementRequestListComponent  implements OnInit {
  unprocessedRequests: AdvertisementRequest[] = [];
  selectedRequest: AdvertisementRequest | null = null;
  slogan: string = '';
  advertisement: Advertisement = new Advertisement(0, '', '', '', '', '', 0);

  constructor(private tokenStorage: TokenStorage,
    private advertisementService: AdvertisementService,
    private notifier: NotifierService,
){
}

  ngOnInit(): void {
    this.loadUnprocessedRequests();
  }

  showForm(request: AdvertisementRequest): void {
    this.selectedRequest = request;
    this.slogan = ''; // Clear the slogan input when the form is displayed
  }

  submitForm(): void {
    console.log('Slogan:', this.slogan);
    console.log(this.selectedRequest);
    if(this.selectedRequest != null){
      this.advertisement = new Advertisement(
        0,
        this.selectedRequest.username,
        this.selectedRequest.activeFrom,
        this.selectedRequest.activeUntil,
        this.selectedRequest.description,
        this.slogan,
        this.selectedRequest.id
      );

      console.log(this.advertisement);
      this.createAdvertisement();
    }

    console.log("AD: ", this.advertisement);
    this.selectedRequest = null;

    this.loadUnprocessedRequests();
  }
  
  createAdvertisement(){
    this.advertisementService.createAdvertisement(this.advertisement).subscribe(
      (response: any) => {
        console.log("odradjeno");
        this.notifier.notify('succes', "You succesfully created an advertisement request !");
      },
      (error: any) => {
        console.log(error.error)
        this.notifier.notify('error', error.error);
      }
    )
  }

  loadUnprocessedRequests(): void {
    this.advertisementService.getUnprocessedRequests()
      .subscribe((requests: AdvertisementRequest[]) => {
        this.unprocessedRequests = requests;
        console.log('Unprocessed Requests:', this.unprocessedRequests);
      });
  }
}

import { Component, OnInit } from '@angular/core';
import { AdvertisementRequest } from '../model/AdvertisementRequest.model';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { AdvertisementService } from '../advertisement.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'xp-advertisement-request',
  templateUrl: './advertisement-request.component.html',
  styleUrls: ['./advertisement-request.component.css']
})
export class AdvertisementRequestComponent{
  advertisementRequest: AdvertisementRequest = new AdvertisementRequest();

  constructor(private tokenStorage: TokenStorage,
              private advertisementService: AdvertisementService,
              private notifier: NotifierService,
  ){

  }

  

  onSubmit() {
    this.advertisementRequest.username = this.tokenStorage.getUsername();
    console.log('Submitted advertisement request:', this.advertisementRequest);
    this.requestAdvertisement();
  }

  requestAdvertisement(){
    this.advertisementService.requestAdvertisement(this.advertisementRequest).subscribe(
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

  


}

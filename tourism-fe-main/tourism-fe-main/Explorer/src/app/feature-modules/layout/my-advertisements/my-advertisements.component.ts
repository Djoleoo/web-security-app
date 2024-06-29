import { Component } from '@angular/core';
import { AdvertisementService } from '../advertisement.service';
import { Advertisement } from '../model/Advertisement.model';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';

@Component({
  selector: 'xp-my-advertisements',
  templateUrl: './my-advertisements.component.html',
  styleUrls: ['./my-advertisements.component.css']
})
export class MyAdvertisementsComponent {
  advertisements: Advertisement[] = [];
  currentDate: Date = new Date(); 

  constructor(private advertisementService: AdvertisementService, 
              private tokenStorage: TokenStorage
  ) {}

  ngOnInit(): void {
    this.loadAdvertisements();
  }

  loadAdvertisements(): void {
    this.advertisementService.getClientsAdvertisements(this.tokenStorage.getUsername())
      .subscribe((ads: Advertisement[]) => {
        this.advertisements = ads;
        console.log('Advertisements:', this.advertisements);
      });
  }

  getStatus(activeUntil: string): string {
    const activeUntilDate: Date = new Date(activeUntil);
    return activeUntilDate > this.currentDate ? 'Active' : 'Inactive';
  }
}

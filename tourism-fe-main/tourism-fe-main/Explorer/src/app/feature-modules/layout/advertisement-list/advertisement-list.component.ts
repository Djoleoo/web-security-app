import { Component, OnInit } from '@angular/core';
import { Advertisement } from '../model/Advertisement.model';
import { AdvertisementService } from '../advertisement.service';

@Component({
  selector: 'xp-advertisement-list',
  templateUrl: './advertisement-list.component.html',
  styleUrls: ['./advertisement-list.component.css']
})
export class AdvertisementListComponent implements OnInit{
  advertisements: Advertisement[] = [];
  currentDate: Date = new Date(); 

  constructor(private advertisementService: AdvertisementService) {}

  ngOnInit(): void {
    console.log("AAAAAAAAAA");
    this.loadAdvertisements();
  }

  loadAdvertisements(): void {
    this.advertisementService.getAdvertisements()
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

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AdvertisementRequest } from './model/AdvertisementRequest.model';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';
import { Advertisement } from './model/Advertisement.model';
import { useAnimation } from '@angular/animations';

@Injectable({
  providedIn: 'root'
})
export class AdvertisementService {

  constructor(private http:HttpClient) { }

  requestAdvertisement(request: AdvertisementRequest): Observable<any>{
    var url = environment.apiHost + "advertisement/createRequest";
    return this.http.post(url, request);
  }

  getUnprocessedRequests(): Observable<AdvertisementRequest[]> {
    const url = environment.apiHost + 'advertisement/unprocessedRequests';
    return this.http.get<AdvertisementRequest[]>(url);
  }

  createAdvertisement(ad: Advertisement): Observable<any>{
    var url = environment.apiHost + "advertisement/create";
    return this.http.post(url, ad);
  }

  getAdvertisements(): Observable<Advertisement[]>{
    const url = environment.apiHost + 'advertisement/getAdvertisements';
    return this.http.get<Advertisement[]>(url);
  }

  getClientsAdvertisements(username: String): Observable<Advertisement[]>{
    const url = environment.apiHost + 'advertisement/getClientsAdvertisements/' + username;
    console.log("URL", url);
    return this.http.get<Advertisement[]>(url);
  }
}

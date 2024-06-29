import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { environment } from 'src/env/environment';
import { ClientRegistrationRequestCreation } from './model/ClientRegistrationRequestCreation.model';
import { UserActivation } from './model/UserActivation.model';
import { ClientRegistrationRequestView } from './model/ClientRegistrationRequestView.model';
import { ClientRegistrationRequestRejection } from './model/ClientRegistrationRequestRejection.model';
import { UserCreation } from './model/UserCreation.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient,
    private router: Router) { }

  requestClientRegistration(request: ClientRegistrationRequestCreation): Observable<any> {
    var url = environment.apiHost + "registration/request";
    return this.http.post(url, request);
  }

  activateUserAccount(activation: UserActivation): Observable<any> {
    var url = environment.apiHost + "registration/activate-user";
    return this.http.put(url, activation);
  }

  getClientRegistrationRequests(): Observable<ClientRegistrationRequestView[]> {
    var url = environment.apiHost + "registration/client-registration-requests";
    return this.http.get<ClientRegistrationRequestView[]>(url);
  }

  rejectClientRegistrationRequest(requestId: number, reason: ClientRegistrationRequestRejection):Observable<any> {
    var url = environment.apiHost + "registration/reject/" + requestId;
    return this.http.put(url, reason);
  }

  acceptClientRegistrationRequest(requestId: number):Observable<any> {
    var url = environment.apiHost + "registration/accept/" + requestId;
    return this.http.put(url, {});
  }

  adminCreatesUser(userCreation: UserCreation): Observable<any>{
    var url = environment.apiHost + "registration/adminCreatesUser";
    return this.http.post(url, userCreation);
  }

  deleteUserAccount(): Observable<any> {
    var url = environment.apiHost + "accounts";
    return this.http.delete<any>(url);
  }
}

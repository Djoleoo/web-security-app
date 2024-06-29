import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientEmployeeDto } from './model/ClientEmployee.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http:HttpClient) { }

  updateUser(username: string,newusername:string, firstName: string, lastName: string, address: string, city: string, country: string, phoneNumber: string): Observable<any> {
    
    const url = `https://localhost/api/client/updateUser?username=${username}&newusername=${newusername}&firstName=${firstName}&lastName=${lastName}&address=${address}&city=${city}&country=${country}&phoneNumber=%2B${phoneNumber}`;

    // Make the HTTP POST request
    return this.http.post(url, null);
  }


}

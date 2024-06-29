import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http:HttpClient) { }

  updateUser(username: string, firstName: string, lastName: string, address: string, city: string, country: string, phoneNumber: string): Observable<any> {
    
    const url = `https://localhost/api/employee/updateUser?username=${username}&firstName=${firstName}&lastName=${lastName}&address=${address}&city=${city}&country=${country}&phoneNumber=%2B${phoneNumber}`;

    // Make the HTTP POST request
    return this.http.post(url, null);
  }
}

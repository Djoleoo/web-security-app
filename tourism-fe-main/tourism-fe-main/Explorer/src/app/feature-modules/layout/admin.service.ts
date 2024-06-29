import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientEmployeeDto } from './model/ClientEmployee.model';
import { PersonalInfo } from './model/PersonalInfo.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http:HttpClient) { }

  updateUser(username: string, firstName: string, lastName: string, address: string, city: string, country: string, phoneNumber: string): Observable<any> {
    
    const url = `https://localhost/api/admin/updateUser?username=${username}&firstName=${firstName}&lastName=${lastName}&address=${address}&city=${city}&country=${country}&phoneNumber=%2B${phoneNumber}`;

    // Make the HTTP POST request
    return this.http.post(url, null);
  }

  getClientsAndEmployees(): Observable<ClientEmployeeDto[]> {
    const url = `https://localhost/api/admin/clientsAndEmployees`;
    return this.http.get<ClientEmployeeDto[]>(url);
  }

  getUserByUsername(username: string): Observable<PersonalInfo> {
    const url = `https://localhost/api/admin/${username}`;
    return this.http.get<PersonalInfo>(url);
  }

  blockUser(username: string): Observable<void> {
    const url = `https://localhost/api/admin/block`;
    return this.http.post<void>(url, { username });
  }

  recoverPassword(email: string): Observable<void> {
    const url = `https://localhost/api/login/recover-password`;
    return this.http.post<void>(url, { email });
  }

  resetPassword(token: string, newPassword: string): Observable<void> {
    const url = `https://localhost/api/login/reset-password`;
    return this.http.post<void>(url, { token, newPassword });
  }
}

import { Component, OnInit } from '@angular/core';
import { ClientEmployeeDto } from '../model/ClientEmployee.model';
import { AdminService } from '../admin.service';

@Component({
  selector: 'xp-client-employee-list',
  templateUrl: './client-employee-list.component.html',
  styleUrls: ['./client-employee-list.component.css']
})
export class ClientEmployeeListComponent implements OnInit {
  clientsAndEmployees: ClientEmployeeDto[] = [];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.getClientsAndEmployees();
  }

  getClientsAndEmployees(): void {
    this.adminService.getClientsAndEmployees()
      .subscribe(
        data => {
          this.clientsAndEmployees = data;
          console.log('Clients and employees:', this.clientsAndEmployees);
        },
        error => {
          console.error('Error fetching clients and employees:', error);
        }
      );
  }

  blockUser(clientEmployee: ClientEmployeeDto): void {
    this.adminService.blockUser(clientEmployee.username)
      .subscribe(
        () => {
          console.log(`User ${clientEmployee.username} blocked successfully`);
          this.getClientsAndEmployees(); // Refresh the list after blocking a user
        },
        error => {
          console.error('Error blocking user:', error);
        }
      );
  }
}

export interface ClientEmployeeDto {
    username: string;
    firstName: string;
    lastName: string;
    role: Role;
  }

  export enum Role {
    CLIENT = 'CLIENT',
    EMPLOYEE = 'EMPLOYEE',
    ADMIN = 'ADMIN'
  }
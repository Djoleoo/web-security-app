export class ClientRegistrationRequestView {
    id: number = 0;
    email: string = '';
    password: string = '';
    repeatPassword: string = '';
    isIndividual: boolean = true;
    phoneNumber: string = '';
    firstName: string = '';
    lastName: string = '';
    companyName: string = '';
    taxIdentificationNumber: string = '';
    country: string = '';
    city: string = '';
    clientPackage: string = '';
    address: string = '';
    dateTime: string = '';
    status: string = '';
    rejectionReason: string = 'Razlog';
  
    constructor() {}
  }
export class ClientRegistrationRequestCreation {
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
    clientPackage: string = 'STANDARD';
    address: string = '';
    dateTime: string = '';
    reCaptchaResponse: string = '';
  
    constructor() {}
  }
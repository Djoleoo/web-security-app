export class PersonalInfo {
    username: string = '';
    email: string = '';
    firstName: string = '';
    lastName: string = '';
    address: string = '';
    city: string = '';
    country: string = '';
    phoneNumber: string = '';
    companyName: string = '';
    taxIdentificationNumber: string = '';
    
    constructor(username: string, email: string, firstName: string, lastName: string, address: string, city: string, country: string, phoneNumber: string,  companyName: string,  taxIdentificationNumber: string) {
      this.username = username;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.city = city;
      this.country = country;
      this.phoneNumber = phoneNumber;
      this.companyName = companyName;
      this.taxIdentificationNumber = taxIdentificationNumber;
    }
  }
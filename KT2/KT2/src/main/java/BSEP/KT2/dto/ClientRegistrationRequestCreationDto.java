package BSEP.KT2.dto;

import java.time.LocalDateTime;

public class ClientRegistrationRequestCreationDto {
    private String email;

    private String password;

    private String repeatPassword;

    private boolean isIndividual;

    private String phoneNumber;

    private String firstName = "";

    private String lastName = "";

    private String companyName = "";

    private String taxIdentificationNumber = "";

    private String country;

    private String city;

    private String clientPackage;

    private String address;

    private LocalDateTime dateTime;

    private String reCaptchaResponse;

    public ClientRegistrationRequestCreationDto() {}

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getRepeatPassword() { return this.repeatPassword; }
    public void setRepeatPassword(String repeatPassword) { this.repeatPassword = repeatPassword; }

    public boolean getIsIndividual() { return this.isIndividual; }
    public void setIsIndividual(boolean isIndividual) { this.isIndividual = isIndividual; }

    public String getPhoneNumber() { return this.phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCompanyName() { return this.companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTaxIdentificationNumber() { return this.taxIdentificationNumber; }
    public void setTaxIdentificationNumber(String taxIdentificationNumber) { this.taxIdentificationNumber = taxIdentificationNumber; }

    public String getCountry() { return this.country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return this.city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public String getClientPackage() { return this.clientPackage; }
    public void setClientPackage(String clientPackage) { this.clientPackage = clientPackage; }

    public LocalDateTime getDateTime() { return this.dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getReCaptchaResponse() { return this.reCaptchaResponse; }
    public void setReCaptchaResponse(String reCaptchaResponse) { this.reCaptchaResponse = reCaptchaResponse; }
}

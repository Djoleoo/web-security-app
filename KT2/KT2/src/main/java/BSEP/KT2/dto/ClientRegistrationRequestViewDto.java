package BSEP.KT2.dto;

import java.time.LocalDateTime;

import BSEP.KT2.model.ClientRegistrationRequest;
import BSEP.KT2.model.enums.ClientType;

public class ClientRegistrationRequestViewDto {
    private Integer id;

    private String email;

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

    private String status;

    private String rejectionReason;

    public ClientRegistrationRequestViewDto(ClientRegistrationRequest request) {
        this.id = request.getId();
        this.email = request.getEmail();
        this.isIndividual = request.getType().equals(ClientType.INDIVIDUAL) ? true : false;
        this.phoneNumber = request.getPhoneNumber();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.companyName = request.getCompanyName();
        this.taxIdentificationNumber = request.getTaxIdentificationNumber();
        this.country = request.getCountry();
        this.city = request.getCity();
        this.address = request.getAddress();
        this.dateTime = request.getDateTime();
        this.clientPackage = request.getClientPackage().name();
        this.status = request.getStatus().name();
        this.rejectionReason = request.getRejectionReason();
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public boolean getIsIndividual() { return this.isIndividual; }
    public void setIsIndividual(boolean isIndividual) { this.isIndividual = isIndividual; }

    public String getPhoneNumber() { return this.phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return this.lastName; }
    public void setLasName(String lastName) { this.lastName = lastName; }

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

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return this.rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
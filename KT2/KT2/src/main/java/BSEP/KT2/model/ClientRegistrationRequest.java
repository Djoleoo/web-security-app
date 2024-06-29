package BSEP.KT2.model;

import java.time.LocalDateTime;

import BSEP.KT2.dto.ClientRegistrationRequestCreationDto;
import BSEP.KT2.model.enums.ClientPackage;
import BSEP.KT2.model.enums.ClientRegistrationRequestStatus;
import BSEP.KT2.model.enums.ClientType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "_client_registration_request")
@Inheritance(strategy = InheritanceType.JOINED)
public class ClientRegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotEmpty
    @Email
    @Column(nullable = false)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String passwordSalt;

    @Column(nullable = false)
    private ClientType type;

    @NotEmpty
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String companyName;

    @Column(nullable = true)
    private String taxIdentificationNumber;

    @NotEmpty
    @Column(nullable = false)
    private String country;

    @NotEmpty
    @Column(nullable = false)
    private String city;

    @NotEmpty
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private ClientPackage clientPackage;

    @Column(nullable = false)
    private ClientRegistrationRequestStatus status;

    @Column(nullable = false)
    private String rejectionReason;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public ClientRegistrationRequest() {}


    public ClientRegistrationRequest(ClientRegistrationRequestCreationDto request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.type = request.getIsIndividual() ? ClientType.INDIVIDUAL : ClientType.LEGAL;
        this.phoneNumber = request.getPhoneNumber();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.companyName = request.getCompanyName();
        this.taxIdentificationNumber = request.getTaxIdentificationNumber();
        this.country = request.getCountry();
        this.city = request.getCity();
        this.address = request.getAddress();
        this.clientPackage = ClientPackage.valueOf(request.getClientPackage());
        this.dateTime = request.getDateTime();
        this.status = ClientRegistrationRequestStatus.PENDING;
        this.rejectionReason = "";

        this.validate(request.getRepeatPassword());
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordSalt() { return this.passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public ClientType getType() { return this.type; }
    public void setType(ClientType type) { this.type = type; }

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

    public ClientRegistrationRequestStatus getStatus() { return this.status; }
    public void setStatus(ClientRegistrationRequestStatus status) { this.status = status; }

    public String getRejectionReason() { return this.rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getDateTime() { return this.dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    private void validate(String repeatPassword) {
        String emailPattern = "^\\S+@\\S+\\.\\S+$";
        if(email == null || email.isEmpty() || !email.matches(emailPattern)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$";
        if(password == null || password.isEmpty() || !password.matches(passwordPattern)) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        if(!password.equals(repeatPassword)) {
            throw new IllegalArgumentException("Password and password confirmation must match.");
        }

        String phoneNumberPattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{2,3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        if(phoneNumber == null || phoneNumber.isEmpty() || !phoneNumber.matches(phoneNumberPattern)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        if (type == null) {
            throw new IllegalArgumentException("Client type cannot be empty.");
        }

        if (type == ClientType.INDIVIDUAL) {
            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
                throw new IllegalArgumentException("First name and last name cannot be empty for individual type.");
            }
            if (companyName != null && !companyName.isEmpty()) {
                throw new IllegalArgumentException("Company name must be empty for individual type.");
            }
            if (taxIdentificationNumber != null && !taxIdentificationNumber.isEmpty()) {
                throw new IllegalArgumentException("Tax identification number must be empty for individual type.");
            }
        } else if (type == ClientType.LEGAL) {
            if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
                throw new IllegalArgumentException("First name and last name must be empty for legal type.");
            }
            if (companyName == null || companyName.isEmpty()) {
                throw new IllegalArgumentException("Company name cannot be empty for legal type.");
            }
            if (taxIdentificationNumber == null || taxIdentificationNumber.isEmpty()) {
                throw new IllegalArgumentException("Tax identification number cannot be empty for legal type.");
            }
            String taxIdentificationNumberPattern = "^1\\d{8}$";
            if(!taxIdentificationNumber.matches(taxIdentificationNumberPattern)) {
                throw new IllegalArgumentException("Invalid tax identification number format.");
            }
        }

        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty.");
        }

        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty.");
        }

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }

        if (clientPackage == null) {
            throw new IllegalArgumentException("Client package cannot be empty.");
        }

        if (dateTime == null) {
            throw new IllegalArgumentException("Date and time cannot be empty.");
        }

        LocalDateTime now = LocalDateTime.now();
        if(dateTime.isAfter(now)) {
            throw new IllegalArgumentException("Date and time cannot be future.");
        }
    }

    public void accept() {
        if(status.equals(ClientRegistrationRequestStatus.PENDING)) {
            setStatus(ClientRegistrationRequestStatus.ACCEPTED);
        }
        else {
            throw new IllegalArgumentException("Request must be pending to be accepted.");
        }
    }

    public void reject(String reason) {
        if(status.equals(ClientRegistrationRequestStatus.PENDING)) {
            setStatus(ClientRegistrationRequestStatus.REJECTED);
        }
        else {
            throw new IllegalArgumentException("Request must be pending to be rejected.");
        }

        if(reason != null && !reason.isEmpty()) {
            setRejectionReason(reason);
        }
        else {
            throw new IllegalArgumentException("Reason cannot be empty.");
        }
    }

    public boolean isRecentlyRejected() {
        boolean isRecentlyRejected = dateTime.isAfter(LocalDateTime.now().minusDays(7))
                && status.equals(ClientRegistrationRequestStatus.REJECTED);
        
        return isRecentlyRejected;
    }

    public void invalidate() {
        status = ClientRegistrationRequestStatus.INVALID;
    }
}

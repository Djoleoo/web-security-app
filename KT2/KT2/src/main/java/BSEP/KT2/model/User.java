package BSEP.KT2.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.api.client.auth.openidconnect.IdToken.Payload;

import BSEP.KT2.model.enums.ClientPackage;
import BSEP.KT2.model.enums.ClientType;
import BSEP.KT2.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String passwordSalt;

    @Column(nullable = true)
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

    @Enumerated
    private Role role;

    @Column(nullable = false)
    private boolean isActivated;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isFirstLogin;

    @Column(nullable = false)
    private boolean is2FactorAuthenticationEnabled;

    @Column(nullable = false)
    private boolean isBlocked;

    private String key2FactorAuthentication;

    public User() {}

    public User(ClientRegistrationRequest request) {
        this.username = request.getEmail();
        this.password = request.getPassword();
        this.passwordSalt = request.getPasswordSalt();
        this.type = request.getType();
        this.phoneNumber = request.getPhoneNumber();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.companyName = request.getCompanyName();
        this.taxIdentificationNumber = request.getTaxIdentificationNumber();
        this.country = request.getCountry();
        this.city = request.getCity();
        this.address = request.getAddress();
        this.clientPackage = request.getClientPackage();
        this.role = Role.CLIENT; 
        this.isActivated = false;
        this.createdAt = LocalDateTime.now();
        this.isFirstLogin = true;
        this.is2FactorAuthenticationEnabled = false;
        this.key2FactorAuthentication = "";
        this.isBlocked = false;

        this.validateClient();
    }

    public User(Payload userInfo) {
        this.username = (String) userInfo.get("email");
        this.password = "";
        this.passwordSalt = "";
        this.type = ClientType.INDIVIDUAL;
        this.phoneNumber = "Unknown";
        this.firstName = (String) userInfo.get("given_name");
        this.lastName = (String) userInfo.get("family_name");
        this.companyName = "";
        this.taxIdentificationNumber = "";
        this.country = "Unknown";
        this.city = "Unknown";
        this.address = "Unknown";
        this.clientPackage = ClientPackage.BASE;
        this.role = Role.CLIENT; 
        this.isActivated = true;
        this.createdAt = LocalDateTime.now();
        this.isFirstLogin = false;
        this.is2FactorAuthenticationEnabled = false;
        this.key2FactorAuthentication = "";
        this.isBlocked = false;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

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

    public boolean getIsActivated() { return this.isActivated; }
    public void setIsActivated(boolean isActivated) { this.isActivated = isActivated; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean getIsFirstLogin() { return this.isFirstLogin; }
    public void setIsFirstLogin(boolean isFirstLogin) { this.isFirstLogin = isFirstLogin; }

    public boolean getIs2FactorAuthenticationEnabled() { return this.is2FactorAuthenticationEnabled; }
    public void setIs2FactorAuthenticationEnabled(boolean is2FactorAuthenticationEnabled) { this.is2FactorAuthenticationEnabled = is2FactorAuthenticationEnabled; }

    public String getKey2FactorAuthentication() { return this.key2FactorAuthentication; }
    public void setKey2FactorAuthentication(String key2FactorAuthentication) { this.key2FactorAuthentication = key2FactorAuthentication; }

    public boolean getIsBlocked() { return this.isBlocked; }
    public void setIsBlocked(boolean isBlocked) { this.isBlocked = isBlocked; }

    private void validateClient() {
        String emailPattern = "^\\S+@\\S+\\.\\S+$";
        if(username == null || username.isEmpty() || !username.matches(emailPattern)) {
            throw new IllegalArgumentException("Invalid username format.");
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
    }

    public void activate() {
        if(isActivated) {
            throw new IllegalArgumentException("User is already activated.");
        } else {
            isActivated = true;
        }
    }

    public void block(){
        if(isBlocked){
            throw new IllegalArgumentException("User is blocked already.");
        }else{
            isBlocked = true;
        }
    }

    public void updateFirstLogin() {
        if(!isFirstLogin) {
            throw new IllegalArgumentException("This is not first login.");
        } else {
            isFirstLogin = false;
        }
    }

    public boolean isInactive() {
        boolean isInactive = !isActivated && createdAt.isBefore(LocalDateTime.now().minusDays(7));

        return isInactive;
    }

    public void enable2FactorAuthentication() {
        if(is2FactorAuthenticationEnabled) {
            throw new IllegalArgumentException("2 factor authentication is already enabled.");
        } else {
            is2FactorAuthenticationEnabled = true;
        }
    }

    public void disable2FactorAuthentication() {
        if(!is2FactorAuthenticationEnabled) {
            throw new IllegalArgumentException("2 factor authentication is already disabled.");
        } else {
            is2FactorAuthenticationEnabled = false;
            key2FactorAuthentication = "";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public boolean isAccountNonExpired() {
       
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
    }


    @Override
    public boolean isAccountNonLocked() {
      
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
    }


    @Override
    public boolean isCredentialsNonExpired() {
       
        throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
    }


    @Override
    public boolean isEnabled() {
        
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }
}

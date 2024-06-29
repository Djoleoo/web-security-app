package BSEP.KT2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "_passwordless_login_attempt")
@Inheritance(strategy = InheritanceType.JOINED)
public class PasswordlessLoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false)
    private String signature;

    @Column(nullable = false)
    private boolean isUsed;

    public PasswordlessLoginAttempt() {}

    public PasswordlessLoginAttempt(String signature) {
        this.signature = signature;
        this.isUsed = false;
    }

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public boolean getisUsed() { return this.isUsed; }
    public void setisUsed(boolean isUsed) { this.isUsed = isUsed; }

    public void use() {
        if(!isUsed) {
            isUsed = true;
        }
        else {
            throw new SecurityException("This link was already used before.");
        }
    }
}

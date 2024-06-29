package BSEP.KT2.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "_advertisementRequest")
public class AdvertisementRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username; 

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime activeFrom;

    @Column(nullable = false)
    private LocalDateTime activeUntil;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private boolean isCreated;

    // Constructor
    public AdvertisementRequest() {
    }

    public AdvertisementRequest(String username, LocalDateTime deadline, LocalDateTime activeFrom, LocalDateTime activeUntil, String description, boolean isCreated) {
        this.username = username;
        this.deadline = deadline;
        this.activeFrom = activeFrom;
        this.activeUntil = activeUntil;
        this.description = description;
        this.isCreated = isCreated;

        this.validateData();
    }

    public void validateData() {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future.");
        }
        if (activeFrom.isAfter(activeUntil)) {
            throw new IllegalArgumentException("Active from must be before active until.");
        }
        if (deadline.isAfter(activeFrom)) {
            throw new IllegalArgumentException("Deadline must be before active from.");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(LocalDateTime activeFrom) {
        this.activeFrom = activeFrom;
    }

    public LocalDateTime getActiveUntil() {
        return activeUntil;
    }

    public void setActiveUntil(LocalDateTime activeUntil) {
        this.activeUntil = activeUntil;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}

package BSEP.KT2.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "_advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer requestId;
    
    @Column(nullable = false)
    private LocalDateTime activeFrom;

    @Column(nullable = false)
    private LocalDateTime activeUntil;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String slogan;

    

    public Advertisement() {
    }

    public Advertisement(Integer id, Integer requestId, String username, LocalDateTime activeFrom, LocalDateTime activeUntil, String description, String slogan) {
        this.id = id;
        this.requestId = requestId;
        this.username = username;
        this.activeFrom = activeFrom;
        this.activeUntil = activeUntil;
        this.description = description;
        this.slogan = slogan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer requestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}

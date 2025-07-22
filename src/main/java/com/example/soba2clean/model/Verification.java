package com.example.soba2clean.model;

import com.example.soba2clean.enums.VerificationType;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
public class Verification extends AuditableEntity {

    private String token;

    @Enumerated(EnumType.STRING)
    private VerificationType type;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public String getToken() {
        return token;
    }

    public String getType() {
        return type.getTypeName();
    }

    public User getUser() {
        return user;
    }

    public void setEmailVerification(User user) {
        this.type = VerificationType.EMAIL_VERIFICATION;
        this.token = UUID.randomUUID().toString();
        this.user = user;
    }
}

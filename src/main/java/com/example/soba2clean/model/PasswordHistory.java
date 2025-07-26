package com.example.soba2clean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PasswordHistory extends AuditableEntity {
    @Column(nullable = false)
    private String historicalPasswordHash;

    public String getHistoricalPasswordHash() {
        return historicalPasswordHash;
    }

    public void setHistoricalPasswordHash(String historicalPasswordHash) {
        this.historicalPasswordHash = historicalPasswordHash;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

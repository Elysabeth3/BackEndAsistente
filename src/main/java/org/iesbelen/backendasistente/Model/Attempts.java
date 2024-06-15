package org.iesbelen.backendasistente.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attempts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Basic
    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;
    @Basic
    @Column(name = "attempt_time", nullable = false)
    private Timestamp attemptTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(Timestamp attemptTime) {
        this.attemptTime = attemptTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attempts attempts = (Attempts) o;
        return id == attempts.id && userId == attempts.userId && Objects.equals(ipAddress, attempts.ipAddress) && Objects.equals(attemptTime, attempts.attemptTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, ipAddress, attemptTime);
    }


}

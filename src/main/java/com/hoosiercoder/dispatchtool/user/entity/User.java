package com.hoosiercoder.dispatchtool.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Author: HoosierCoder
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private String firstName;
    @NotNull
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "hashed_password", length = 60, nullable = true)
    private String hashedPassword;

    @Column(name = "username", length = 30, unique = true, nullable = false)
    private String username;

    @CreationTimestamp
    @Column(name = "created_date")
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ColumnDefault("true")
    private boolean isActive;

    public User(String firstName, String lastName, UserRole userRole, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public User() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userRole=" + userRole.getRole() +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}

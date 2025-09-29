package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Bill> bills;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    // Constructors
//    public User() {
//        this.creationDate = LocalDateTime.now();
//        this.modifiedDate = LocalDateTime.now();
//    }

    // Getters and setters...
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public String getFirstName() { return firstName; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//    public String getLastName() { return lastName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getHashedPassword() { return hashedPassword; }
//    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
//    public LocalDateTime getCreationDate() { return creationDate; }
//    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
//    public LocalDateTime getModifiedDate() { return modifiedDate; }
//    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
//    public Role getRole() { return role; }
//    public void setRole(Role role) { this.role = role; }

}

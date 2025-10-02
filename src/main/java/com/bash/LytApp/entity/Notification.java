package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String type;
    private String message;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "is_read")
    private Boolean isRead;


    // Set default values in the constructor
    public Notification() {
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters and setters...
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }
//    public String getMessage() { return message; }
//    public void setMessage(String message) { this.message = message; }
//    public LocalDateTime getSentAt() { return sentAt; }
//    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
//    public Boolean getIsRead() { return isRead; }
//    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

}

package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        private LocalDateTime created_at;

        @OneToMany(mappedBy = "role")
        private List<User> users;

        // Constructors, Getters, Setters
        public Role() {}

        public Role(String name) {
            this.name = name;
        }

}

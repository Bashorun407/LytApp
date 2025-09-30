package com.bash.LytApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

        @OneToMany(mappedBy = "role")
        private List<User> users;

        // Constructors, Getters, Setters
        public Role() {}

        public Role(String name) {
            this.name = name;
        }

        // Getters and setters...
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<User> getUsers() { return users; }
        public void setUsers(List<User> users) { this.users = users; }
}

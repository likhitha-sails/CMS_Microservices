package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.Role;

@Entity
//username should be unique
@Table(name = "userdetails")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}


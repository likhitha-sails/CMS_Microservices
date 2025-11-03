package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userdetails", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

}


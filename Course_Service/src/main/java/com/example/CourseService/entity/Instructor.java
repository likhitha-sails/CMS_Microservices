package com.example.CourseService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Instructor name cannot be blank")
    @Size(min = 3, max = 50, message = "Instructor name must be between 3 and 50 characters")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Expertise cannot be blank")
    @Size(min = 3, max = 50, message = "Expertise must be between 3 and 100 characters")
    private String expertise;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Course> courses;
}

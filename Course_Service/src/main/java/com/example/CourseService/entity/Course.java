package com.example.CourseService.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Course title cannot be blank")
    @Size(min = 3, max = 100, message = "Course title must be between 3 and 100 characters")
    private String title;

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "Course description cannot be blank")
    @Size(min = 10, max = 1000, message = "Course description must be between 10 and 1000 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating cannot be less than 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot be more than 5.0")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @NotNull(message = "Instructor cannot be null")
    @JsonBackReference
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments;
}



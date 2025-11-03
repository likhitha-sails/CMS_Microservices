package com.example.CourseService.repository;

import com.example.CourseService.entity.Student;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface StudentRepository extends JpaRepository<Student, Long> {

}

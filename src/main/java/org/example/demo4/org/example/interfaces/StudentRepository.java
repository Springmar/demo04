package org.example.demo4.org.example.interfaces;

import org.example.demo4.org.example.instance.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA 会自动实现这个接口，无需写 SQL
public interface StudentRepository extends JpaRepository<Student, Long> {

}

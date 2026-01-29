package org.example.demo4.org.example.interfaces;

import org.example.demo4.org.example.instance.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface stdRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s ORDER BY s.lastDutyDate ASC")
    List<Student> findAllByOrderByLastDutyDateAsc();
}

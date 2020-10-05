package com.khanivorous.studentservice.student;

import com.khanivorous.studentservice.student.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {
}

package com.khanivorous.studentservice.student.services;

import com.khanivorous.studentservice.student.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {
}

package com.khanivorous.studentservice.student.services;

import com.khanivorous.studentservice.student.models.Student;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {


    Student getStudentById(int id);

    Iterable<Student> getAllStudents();

    Student addNewStudent(String name, int age);

    String deleteStudentById(int id);

}

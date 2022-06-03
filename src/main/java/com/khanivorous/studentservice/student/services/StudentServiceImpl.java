package com.khanivorous.studentservice.student.services;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentById (int id) {
        return studentRepository.findById(id).orElseThrow(()-> new NoSuchIdException(id));
    }

    public Iterable<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addNewStudent(String name, int age) {
        Student newStudent = new Student();
        newStudent.setName(name);
        newStudent.setAge(age);
        studentRepository.save(newStudent);
        return newStudent;
    }

    public String deleteStudentById(int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "student with id " +id+ " deleted";
        } else {
            throw new NoSuchIdException(id);
        }
    }
}

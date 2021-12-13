package com.khanivorous.studentservice.student.controllers;

import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.services.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/students")
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    String addNewStudent(@RequestParam String name, @RequestParam Integer age) {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        studentRepository.save(s);
        return "saved";
    }

    @GetMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Iterable<Student> getAllUsers() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Optional<Student> getUserById(@PathVariable Integer id) {
        return studentRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody
    String deleteStudent(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "deleted";
    }
}

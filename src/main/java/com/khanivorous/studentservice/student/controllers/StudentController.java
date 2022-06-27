package com.khanivorous.studentservice.student.controllers;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    Student addNewStudent(@RequestParam String name, @RequestParam Integer age) {
        return studentService.addNewStudent(name, age);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Student getUserById(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Iterable<Student> getAllUsers() {
        return studentService.getAllStudents();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody
    String deleteStudent(@PathVariable Integer id) {
        return studentService.deleteStudentById(id);
    }

    @ResponseBody
    @ExceptionHandler(NoSuchIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String studentNotFoundHandler(NoSuchIdException ex) {
        return ex.getMessage();
    }
}

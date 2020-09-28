package com.khanivorous.studentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/students")
public class MainController {

    private final StudentRepository studentRepository;

    @Autowired
    public MainController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewStudent(@RequestParam String name, @RequestParam Integer age) {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        studentRepository.save(s);
        return "saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getAllUsers() {
        return studentRepository.findAll();
    }
}

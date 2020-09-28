package com.khanivorous.studentservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentServiceApplicationTests {

    @Autowired
    MainController mainController;

    @MockBean
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {

        mainController = new MainController(studentRepository);

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentRepository.findAll()).thenReturn(studentList);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));
    }

    @Test
    public void testGetAllUsers() {
        assertThat(mainController.getAllUsers().iterator().next().getName()).isEqualTo("Ben");
        assertThat(mainController.getAllUsers().iterator().next().getAge()).isEqualTo(28);
        assertThat(mainController.getAllUsers().iterator().next().getId()).isEqualTo(1);
    }

    @Test
    public void testGetUserById() {
        assertThat(mainController.getUserById(1).get().getName()).isEqualTo("Ben");
        assertTrue(mainController.getUserById(1).isPresent());
        assertTrue(mainController.getUserById(2).isEmpty());
    }

}

package com.khanivorous.studentservice;

import com.khanivorous.studentservice.student.Student;
import com.khanivorous.studentservice.student.StudentRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentApplicationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentRepository.findAll()).thenReturn(studentList);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));

        RestAssured.basePath = "/students";
        RestAssured.port = port;
    }

    @Test
    public void testGetAllUsers() {
        given().get("/all")
                .then()
                .assertThat()
                .body("[0].name", is("Ben"))
                .body("[0].id", is(1))
                .body("[0].age", is(28));
    }

    @Test
    public void testGetUserById() {
        given().get("/1")
                .then()
                .assertThat()
                .body("name", is("Ben"))
                .body("id", is(1))
                .body("age", is(28));
    }

    @Test
    public void testAddNewStudent() {
        given().param("name", "Andy")
                .param("age", "22")
                .post("/add")
                .then()
                .assertThat()
                .body(is("saved"));
    }

    @Test
    public void testDeleteStudent() {
        given().delete("/1")
                .then()
                .assertThat()
                .body(is("deleted"));
    }

}

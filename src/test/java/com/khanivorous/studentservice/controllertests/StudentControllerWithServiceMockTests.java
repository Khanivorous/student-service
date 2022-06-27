package com.khanivorous.studentservice.controllertests;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.controllers.StudentController;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(StudentController.class)
public class StudentControllerWithServiceMockTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StudentService studentService;

    @Test
    public void testGetAllUsers() throws Exception {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentService.getAllStudents()).thenReturn(studentList);

        mockMvc.perform(get("/students/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Ben")))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].age", is(28)));
    }

    @Test
    public void testGetUserById() throws Exception {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        when(studentService.getStudentById(1)).thenReturn(student1);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ben")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(28)));
    }

    @Test
    public void testUnknownIdReturnsError() throws Exception {
        when(studentService.getStudentById(2)).thenThrow(new NoSuchIdException(2));
        mockMvc.perform(get("/students/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find student with id 2"));
    }

    @Test
    public void testAddNewStudent() throws Exception {

        Student student = new Student();
        student.setId(1);
        student.setName("Andy");
        student.setAge(22);

        when(studentService.addNewStudent(anyString(), anyInt())).thenReturn(student);

        mockMvc.perform(post("/students/add")
                        .param("name", "Andy")
                        .param("age", "22"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Andy")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    public void testDeleteStudentById() throws Exception {

        when(studentService.deleteStudentById(1)).thenReturn("student with id 1 deleted");
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("student with id 1 deleted"));
    }

    @Test
    public void testDeleteNonExistentStudentThrowsError() throws Exception {

        when(studentService.deleteStudentById(1)).thenThrow(new NoSuchIdException(1));
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find student with id 1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchIdException));

    }

}

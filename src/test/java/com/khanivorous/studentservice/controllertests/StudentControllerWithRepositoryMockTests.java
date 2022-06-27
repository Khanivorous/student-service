package com.khanivorous.studentservice.controllertests;

import com.khanivorous.studentservice.StudentServiceApplication;
import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.controllers.StudentController;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.repository.StudentRepository;
import com.khanivorous.studentservice.student.services.StudentServiceImpl;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@ContextConfiguration(classes = {StudentServiceApplication.class, StudentServiceImpl.class})
class StudentControllerWithRepositoryMockTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @Test
    public void testGetAllUsers() throws Exception {

        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentRepository.findAll()).thenReturn(studentList);

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

        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ben")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.age", is(28)));
    }

    @Test
    public void testUnknownIdReturnsError() throws Exception {

        mockMvc.perform(get("/students/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find student with id 2"));
    }

    @Test
    public void testAddNewStudent() throws Exception {

        mockMvc.perform(post("/students/add")
                        .param("name", "Andy")
                        .param("age", "22"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Andy")))
                .andExpect(jsonPath("$.id").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    public void testDeleteStudentById() throws Exception {

        when(studentRepository.existsById(1)).thenReturn(true);
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("student with id 1 deleted"));
        verify(studentRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteNonExistentStudentThrowsError() throws Exception {

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find student with id 1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchIdException));
        verify(studentRepository, never()).delete(any());
    }

}

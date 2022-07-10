package com.khanivorous.studentservice.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.controllers.StudentController;
import com.khanivorous.studentservice.student.entities.Student;
import com.khanivorous.studentservice.student.mapper.StudentMapper;
import com.khanivorous.studentservice.student.model.StudentCreationDTO;
import com.khanivorous.studentservice.student.model.StudentDTO;
import com.khanivorous.studentservice.student.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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
    private StudentMapper studentMapper;

    @MockBean
    private StudentService studentService;

    @Test
    public void testGetAllUsers() throws Exception {

        StudentDTO student1 = new StudentDTO(1,"Ben",28);

        List<StudentDTO> studentList = new ArrayList<>();
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

        StudentDTO student1 = new StudentDTO(1,"Ben", 28);

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

        StudentDTO student = new StudentDTO(1,"Andy",22);

        when(studentService.addNewStudent(anyString(), anyInt())).thenReturn(student);

        ObjectMapper mapper = new ObjectMapper();
        StudentCreationDTO studentDTO = new StudentCreationDTO("Andy", 22);
        String requestBody = mapper.writeValueAsString(studentDTO);

        mockMvc.perform(post("/students/add")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
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

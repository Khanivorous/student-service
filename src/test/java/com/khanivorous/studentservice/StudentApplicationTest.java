package com.khanivorous.studentservice;

import com.khanivorous.studentservice.student.Student;
import com.khanivorous.studentservice.student.StudentRepository;
import org.apache.commons.httpclient.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
    }

    @Test
    public void testGetAllUsers() throws Exception {
        final String baseUrl = "http://localhost:" + port + "/students/all";
        URI uri = new URI(baseUrl);
        ResponseEntity<Student[]> responseEntity = restTemplate.getForEntity(String.valueOf(uri), Student[].class);
        assertEquals("Ben", Arrays.stream(responseEntity.getBody()).findFirst().get().getName());
        assertEquals(1, Arrays.stream(responseEntity.getBody()).findFirst().get().getId().intValue());
        assertEquals(28, Arrays.stream(responseEntity.getBody()).findFirst().get().getAge().intValue());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetUserById() throws Exception {
        final String baseUrl = "http://localhost:" + port + "/students/1";
        URI uri = new URI(baseUrl);
        ResponseEntity<Student> responseEntity = restTemplate.getForEntity(String.valueOf(uri), Student.class);
        assertEquals("Ben", responseEntity.getBody().getName());
        assertEquals(1, responseEntity.getBody().getId().intValue());
        assertEquals(28, responseEntity.getBody().getAge().intValue());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

//    @Test
//    public void testAddNewStudent() throws Exception {
//        final String baseUrl = "http://localhost:" + port + "/students/add";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity()
//        mockMvc.perform(post("/students/add")
//                .param("name", "Andy")
//                .param("age", "22"))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("saved"));
//    }
//
//    @Test
//    public void testDeleteStudent() throws Exception {
//        mockMvc.perform(delete("/students/1"))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().string("deleted"));
//    }
}

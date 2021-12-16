package com.khanivorous.studentservice.servicetests;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.repository.StudentRepository;
import com.khanivorous.studentservice.student.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    StudentService studentService;

    @BeforeEach
    public void setUp() {
        this.studentService = new StudentService(studentRepository);
    }

    @Test
    public void testGetStudentById() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));

        Student response = studentService.getStudentById(1);
        assertEquals(student1, response);
    }

    @Test
    public void testGetUnknownIdReturnsError()  {

        Exception exception = assertThrows(NoSuchIdException.class, () -> {
            studentService.getStudentById(1);
        });

        String expectedMessage = "Could not find student with id 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetAllStudents() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);

        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);

        when(studentRepository.findAll()).thenReturn(studentList);

        Iterable<Student> response = studentService.getAllStudents();
        assertEquals(studentList,response);
    }

    @Test
    public void testAddNewStudent() {

        Student newStudent = new Student();
        newStudent.setName("john");
        newStudent.setAge(23);

        when(studentRepository.save(any())).thenReturn(newStudent);

        Student actualStudent = studentService.addNewStudent("john",23);

        assertEquals(newStudent.getName(),actualStudent.getName());
        assertEquals(newStudent.getAge(),actualStudent.getAge());
        assertEquals(newStudent.getId(), actualStudent.getId());

    }


    @Test
    public void testDeleteById() {
       when(studentRepository.existsById(1)).thenReturn(true);
        String response = studentService.deleteStudentById(1);
        assertEquals("student with id 1 deleted", response);
    }

    @Test
    public void testDeleteByNonExistentIdThrowsError() {

        when(studentRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(NoSuchIdException.class, () -> {
            studentService.deleteStudentById(1);
        });

        String expectedMessage = "Could not find student with id 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(studentRepository, never()).delete(any());
    }

}

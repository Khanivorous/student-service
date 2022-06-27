package com.khanivorous.studentservice.servicetests;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.models.Student;
import com.khanivorous.studentservice.student.repository.StudentRepository;
import com.khanivorous.studentservice.student.services.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Spy
    private StudentRepository studentRepository;

    private StudentServiceImpl serviceUnderTest;

    @BeforeEach
    public void setUp() {
        this.serviceUnderTest = new StudentServiceImpl(studentRepository);
    }

    @Test
    public void testGetStudentById() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ben");
        student1.setAge(28);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student1));

        Student response = serviceUnderTest.getStudentById(1);
        assertEquals(student1, response);
    }

    @Test
    public void testGetUnknownIdReturnsError() {

        Exception exception = assertThrows(NoSuchIdException.class, () -> serviceUnderTest.getStudentById(1));

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

        Iterable<Student> response = serviceUnderTest.getAllStudents();
        assertEquals(studentList, response);
    }

    @Test
    public void testAddNewStudent() {
        Student actualStudent = serviceUnderTest.addNewStudent("john", 23);

        assertEquals("john", actualStudent.getName());
        assertEquals(23, actualStudent.getAge());
    }


    @Test
    public void testDeleteById() {
        when(studentRepository.existsById(1)).thenReturn(true);
        String response = serviceUnderTest.deleteStudentById(1);
        assertEquals("student with id 1 deleted", response);
        verify(studentRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteByNonExistentIdThrowsError() {
        when(studentRepository.existsById(1)).thenReturn(false);
        Exception exception = assertThrows(NoSuchIdException.class, () -> serviceUnderTest.deleteStudentById(1));
        String expectedMessage = "Could not find student with id 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(studentRepository, never()).delete(any());
    }

}

package com.khanivorous.studentservice.student.controllers;

import com.khanivorous.studentservice.student.NoSuchIdException;
import com.khanivorous.studentservice.student.model.StudentCreationDTO;
import com.khanivorous.studentservice.student.model.StudentDTO;
import com.khanivorous.studentservice.student.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Add a new Student")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Added new student",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class)) }
            )})
    @PostMapping(path = "/add",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    StudentDTO addNewStudent(@RequestBody StudentCreationDTO student) {
        String name = student.name();
        int age = student.age();
        return studentService.addNewStudent(name, age);
    }

    @Operation(summary = "Find student by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "found student",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content =  @Content)})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    StudentDTO getUserById(@Parameter(description = "id of student to be searched") @PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @Operation(summary = "Find all students")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "all students",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class))
                    )}
            )})
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<StudentDTO> getAllUsers() {
        return studentService.getAllStudents();
    }

    @Operation(summary = "Delete student by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "deleted student",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content =  @Content)})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody
    String deleteStudent(@Parameter(description = "id of student to be deleted") @PathVariable Integer id) {
        return studentService.deleteStudentById(id);
    }

    @ResponseBody
    @ExceptionHandler(NoSuchIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String studentNotFoundHandler(NoSuchIdException ex) {
        return ex.getMessage();
    }
}

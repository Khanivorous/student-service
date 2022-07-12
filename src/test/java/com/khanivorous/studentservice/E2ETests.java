package com.khanivorous.studentservice;

import com.khanivorous.studentservice.student.model.StudentCreationDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.basePath = "/students";
        RestAssured.port = port;
    }

    @Test
    public void testAddGetAndDelete() {

        StudentCreationDTO studentDTO = new StudentCreationDTO("Andy", 22);

        given().body(studentDTO)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .assertThat()
                .body(is("{\"id\":1,\"name\":\"Andy\",\"age\":22}"))
                .statusCode(is(201));

        given().get("/1")
                .then()
                .assertThat()
                .body("name", is("Andy"))
                .body("id", is(1))
                .body("age", is(22))
                .statusCode(is(200));

        given().delete("/1")
                .then()
                .assertThat()
                .statusCode(is(204));

        given().get()
                .then()
                .assertThat()
                .body("isEmpty()", is(true))
                .statusCode(is(200));
    }
}

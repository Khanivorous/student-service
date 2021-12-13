package com.khanivorous.studentservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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

        given().param("name", "Andy")
                .param("age", "22")
                .post("/add")
                .then()
                .assertThat()
                .body(is("saved"))
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
                .body(is("deleted"))
                .statusCode(is(202));

        given().get("/all")
                .then()
                .assertThat()
                .body("isEmpty()", is(true))
                .statusCode(is(200));
    }
}

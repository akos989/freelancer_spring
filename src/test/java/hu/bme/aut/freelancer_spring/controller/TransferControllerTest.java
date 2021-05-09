package hu.bme.aut.freelancer_spring.controller;

import hu.bme.aut.freelancer_spring.model.Transfer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("dev")
class TransferControllerTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "api";
    }

    @Test
    void findAllTest() {
        // given
        // test data

        // when
        Transfer[] result = given()
                .when().get("/transfers")
                .then().statusCode(is(200))
                .extract().as(Transfer[].class);

        // then
        List<Transfer> expected = Arrays.asList(
                new Transfer(null, 23.34, 34.546),
                new Transfer(null, 13.34, 344.546),
                new Transfer(null, 543.34, 344.546),
                new Transfer(null, 237.34, 364.546)
        );
        List<Transfer> resultList = Arrays.asList(result);
        assertEquals(4, resultList.size());
        assertThat(resultList)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }
}

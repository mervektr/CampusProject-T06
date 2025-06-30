package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Login extends BaseTest {

    @Test
    public void invalidLoginTest() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "wrong");
        body.put("password", "badpass");

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400); // Postman'da 400 bekleniyordu
    }

    @Test
    public void validLoginTest() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "turkeyts");
        body.put("password", "TS.%=2025TR");

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", equalTo("turkeyts")); // örnek, response JSON’da varsa
    }
}

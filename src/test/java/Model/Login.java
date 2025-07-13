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
                .log().all()
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void validLoginTest() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password", "TS.%=2025TR");

        given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .log().all()
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("refresh_token");
    }
}
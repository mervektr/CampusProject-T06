package Model;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import utilities.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class BaseTest {

    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        baseURI = "https://test.mersys.io";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password", "TS.%=2025TR");

        String token =
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

        System.out.println(token);

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .log(LogDetail.URI)
                .build();
    }
}
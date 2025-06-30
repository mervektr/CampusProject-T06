package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Country extends BaseTest {

    @Test
    public void addCountry() {
        Map<String, Object> state = new HashMap<>();
        state.put("name", "SampleState");
        state.put("code", "SS");

        Map<String, Object> country = new HashMap<>();
        country.put("name", "TestCountry");
        country.put("code", "TC");
        country.put("hasState", true);
        country.put("states", Arrays.asList(state));

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .body("name", equalTo("TestCountry"))
                .body("states[0].name", equalTo("SampleState"));
    }

    @Test
    public void listCountry() {
        given()
                .spec(spec)
                .when()
                .get("/school-service/api/countries")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void updateCountry() {
        Map<String, Object> country = new HashMap<>();
        country.put("id", "68611a0814d596fc94c1a506");
        country.put("name", "UpdatedCountryName");
        country.put("code", "TC");
        country.put("hasState", true);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .body("name", equalTo("UpdatedCountryName"))
                .body("id", equalTo("68611a0814d596fc94c1a506"));
    }

    @Test
    public void deleteCountry() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/countries/68611a0814d596fc94c1a506")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }
}

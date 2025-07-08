package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Country extends BaseTest {

    String countryId;

    @Test(priority = 1)
    public void addCountry() {
        Map<String, Object> state = new HashMap<>();
        state.put("name", "SampleState" + System.currentTimeMillis());
        state.put("code", "SS" + (int)(Math.random()*100));

        Map<String, Object> country = new HashMap<>();
        country.put("name", "TestCountry" + System.currentTimeMillis());
        country.put("code", "TC" + (int)(Math.random()*1000));
        country.put("hasState", true);
        country.put("states", Arrays.asList(state));

        countryId =
                given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("/school-service/api/countries")
                        .then()
                        .log().body()
                        .statusCode(anyOf(is(200), is(201))) // bazen 200 da gelebilir
                        .body("name", containsString("TestCountry"))
                        .extract().path("id");
    }

    @Test(priority = 2, dependsOnMethods = "addCountry")
    public void listCountry() {
        List<Map<String, Object>> countries =
                given()
                        .spec(spec)
                        .when()
                        .get("/school-service/api/countries")
                        .then()
                        .statusCode(200)
                        .body("size()", greaterThan(0))
                        .extract().jsonPath().getList("");

        // istersen debug için
        countries.forEach(c -> System.out.println(c.get("name")));
    }

    @Test(priority = 3, dependsOnMethods = "addCountry")
    public void updateCountry() {
        Map<String, Object> country = new HashMap<>();
        country.put("id", countryId);
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
                .body("id", equalTo(countryId));
    }

    @Test(priority = 4, dependsOnMethods = "updateCountry")
    public void deleteCountry() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/countries/" + countryId)
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }
}
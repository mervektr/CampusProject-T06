package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class State extends BaseTest {

    String stateId;

    @Test(priority = 1)
    public void addStateTest() {
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("id", "6851afe5ca9f665c0c659eec");

        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("name", "SampleState");
        stateMap.put("code", "SS");
        stateMap.put("country", countryMap);

        stateId =
                given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .body(stateMap)
                        .when()
                        .post("/school-service/api/states")
                        .then()
                        .statusCode(201)
                        .body("name", equalTo("SampleState"))
                        .body("code", equalTo("SS"))
                        .body("country.name", notNullValue())
                        .body("country.code", notNullValue())
                        .extract().path("id");
    }

    @Test(priority = 2)
    public void getAllStatesTest() {
        List<Map<String, Object>> states =
                given()
                        .spec(spec)
                        .when()
                        .get("/school-service/api/states")
                        .then()
                        .statusCode(200)
                        .time(lessThan(1000L)) // <1000 ms kontrolü
                        .extract().jsonPath().getList("");

        for (Map<String, Object> state : states) {
            assert state.containsKey("name");
            assert state.containsKey("code");
            assert state.containsKey("country");

            Map<String, Object> country = (Map<String, Object>) state.get("country");
            assert country.containsKey("name");
            assert country.containsKey("code");
        }
    }

    @Test(priority = 3, dependsOnMethods = "addStateTest")
    public void updateStateTest() {
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("id", "6851afe5ca9f665c0c659eec");
        countryMap.put("hasState", false);

        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("id", stateId);
        stateMap.put("name", "UpdatedState");
        stateMap.put("code", "US");
        stateMap.put("country", countryMap);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(stateMap)
                .when()
                .put("/school-service/api/states")
                .then()
                .statusCode(200)
                .body("name", equalTo("UpdatedState"))
                .body("code", equalTo("US"))
                .body("country.name", notNullValue());
    }

    @Test(priority = 4, dependsOnMethods = "updateStateTest")
    public void deleteStateTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/states/" + stateId)
                .then()
                .statusCode(anyOf(is(204), is(200)));
    }
}
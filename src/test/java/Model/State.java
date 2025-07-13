package Model;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class State extends BaseTest {

    private String stateId;
    Faker faker= new Faker();
    @Test(priority = 1)
    public void addStateTest() {
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("id", "6851afe5ca9f665c0c659eec");
        String nameCity=faker.address().cityName();
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("name", nameCity );
        stateMap.put("code", "SS" + (int)(Math.random() * 1000));
        stateMap.put("country", countryMap);

        stateId =
                given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .body(stateMap)
                        .log().all()
                        .when()
                        .post("/school-service/api/states")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .body("name", equalTo(nameCity))
                        .body("code", startsWith("SS"))
                        .body("country.id", equalTo("6851afe5ca9f665c0c659eec"))
                        .extract().path("id");

        System.out.println("Created State ID: " + stateId);
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
                        .time(lessThan(1500L)) // <1.5s kontrolü
                        .extract().jsonPath().getList("");

        for (Map<String, Object> state : states) {
            assert state.containsKey("name") : "State missing 'name'";
            assert state.containsKey("code") : "State missing 'code'";
            assert state.containsKey("country") : "State missing 'country'";

            Map<String, Object> country = (Map<String, Object>) state.get("country");
            assert country.containsKey("id") : "Country missing 'id'";
            assert country.containsKey("name") : "Country missing 'name'";
            assert country.containsKey("code") : "Country missing 'code'";
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
                .body("id", equalTo(stateId))
                .body("name", equalTo("UpdatedState"))
                .body("code", equalTo("US"))
                .body("country.id", equalTo("6851afe5ca9f665c0c659eec"));
    }

    @Test(priority = 4, dependsOnMethods = "updateStateTest")
    public void deleteStateTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/states/" + stateId)
                .then()
                .statusCode(anyOf(is(204), is(200)));

        System.out.println("Deleted State ID: " + stateId);
    }
}
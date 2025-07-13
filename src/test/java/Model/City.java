package Model;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class City extends BaseTest {
    String cityId;
    Faker faker= new Faker();

    @Test(priority = 1)
    public void addCityTest() {
        String cityName=faker.address().cityName();

        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("id", "68611dc114d596fc94c1a507");

        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("id", "6851afe5ca9f665c0c659eec");


        Map<String, Object> cityMap = new HashMap<>();
        cityMap.put("name", cityName);
        cityMap.put("translateName", Collections.emptyList());
        cityMap.put("state", stateMap);
        cityMap.put("country", countryMap);

        cityId =
                given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .body(cityMap)
                        .when()
                        .post("/school-service/api/cities")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("name", equalTo(cityName))
                        .extract().path("id");

        System.out.println("Created cityId: " + cityId);
    }

    @Test(priority = 2)
    public void getAllCitiesTest() {
        List<Map<String, Object>> cities =
                given()
                        .spec(spec)
                        .when()
                        .get("/school-service/api/cities")
                        .then()
                        .statusCode(200)
                        .time(lessThan(2000L)) // 2sn threshold
                        .extract().jsonPath().getList("");

        for (Map<String, Object> city : cities) {
            assert city.containsKey("name") : "City missing name";
            if (city.containsKey("state")) {
                Map<String, Object> state = (Map<String, Object>) city.get("state");
                assert state.containsKey("id") : "State missing id";
                assert state.containsKey("name") : "State missing name";
                if (state.containsKey("country")) {
                    Map<String, Object> country = (Map<String, Object>) state.get("country");
                    assert country.containsKey("id") : "Country missing id";
                }
            }
        }
    }

    @Test(priority = 3, dependsOnMethods = "addCityTest")
    public void updateCityTest() {
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("id", "68611dc114d596fc94c1a507");

        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("id", "6851afe5ca9f665c0c659eec");
        countryMap.put("hasState", false);

        Map<String, Object> cityMap = new HashMap<>();
        cityMap.put("id", cityId);
        cityMap.put("name", "UpdatedCityName");
        cityMap.put("translateName", Collections.emptyList());
        cityMap.put("state", stateMap);
        cityMap.put("country", countryMap);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(cityMap)
                .when()
                .put("/school-service/api/cities")
                .then()
                .statusCode(200)
                .body("name", equalTo("UpdatedCityName"))
                .body("id", equalTo(cityId));
    }

    @Test(priority = 4, dependsOnMethods = "updateCityTest")
    public void deleteCityTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/cities/" + cityId)
                .then()
                .statusCode(anyOf(is(204), is(200)));
    }
}
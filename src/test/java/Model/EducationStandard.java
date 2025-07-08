package Model;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ConfigReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class EducationStandard extends BaseTest {

    String educationStandardId;
    String schoolId;

    @Test(priority = 1)
    public void addEducationStandardTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("schoolId", schoolId);

given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .log().all()
                .when()
                .post("/school-service/api/education-standard/search")
                .then()
                .log().all()
                .statusCode(200)
                .log().body();

    }

    @Test(priority = 2)
    public void listEducationStandardTest() {
        String hardCodedId = "685ade3f5fe593ee903fb4dd";

 given()
                .spec(spec)
                .log().all()
                .when()
                .get("/school-service/api/education-standard/" + hardCodedId)
                .then()
                .log().all()
                .statusCode(201)
                .log().body();
    }

    @Test(priority = 3, dependsOnMethods = "listEducationStandardTest")
    public void editEducationStandardTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", educationStandardId);
        bodyMap.put("name", "Updated Math Standard");
        bodyMap.put("gradeLevelId", "657713978af7ce488ac6a632");
        bodyMap.put("subjectId", "646ccbe5acf2ee0d37c6d9a5");
        bodyMap.put("gradeCategoriesTemplateId", "646dff4eab1d8d3d700f1037");
        bodyMap.put("calculationMode", "MEAN");
        bodyMap.put("parentStandardCalculationStrategy", "TURN_OFF");
        bodyMap.put("numberOfScores", 7);
        bodyMap.put("scoreWeights", Arrays.asList(1,1,1,1,1,1,2));
        bodyMap.put("tenantId", "646cb816433c0f46e7d44cb0");

        given()
                .spec(spec)
                .body(bodyMap)

                .when()
                .put("/school-service/api/education-standard")

                .then()
                .statusCode(200)

                .log().body();
    }

    @Test(priority = 4, dependsOnMethods = "editEducationStandardTest")
    public void deleteEducationStandardTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/education-standard/" + educationStandardId)
                .then()
                .statusCode(204);
    }
}
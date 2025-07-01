package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EducationStandard extends BaseTest {

    String educationStandardId = "685ade3f5fe593ee903fb4dd";
    String schoolId = "6576fd8f8af7ce488ac69b89";

    @Test(priority = 1)
    public void addEducationStandardTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("schoolId", schoolId);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/school-service/api/education-standard/search")
                .then()
                .statusCode(200);
    }

    @Test(priority = 2)
    public void listEducationStandardTest() {
        given()
                .spec(spec)
                .when()
                .get("/school-service/api/education-standard/" + educationStandardId)
                .then()
                .statusCode(200)
                .body("id", equalTo(educationStandardId));
    }

    @Test(priority = 3)
    public void editEducationStandardTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", educationStandardId);
        bodyMap.put("name", "Updated Math Standard");
        bodyMap.put("gradeLevelId", "657713978af7ce488ac6a632");
        bodyMap.put("subjectId", "646ccbe5acf2ee0d37c6d9a5");
        bodyMap.put("gradeCategoriesTemplateId", "646dff4eab1d8d3d700f1037");
        bodyMap.put("gradeCategoryId", "e38e-911f");
        bodyMap.put("calculationMode", "MEAN");
        bodyMap.put("parentStandardCalculationStarategy", "TURN_OFF");
        bodyMap.put("numberOfScores", 7);
        bodyMap.put("scoreWeights", Arrays.asList(1,1,1,1,1,1,2));
        bodyMap.put("tenantId", "646cb816433c0f46e7d44cb0");

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .put("/school-service/api/education-standard")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Math Standard"));
    }

    @Test(priority = 4)
    public void deleteEducationStandardTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/education-standard/" + educationStandardId)
                .then()
                .statusCode(204);
    }
}

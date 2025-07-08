package Model;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import utilities.ConfigReader;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class StudentGroup extends BaseTest {

    @Test
    public void createEducationStandard() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", null);
        Faker faker = new Faker();
        body.put("name", faker.educator().course());
        body.put("description", faker.lorem().sentence());
        body.put("schoolId", ConfigReader.getProperty("schoolID"));
        body.put("gradeLevelId", ConfigReader.getProperty("gradeLevelId"));
        body.put("subjectId", ConfigReader.getProperty("subjectId"));
        body.put("gradeCategoriesTemplateId", ConfigReader.getProperty("gradeCategoriesTemplateId"));
        body.put("gradeCategoryId", ConfigReader.getProperty("gradeCategoryId"));
        body.put("calculationMode", "MEAN");
        body.put("parentStandardCalculationStrategy", "TURN_OFF");
        body.put("numberOfScores", 7);
        body.put("scoreWeights", Arrays.asList(1, 1, 1, 1, 1, 1, 2));
        body.put("tenantId", ConfigReader.getProperty("tenantId"));

        String educationStandardID =
                given()
                        .spec(spec)
                        .body(body)
                        .when()
                        .post("/school-service/api/education-standard")
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id");

        ConfigReader.updateProperty("educationStandardID", educationStandardID);
        System.out.println("Created EducationStandard ID: " + educationStandardID);
    }

    @Test(dependsOnMethods = "createEducationStandard")
    public void updateEducationStandard() {
        Map<String, Object> body = new LinkedHashMap<>();
        Faker faker = new Faker();
        body.put("id", ConfigReader.getProperty("educationStandardID"));
        body.put("name", "Updated " + faker.educator().course());
        body.put("description", faker.lorem().paragraph());
        body.put("schoolId", ConfigReader.getProperty("schoolID"));
        body.put("gradeLevelId", ConfigReader.getProperty("gradeLevelId"));
        body.put("subjectId", ConfigReader.getProperty("subjectId"));
        body.put("gradeCategoriesTemplateId", ConfigReader.getProperty("gradeCategoriesTemplateId"));
        body.put("gradeCategoryId", ConfigReader.getProperty("gradeCategoryId"));
        body.put("calculationMode", "MEAN");
        body.put("parentStandardCalculationStrategy", "TURN_OFF");
        body.put("numberOfScores", 7);
        body.put("scoreWeights", Arrays.asList(1, 1, 1, 1, 1, 1, 2));
        body.put("tenantId", ConfigReader.getProperty("tenantId"));

        given()
                .spec(spec)
                .body(body)
                .when()
                .put("/school-service/api/education-standard")
                .then()
                .statusCode(200)
                .log().body();
    }

    @Test(dependsOnMethods = "updateEducationStandard")
    public void getEducationStandardDetail() {
        String id = ConfigReader.getProperty("educationStandardID");
        Response response =
                given()
                        .spec(spec)
                        .log().all()
                        .when()
                        .get("/school-service/api/education-standard/" + id)
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().response();

        JsonPath json = response.jsonPath();
        String returnedName = json.getString("name");
        System.out.println("GET EducationStandard returned name: " + returnedName);
    }

    @Test(dependsOnMethods = "getEducationStandardDetail")
    public void deleteEducationStandard() {
        String id = ConfigReader.getProperty("educationStandardID");
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/education-standard/" + id)
                .then()
                .statusCode(204);
    }
}
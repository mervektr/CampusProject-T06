package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GradingScheme extends BaseTest {

    String gradingSchemeId = "686159ae14d596fc94c1a518";
    String schoolId = "6576fd8f8af7ce488ac69b89";
    String tenantId = "6576fd8f8af7ce488ac69b89";

    @Test(priority = 1)
    public void addGradingScheme() {
        given()
                .spec(spec)
                .when()
                .get("/school-service/api/grading-schemes/school/" + schoolId)
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("[0]", hasKey("id"))
                .body("[0]", hasKey("name"))
                .body("[0]", hasKey("tenantId"))
                .body("[0]", hasKey("type"));
    }

    @Test(priority = 2)
    public void listGradingScheme() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("schoolId", schoolId);
        bodyMap.put("tenantId", tenantId);
        bodyMap.put("name", "Yeni Grading Scheme");
        bodyMap.put("type", "POINT");

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/school-service/api/grading-schemes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("schoolId", equalTo(schoolId))
                .body("type", equalTo("POINT"));
    }

    @Test(priority = 3)
    public void editGradingScheme() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", gradingSchemeId);
        bodyMap.put("name", "Güncellenmiş Grading Scheme");
        bodyMap.put("active", true);
        bodyMap.put("tenantId", tenantId);
        bodyMap.put("gradeRanges", new String[]{});
        bodyMap.put("type", "POINT");
        bodyMap.put("enablePoint", true);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .put("/school-service/api/grading-schemes")
                .then()
                .statusCode(200)
                .body("id", equalTo(gradingSchemeId))
                .body("name", equalTo("Güncellenmiş Grading Scheme"))
                .body("tenantId", equalTo(tenantId))
                .body("type", equalTo("POINT"))
                .body("active", equalTo(true))
                .body("enablePoint", equalTo(true));
    }

    @Test(priority = 4)
    public void deleteGradingScheme() {
        given()
                .spec(spec)
                .queryParam("schoolId", schoolId)
                .when()
                .delete("/school-service/api/grading-schemes/" + gradingSchemeId)
                .then()
                .statusCode(anyOf(is(200), is(204)))
                .body(equalTo(""));
    }
}

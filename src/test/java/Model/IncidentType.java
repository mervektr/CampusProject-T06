package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IncidentType extends BaseTest{

    String tenantId = "646cb816433c0f46e7d44cb0";
    String schoolId = "6576fd8f8af7ce488ac69b89";
    String incidentTypeId;

    @Test(priority = 1)
    public void listIncidentTypes() {
        Map<String, Object> body = new HashMap<>();
        body.put("schoolId", schoolId);
        body.put("tenantId", tenantId);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/school-service/api/incident-type/search")
                .then()
                .statusCode(200)
                .body("$", is(not(empty())))
                .body("$", instanceOf(java.util.List.class));
    }

    @Test(priority = 2)
    public void addIncidentType() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Yeni Olay Tipi");
        body.put("minPoint", 1);
        body.put("maxPoint", 10);
        body.put("tenantId", tenantId);

        incidentTypeId = given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/school-service/api/incident-type")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");

        System.out.println("Created Incident Type ID: " + incidentTypeId);
    }

    @Test(priority = 3, dependsOnMethods = "addIncidentType")
    public void editIncidentType() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", incidentTypeId);
        body.put("name", "Güncellenmiş Olay Tipi");
        body.put("active", true);
        body.put("tenantId", tenantId);
        body.put("minPoint", 2);
        body.put("maxPoint", 15);
        body.put("academicBased", false);
        body.put("enabled", true);
        body.put("notifyWithMessage", true);
        body.put("notifyWithEmail", false);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/school-service/api/incident-type")
                .then()
                .statusCode(200)
                .body("name", equalTo("Güncellenmiş Olay Tipi"))
                .body("minPoint", equalTo(2))
                .body("maxPoint", equalTo(15));
    }

    @Test(priority = 4, dependsOnMethods = "editIncidentType")
    public void deleteIncidentType() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/incident-type/" + incidentTypeId)
                .then()
                .statusCode(anyOf(is(200), is(204)))
                .body(anyOf(equalTo(""), nullValue()));

        System.out.println("Deleted Incident Type ID: " + incidentTypeId);
    }
}

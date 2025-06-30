package Model;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StudentGroup extends BaseTest{

    String groupId;

    @Test(priority = 1)
    public void addStudentGroupTest() {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("schoolId", "6576fd8f8af7ce488ac69b89");
        groupMap.put("name", "Test Group For CRUD");
        groupMap.put("description", "Bu grup Postman üzerinden CRUD testleri için oluşturuldu.");
        groupMap.put("active", true);
        groupMap.put("public", false);
        groupMap.put("visibilityToStudent", true);

        groupId =
                given()
                        .spec(spec)
                        .contentType(ContentType.JSON)
                        .body(groupMap)
                        .when()
                        .post("/school-service/api/student-group")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .extract().path("id");

        System.out.println("Created Student Group ID: " + groupId);
    }

    @Test(priority = 2, dependsOnMethods = "addStudentGroupTest")
    public void listStudentGroupTest() {
        given()
                .spec(spec)
                .when()
                .get("/school-service/api/student-group/" + groupId)
                .then()
                .statusCode(200)
                .body("id", equalTo(groupId))
                .body("name", equalTo("Test Group For CRUD"))
                .body("schoolId", equalTo("6576fd8f8af7ce488ac69b89"));
    }

    @Test(priority = 3, dependsOnMethods = "listStudentGroupTest")
    public void updateStudentGroupTest() {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("id", groupId);
        groupMap.put("schoolId", "6576fd8f8af7ce488ac69b89");
        groupMap.put("name", "Updated Group Name");
        groupMap.put("description", "Bu grup Postman CRUD testi için güncellendi.");
        groupMap.put("active", false);
        groupMap.put("publicGroup", true);
        groupMap.put("showToStudent", true);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(groupMap)
                .when()
                .put("/school-service/api/student-group")
                .then()
                .statusCode(200)
                .body("id", equalTo(groupId))
                .body("name", equalTo("Updated Group Name"))
                .body("description", equalTo("Bu grup Postman CRUD testi için güncellendi."))
                .body("active", equalTo(false))
                .body("publicGroup", equalTo(true))
                .body("showToStudent", equalTo(true));
    }

    @Test(priority = 4, dependsOnMethods = "updateStudentGroupTest")
    public void listStudentsInSchoolTest() {
        List<Map<String, Object>> groups =
                given()
                        .spec(spec)
                        .when()
                        .get("/school-service/api/student-group/school/6576fd8f8af7ce488ac69b89?page=0&size=50")
                        .then()
                        .statusCode(200)
                        .time(lessThan(1000L))
                        .extract().jsonPath().getList("content");

        assert groups.size() > 0;
    }

    @Test(priority = 5, dependsOnMethods = "listStudentsInSchoolTest")
    public void addStudentToGroupTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("studentIds", List.of("657711ca8af7ce488ac6a628"));

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/school-service/api/student-groups/" + groupId + "/add-students")
                .then()
                .statusCode(200);
    }

    @Test(priority = 6, dependsOnMethods = "addStudentToGroupTest")
    public void displayStudentGroupTest() {
        List<Map<String, Object>> students =
                given()
                        .spec(spec)
                        .when()
                        .get("/school-service/api/students/group/" + groupId + "?page=0&size=10")
                        .then()
                        .statusCode(200)
                        .time(lessThan(1000L))
                        .extract().jsonPath().getList("content");

        assert students instanceof List;
    }

    @Test(priority = 7, dependsOnMethods = "displayStudentGroupTest")
    public void removeStudentFromGroupTest() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("studentIds", List.of("657711c9a18cee0c25254256"));

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/school-service/api/student-group/" + groupId + "/removeStudent")
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }

    @Test(priority = 8, dependsOnMethods = "removeStudentFromGroupTest")
    public void deleteStudentGroupTest() {
        given()
                .spec(spec)
                .when()
                .delete("/school-service/api/student-group/" + groupId)
                .then()
                .statusCode(anyOf(is(204), is(200)));

        System.out.println("Deleted Student Group ID: " + groupId);
    }
}
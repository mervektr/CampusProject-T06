package Model;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import utilities.ConfigReader;

import static io.restassured.RestAssured.*;

public class BaseTest {

    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        baseURI = ConfigReader.getProperty("baseUrl");

        spec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + ConfigReader.getProperty("token"))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("schoolId", ConfigReader.getProperty("schoolID"))
                .addHeader("tenantId", ConfigReader.getProperty("tenantId"))
                .build();
    }
}
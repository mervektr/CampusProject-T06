package Model;

import Utilities.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    public static RequestSpecification spec;

    static {
        spec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getProperty("BaseTest"))
                .addHeader("Authorization", "Bearer " + ConfigReader.getProperty("token"))
                .build();
    }
}

package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class BaseClient {

    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BaseUrl.BASE_URL)
                .setContentType(JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}

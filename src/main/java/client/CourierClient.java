package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера по id: {id}")
    public Response delete(int id) {
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_PATH + "/" + id);
    }
}

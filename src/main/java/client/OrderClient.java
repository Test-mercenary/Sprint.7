package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(buildOrderBody(order))
                .post(ORDERS_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .spec(getBaseSpec())
                .get(ORDERS_PATH);
    }

    @Step("Подготовка тела заказа")
    private Map<String, Object> buildOrderBody(Order order) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("firstName", order.getFirstName());
        body.put("lastName", order.getLastName());
        body.put("address", order.getAddress());
        body.put("metroStation", order.getMetroStation());
        body.put("phone", order.getPhone());
        body.put("rentTime", order.getRentTime());
        body.put("deliveryDate", order.getDeliveryDate());
        body.put("comment", order.getComment());
        if (order.getColor() != null) {
            body.put("color", order.getColor());
        }
        return body;
    }
}

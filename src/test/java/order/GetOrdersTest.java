package order;

import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Можно получить список заказов")
    public void getOrdersTest() {
        orderClient.getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
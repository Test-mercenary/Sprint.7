package order;

import client.OrderClient;
import client.OrderGenerator;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final OrderClient orderClient = new OrderClient();
    private final List<String> colors;
    private final String testName;

    public CreateOrderTest(List<String> colors, String testName) {
        this.colors = colors;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> getOrderColors() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK"), "Можно создать заказ с цветом BLACK"},
                {List.of("GREY"), "Можно создать заказ с цветом GREY"},
                {List.of("BLACK", "GREY"), "Можно создать заказ с двумя цветами"},
                {null, "Можно создать заказ без указания цвета"}
        });
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrderTest() {
        Order order = OrderGenerator.getRandomOrder(colors);

        orderClient.create(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
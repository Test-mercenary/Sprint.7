package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import client.CourierGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;

public class LoginCourierTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        createCourierForLoginTests();
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Step("Создать курьера для тестов логина")
    private void createCourierForLoginTests() {
        courierClient.create(courier);
    }

    @Step("Получить id курьера")
    private int getCourierId() {
        Response response = courierClient.login(CourierCredentials.from(courier));
        return response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Успешная авторизация возвращает 200 и id курьера")
    public void courierCanLogin() {
        courierClient.login(CourierCredentials.from(courier))
                .then()
                .statusCode(200)
                .body("id", greaterThan(0));
    }
}
package courier;

import client.CourierClient;
import client.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Step("Получить id созданного курьера")
    private int getCreatedCourierId() {
        Response loginResponse = courierClient.login(CourierCredentials.from(courier));
        return loginResponse.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Успешное создание курьера возвращает 201 и ok: true")
    public void courierCanBeCreated() {
        courierClient.create(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = getCreatedCourierId();
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("При попытке создать курьера с уже существующим логином возвращается 409 и сообщение об ошибке")
    public void cannotCreateTwoIdenticalCouriers() {
        courierClient.create(courier);
        courierId = getCreatedCourierId();

        courierClient.create(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Если не передать логин, возвращается 400 и сообщение об ошибке")
    public void cannotCreateCourierWithoutLogin() {
        Courier noLogin = new Courier(null, courier.getPassword(), courier.getFirstName());
        courierClient.create(noLogin)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Если не передать пароль, возвращается 400 и сообщение об ошибке")
    public void cannotCreateCourierWithoutPassword() {
        Courier noPassword = new Courier(courier.getLogin(), null, courier.getFirstName());
        courierClient.create(noPassword)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

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
import static org.hamcrest.Matchers.greaterThan;

public class LoginCourierTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        createCourierForLoginTests();
        courierId = getCourierId();
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

    @Test
    @DisplayName("Авторизация без логина возвращает ошибку")
    @Description("Если не передать логин, возвращается 400 и сообщение об ошибке")
    public void loginWithoutLoginFieldReturnsError() {
        CourierCredentials noLogin = new CourierCredentials(null, courier.getPassword());

        courierClient.login(noLogin)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с пустым паролем возвращает ошибку")
    @Description("Если передать пустой пароль, возвращается ошибка")
    public void loginWithEmptyPasswordReturnsError() {
        CourierCredentials emptyPassword = new CourierCredentials(courier.getLogin(), "");

        courierClient.login(emptyPassword)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином возвращает ошибку")
    @Description("Если передать неверный логин, возвращается 404 и сообщение об ошибке")
    public void loginWithWrongLoginReturnsError() {
        CourierCredentials wrongLogin = new CourierCredentials("wrong_" + courier.getLogin(), courier.getPassword());

        courierClient.login(wrongLogin)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем возвращает ошибку")
    @Description("Если передать неверный пароль, возвращается 404 и сообщение об ошибке")
    public void loginWithWrongPasswordReturnsError() {
        CourierCredentials wrongPassword = new CourierCredentials(courier.getLogin(), "wrong_" + courier.getPassword());

        courierClient.login(wrongPassword)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера возвращает ошибку")
    @Description("Если авторизоваться несуществующим пользователем, возвращается 404 и сообщение об ошибке")
    public void loginWithNonExistentCourierReturnsError() {
        CourierCredentials nonExistentCourier = new CourierCredentials("not_exists_login", "not_exists_password");

        courierClient.login(nonExistentCourier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
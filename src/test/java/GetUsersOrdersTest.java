import Api.Path;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static Api.Path.*;
import static Api.RandomUserData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class GetUsersOrdersTest {
    private UserData user;
    private UserAuthData userAuthData;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = Path.BASE_URI;
    }

    @Test
    @DisplayName("Получить список заказов авторизованного пользователя")
    public void getUserOrdersWithTokenTest() {
        createUser();
        getUserOrdersWithToken()
                .then().statusCode(200)
                .and().assertThat().body("success", equalTo(true), "$", hasKey("orders"));
        deleteUser();
    }

    @Test
    @DisplayName("Получить список заказов неавторизованного пользователя")
    public void getUserOrdersWithoutTokenTest() {
        getUserOrdersWithoutToken()
                .then().statusCode(401)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @Step("Создать пользователя и сохранить тело ответа в класс UserAuthData")
    public void createUser() {
        user = new UserData(USER_EMAIL, USER_PASSWORD, USER_NAME);
        response = given().header("Content-type", "application/json").body(user).post(USER_REGISTRATION_PATH);
        userAuthData = response.body().as(UserAuthData.class);
    }

    @Step("Удалить пользователя")
    public void deleteUser() {
        if (getAccessToken() != null) {
            given().header("Authorization", getAccessToken()).delete(USER_DATA_PATH);
        }
    }

    @Step("Получить список заказов пользователя, передав токен авторизации")
    public Response getUserOrdersWithToken() {
        response = given().headers("Authorization", getAccessToken()).get(USER_ORDER_PATH);
        return response;
    }

    @Step("Получить список заказов пользователя, без передачи токена авторизации")
    public Response getUserOrdersWithoutToken() {
        response = given().get(USER_ORDER_PATH);
        return response;
    }

    @Step("Получить токен авторизации")
    public String getAccessToken() {
        return userAuthData.getAccessToken();
    }
}
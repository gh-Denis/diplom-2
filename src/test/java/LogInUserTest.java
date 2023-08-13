import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static Api.Path.*;
import static Api.RandomUserData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class LogInUserTest {
    private UserData user;
    private UserAuthData userAuthData;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        user = new UserData(USER_EMAIL, USER_PASSWORD, USER_NAME);
        createUser();
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void existingUserAuthorisationTest() {
        userAuthorization().then().statusCode(200)
                .and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    public void authorisationWithWrongLoginTest() {
        user = new UserData("email", USER_PASSWORD, USER_NAME);
        userAuthorization().then().statusCode(401)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    public void authorisationWithWrongPasswordTest() {
        user = new UserData(USER_EMAIL, "password", USER_NAME);
        userAuthorization().then().statusCode(401)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Удалить пользователя")
    public void deleteUser() {
        if (getAccessToken() != null) {
            given().header("Authorization", getAccessToken()).delete(USER_DATA_PATH);
        }
    }

    @Step("Создать пользователя и сохранить тело ответа в класс UserAuthData")
    public void createUser() {
        response = given().header("Content-type", "application/json").body(user).post(USER_REGISTRATION_PATH);
        userAuthData = response.body().as(UserAuthData.class);
    }

    @Step("Авторизация пользователя")
    public Response userAuthorization() {
        response = given().header("Content-type", "application/json").body(user).post(USER_AUTHORISATION_PATH);
        return response;
    }

    @Step("Получить токен авторизации")
    public String getAccessToken() {
        return userAuthData.getAccessToken();
    }
}
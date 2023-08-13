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

public class CreateUserTest {

    private UserData user;
    private UserAuthData userAuthData;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        user = new UserData(USER_EMAIL, USER_PASSWORD, USER_NAME);
    }

    @Test
    @DisplayName("Проверить создание пользователя")
    public void createUserTest() {
        createUser().then().statusCode(200)
                .and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверить создание существующего пользователя")
    public void createExistingUserTest() {
        createUser();
        given().header("Content-type", "application/json").body(user).post(USER_REGISTRATION_PATH).then().statusCode(403)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверить создание пользователя без заполненного поля Имя")
    public void createUserWithEmptyName() {
        user = new UserData(USER_EMAIL, USER_PASSWORD, "");
        createUser().then().statusCode(403)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверить создание пользователя без заполненного поля email")
    public void createUserWithEmptyEmail() {
        user = new UserData("", USER_PASSWORD, USER_NAME);
        createUser().then().statusCode(403)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверить создание пользователя без заполненного поля Пароль")
    public void createUserWithEmptyPassword() {
        user = new UserData(USER_EMAIL, "", USER_NAME);
        createUser().then().statusCode(403)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @After
    @DisplayName("Удалить пользователя")
    public void deleteUser() {
        if (getAccessToken() != null) {
            given().header("Authorization", getAccessToken()).delete(USER_DATA_PATH);
        }
    }

    @Step("Создать пользователя и сохранить тело ответа в класс UserAuthData")
    public Response createUser() {
        response = given().header("Content-type", "application/json").body(user).post(USER_REGISTRATION_PATH);
        userAuthData = response.body().as(UserAuthData.class);
        return response;
    }

    @Step("Получить токен авторизации")
    public String getAccessToken() {
        return userAuthData.getAccessToken();
    }
}
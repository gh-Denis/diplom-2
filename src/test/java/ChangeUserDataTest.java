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

public class ChangeUserDataTest {
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
    @DisplayName("Изменить имя пользователя")
    public void changeUserNameTest() {
        user.setName("NewName");
        changeUserData().then().statusCode(200).and().body("user.name", equalTo("NewName"));
    }

    @Test
    @DisplayName("Изменить почту пользователя")
    public void changeUserEmailTest() {
        user.setEmail("newmail@ya.ru");
        changeUserData().then().statusCode(200).and().body("user.email", equalTo("newmail@ya.ru"));
    }

    @Test
    @DisplayName("Изменить почту пользователя на адрес, который уже используется")
    public void changeUserEmailToExistingEmailTest() {
        String createdUserToken = userAuthData.getAccessToken();
        user.setEmail("existingemail@ya.ru");
        createUser();
        given().headers("Authorization", createdUserToken, "Content-type", "application/json").body(user).patch(USER_DATA_PATH)
                .then().statusCode(403)
                .and().body("success", equalTo(false), "message", equalTo("User with such email already exists"));
        given().header("Authorization", createdUserToken).delete(USER_DATA_PATH);
    }

    @Test
    @DisplayName("Изменить имя пользователя, не отправляя токен авторизации")
    public void changeUserNameTestWithoutTokenTest() {
        user.setName("NewName");
        changeUserDataWithoutToken()
                .then().statusCode(401)
                .and().body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменить почту пользователя, не отправляя токен авторизации")
    public void changeUserEmailTestWithoutTokenTest() {
        user.setEmail("newmail@ya.ru");
        changeUserDataWithoutToken()
                .then().statusCode(401)
                .and().body("success", equalTo(false), "message", equalTo("You should be authorised"));
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

    @Step("Отправить PATCH запрос с токеном авторизации на изменение данных пользователя")
    public Response changeUserData() {
        response = given().headers("Authorization", getAccessToken(), "Content-type", "application/json").body(user).patch(USER_DATA_PATH);
        return response;
    }

    @Step("Отправить PATCH запрос БЕЗ токена авторизации на изменение данных пользователя")
    public Response changeUserDataWithoutToken() {
        response = given().headers("Content-type", "application/json").body(user).patch(USER_DATA_PATH);
        return response;
    }

    @Step("Получить токен авторизации")
    public String getAccessToken() {
        return userAuthData.getAccessToken();
    }
}
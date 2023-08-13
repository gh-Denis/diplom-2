import Api.Path;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static Api.Path.USER_DATA_PATH;
import static Api.Path.USER_ORDER_PATH;
import static Api.RandomUserData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateAnOrderTest {
    private final String ingredient1 = "60d3b41abdacab0026a733c6";
    private final String ingredient2 = "609646e4dc916e00276b2870";
    private final ArrayList<String> ingredients = new ArrayList<>();
    private Ingredients ingredient;
    private Response response;
    private UserAuthData userAuthData;
    private UserData user;

    @Before
    public void setUp() {
        RestAssured.baseURI = Path.BASE_URI;
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredient = new Ingredients(ingredients);
    }

    @Test
    @DisplayName("Создать заказ без токена авторизации")
    public void createOrderWithoutTokenTest() {
        createOrderWithoutToken()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Создать заказ с токеном авторизации")
    public void createOrderWithTokenTest() {
        createUser();
        createOrderWithToken()
                .then().statusCode(200)
                .and().assertThat().body("success", is(true));
        deleteUser();
    }

    @Test
    @DisplayName("Создать заказ с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ingredients.clear();
        createOrderWithoutToken()
                .then().statusCode(400)
                .and().assertThat().body("success", equalTo(false), "message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создать заказ с невалидным списком интегредиентов")
    public void createOrderWithInvalidIngredientsTest() {
        ingredients.set(0, "");
        createOrderWithoutToken().then().statusCode(500);
    }

    @Step("Отправить запрос на создание заказа без авторизации")
    public Response createOrderWithoutToken() {
        response = given().headers("Content-type", "application/json").body(ingredient).post(USER_ORDER_PATH);
        return response;
    }

    @Step("Отправить запрос на создание заказа с токеном авторизации")
    public Response createOrderWithToken() {
        given()
                .headers("Content-type", "application/json")
                .auth().oauth2(getAccessToken())
                .body(ingredient)
                .post(USER_ORDER_PATH);
        return response;
    }

    @Step("Создать пользователя и сохранить тело ответа в класс UserAuthData")
    public void createUser() {
        user = new UserData(USER_EMAIL, USER_PASSWORD, USER_NAME);
        response = given().header("Content-type", "application/json").body(user).post(Path.USER_REGISTRATION_PATH);
        userAuthData = response.body().as(UserAuthData.class);
    }

    @Step("Получить токен авторизации")
    public String getAccessToken() {
        return userAuthData.getAccessToken();
    }

    @Step("Удалить пользователя")
    public void deleteUser() {
        if (getAccessToken() != null) {
            given().header("Authorization", getAccessToken()).delete(USER_DATA_PATH);
        }
    }
}
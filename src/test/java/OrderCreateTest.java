import api.Path;
import ingredient.Ingredient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.OrderData;
import order.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserData;
import user.UserRandomData;
import user.UserSteps;

import static ingredient.IngredientRequest.getIngredientFromArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private String accessToken;
    private Response response;
    private Ingredient validIngredient;

    @Before
    public void setUp() {
        RestAssured.baseURI = Path.BASE_URL;
        validIngredient = getIngredientFromArray();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    public void createOrderWithUserLoginAndCorrectIngHashShouldReturnOk() {
        UserData user = UserRandomData.createNewRandomUser();
        OrderData order = new OrderData(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createOrderWithoutUserLoginAndEmptyIngHashShouldReturnOk() {
        OrderData order = new OrderData(validIngredient);
        response = orderSteps.createOrderWithToken(order, "");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createOrderWithoutUserLoginAndWithoutIngHashShouldReturnError() {
        UserData user = UserRandomData.createNewRandomUser();
        OrderData order = new OrderData();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutToken(order);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);
    }

    @Test
    public void createOrderWithoutIngredientsShouldReturnError() {
        UserData user = UserRandomData.createNewRandomUser();
        OrderData order = new OrderData();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutToken(order);
        response.then()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    public void createOrderWithWrongIngredientHashReturnError() {
        validIngredient.set_id("MutantIngredientTokenWussHere.");
        OrderData order = new OrderData(validIngredient);
        UserData user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response.then().
                statusCode(500);
    }

    @Test
    public void getStatusOfIngredients() {
        response = orderSteps.getIngredients();
        response.then()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue())
                .and()
                .statusCode(200);
    }
}
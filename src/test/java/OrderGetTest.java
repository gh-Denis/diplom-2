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
import static user.UserRandomData.faker;

public class OrderGetTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private String accessToken;
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
    public void getOrdersWithAuthorizedUserShouldReturnOk() {
        UserData user = UserRandomData.createNewRandomUser();
        OrderData order = new OrderData(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithToken(order, accessToken);
        response = orderSteps.getUserOrders(accessToken);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getOrdersWithUnauthorizedUserShouldReturnError() {
        OrderData order = new OrderData(validIngredient);
        response = orderSteps.createOrderWithToken(order, String.valueOf(faker.random().hashCode()));
        response = orderSteps.getUserOrders(String.valueOf(faker.random().hashCode()));
        response.then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
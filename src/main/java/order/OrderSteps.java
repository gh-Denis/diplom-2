package order;

import api.Path;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("createOrderWithToken")
    public Response createOrderWithToken(OrderData order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(Path.BASE_URL)
                .body(order)
                .post(Path.ORDERS);
    }

    @Step("createOrderWithoutToken")
    public Response createOrderWithoutToken(OrderData order) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Path.BASE_URL)
                .body(order)
                .post(Path.ORDERS);
    }


    @Step("getUserOrders")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(Path.BASE_URL)
                .get(Path.ORDERS);
    }

    @Step("getIngredients")
    public Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Path.BASE_URL)
                .get(Path.INGREDIENTS);
    }
}
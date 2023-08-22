package ingredient;

import api.Path;

import static io.restassured.RestAssured.given;

public class IngredientRequest {

    public static Ingredient[] getIngredientsArray() {
        return getIngredientResponse().getIngredients();
    }

    public static IngredientResponse getIngredientResponse() {
        return given()
                .get(Path.INGREDIENTS)
                .as(IngredientResponse.class);
    }

    public static Ingredient getIngredientFromArray() {
        return getIngredientsArray()[0];
    }
}
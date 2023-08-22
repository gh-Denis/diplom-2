package user;

import api.Path;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(Path.BASE_URL);
    }

    @Step("userCreate")
    public Response userCreate(UserData user) {
        return requestSpec()
                .body(user)
                .post(Path.REGISTER);
    }

    @Step("userDelete")
    public void userDelete(String token) {
        requestSpec()
                .header("Authorization", token)
                .delete(Path.USER);
    }

    @Step("userProfileChanging")
    public Response userProfileChanging(UserData newUser, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(newUser)
                .patch(Path.USER);
    }

    @Step("userLogin")
    public Response userLogin(UserData user) {
        return requestSpec()
                .body(user)
                .post(Path.LOGIN);
    }

    @Step("userLoginToken")
    public Response userLoginToken(UserData user, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(user)
                .post(Path.LOGIN);
    }
}
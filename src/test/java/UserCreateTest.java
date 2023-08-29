import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import user.UserData;
import user.UserRandomData;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private UserData user;
    private String accessToken;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    public void createUserTestMustBeSuccessful() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void createSameUserTestMustReturnError() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        response = userSteps.userCreate(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    public void createNoDataUserTestMustReturnError() {
        user = UserRandomData.createRandomNoDataUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    public void createNoNameUserTestMustReturnError() {
        user = UserRandomData.createRandomNoNameUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    public void createNoEmailUserTestMustReturnError() {
        user = UserRandomData.createRandomNoEmailUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    public void createNoPasswordUserTestMustReturnError() {
        user = UserRandomData.createRandomNoPasswordUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
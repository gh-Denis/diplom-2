import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserData;
import user.UserRandomData;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static user.UserRandomData.faker;

public class UserDataChangingTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private UserData user;
    private String accessToken;
    private String name;
    private String email;
    private String password;

    @Before
    public void setUp() {
        user = UserRandomData.createNewRandomUser();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }

    @Test
    public void userDataChangingTestWithAuthorizationShouldUpdateUserData() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, accessToken);
        user.setName(name);
        user.setEmail(email);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void userPasswordChangingTestWithAuthorizationShouldUpdatePassword() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, accessToken);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void userPasswordChangingTestWithoutAuthorizationShouldReturnError() {
        user.setPassword(faker.internet().password());
        response = userSteps.userProfileChanging(user, "");
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void userDataChangingTestWithoutAuthorizationShouldReturnError() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userProfileChanging(user, "");
        user.setName(name);
        user.setEmail(email);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
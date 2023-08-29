import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserData;
import user.UserRandomData;
import user.UserSteps;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    private final UserSteps userSteps = new UserSteps();
    private Response response;
    private UserData user;
    private String accessToken;

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
    public void loginUserMustBeSuccessful() {
        response = userSteps.userLoginToken(user, accessToken);
        response
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void loginUserWithWrongPasswordAndEmailMustReturnError() {
        String email = user.getEmail();
        user.setEmail("test@email.ru");
        String password = user.getPassword();
        user.setPassword("testPassword");
        response = userSteps.userLoginToken(user, accessToken);
        user.setEmail(email);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
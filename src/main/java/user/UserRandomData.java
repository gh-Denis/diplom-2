package user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserRandomData {

    public static Faker faker = new Faker();

    @Step("createNewRandomUser")
    public static UserData createNewRandomUser() {
        return new UserData(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());
    }

    @Step("createRandomNoNameUser")
    public static UserData createRandomNoNameUser() {
        return new UserData(
                "",
                faker.internet().emailAddress(),
                faker.internet().password());
    }

    @Step("createRandomNoEmailUser")
    public static UserData createRandomNoEmailUser() {
        return new UserData(
                faker.name().firstName(),
                "",
                faker.internet().password());
    }

    @Step("createRandomNoPasswordUser")
    public static UserData createRandomNoPasswordUser() {
        return new UserData(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                "");
    }

    @Step("createRandomNoDataUser")
    public static UserData createRandomNoDataUser() {
        return new UserData(
                "",
                "",
                "");
    }
}
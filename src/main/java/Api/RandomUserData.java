package Api;

import com.github.javafaker.Faker;

public class RandomUserData {
    private static final Faker faker = new Faker();
    public static final String USER_EMAIL = faker.internet().emailAddress();
    public static final String USER_PASSWORD = faker.internet().password();
    public static final String USER_NAME = faker.name().username();
}
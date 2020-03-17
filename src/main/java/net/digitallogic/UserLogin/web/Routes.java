package net.digitallogic.UserLogin.web;

public class Routes {
    public static final String BASE = "/api";
    public static final String USERS = BASE + "/users";
    public static final String SIGN_UP_URL = USERS;
    public static final String LOGIN = "/login";
    public static final String ADRESSES = USERS + "/{userId}/addresses";
}

package net.digitallogic.UserLogin.security;

public class SecurityConstants {        //      ms     s    m   h   day
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String TOKEN_SECRET = "secret_token";
    public static final String H2_CONSOLE = "/h2-console/**";
}

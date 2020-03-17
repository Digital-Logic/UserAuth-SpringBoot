package net.digitallogic.UserLogin.security;

// Authorities are static, sense the system statically checks against authorities at runtime

public enum Authority {

    // User Authorities
    ADMIN_USER("ADMIN_USER_AUTHORITY"),
    READ_USER("READ_USER_AUTHORITY"),
    DELETE_USER("DELETE_USER_AUTHORITY"),
    UPDATE_USER("UPDATE_USER_AUTHORITY"),

    // Role Authorities -- create/read/update roles
    ADMIN_ROLES("ADMIN_ROLES_AUTHORITY"),
    READ_ROLES("READ_ROLES_AUTHORITY");

    public final String auth;

    private Authority(String auth) {
        this.auth = auth;
    }
}

package antifraud.Auth;

public enum Role {

    MERCHANT,
    ADMINISTRATOR,
    SUPPORT;

    public final String roleString;

    Role() {
        this.roleString = "ROLE_" + this.name();
    }

}

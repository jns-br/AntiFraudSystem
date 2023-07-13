package antifraud.Model;

public interface ApiRoutes {

    String AUTH_BASE = "/api/auth";

    String USER = AUTH_BASE + "/user";

    String USERNAME = USER + "/{username}";

    String USERS = USER + "/**";

    String USER_LIST = AUTH_BASE + "/list";

    String USER_ACCESS = AUTH_BASE + "/access";

    String USER_ROLE = AUTH_BASE + "/role";

    String ANTIFRAUD_BASE = "/api/antifraud";

    String TRANSACTION = ANTIFRAUD_BASE + "/transaction";

    String SUSPICIOUS_IP = ANTIFRAUD_BASE + "/suspicious-ip";

    String STOLEN_CARD = ANTIFRAUD_BASE + "/stolencard";

    String ANTIFRAUD_HISTORY = ANTIFRAUD_BASE + "/history";
}

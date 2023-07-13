package antifraud.Exceptions;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String username) {
        super(username + " not found!");
    }
}

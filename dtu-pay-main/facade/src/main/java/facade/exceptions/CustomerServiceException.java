package facade.exceptions;

public class CustomerServiceException extends Exception {

    public CustomerServiceException() {
    }

    public CustomerServiceException(String string) {
        super(string);
    }
}
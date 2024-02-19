package exceptions;

public class MerchantServiceException extends Exception {

    public MerchantServiceException() {
    }

    public MerchantServiceException(String string) {
        super(string);
    }
}
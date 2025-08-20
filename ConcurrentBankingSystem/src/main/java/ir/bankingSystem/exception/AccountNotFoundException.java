package ir.bankingSystem.exception;



public class AccountNotFoundException extends  BaseException{


    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, String... params) {
        super(message, params);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotFoundException(String message, Throwable cause, String... params) {
        super(message, cause, params);
    }
}

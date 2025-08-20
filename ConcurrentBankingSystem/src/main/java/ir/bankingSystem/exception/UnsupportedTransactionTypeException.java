package ir.bankingSystem.exception;


public class UnsupportedTransactionTypeException  extends BaseException{


    public UnsupportedTransactionTypeException(String message) {
        super(message);
    }

    public UnsupportedTransactionTypeException(String message, String... params) {
        super(message, params);
    }

    public UnsupportedTransactionTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedTransactionTypeException(String message, Throwable cause, String... params) {
        super(message, cause, params);
    }
}

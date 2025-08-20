package ir.bankingSystem.exception;

import ir.bankingSystem.util.ResourceBundleUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseException extends Exception {


    private  List<String> parameters = new ArrayList<>();

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, String... params) {
        this(message);
        addParameters(params);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message, Throwable cause, String... params) {
        this(message, cause);
        addParameters(params);
    }

    public void addParameters(String... params) {

        Collections.addAll(parameters, params);

    }

    public List<String> getParameters() {
        return parameters;
    }


    @Override
    public String getMessage() {
        return ResourceBundleUtil.getMessage(super.getMessage(), this.getParameters().toArray());
    }
}
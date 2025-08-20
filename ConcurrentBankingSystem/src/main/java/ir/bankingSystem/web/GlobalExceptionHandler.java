package ir.bankingSystem.web;


import ir.bankingSystem.exception.AccountNotFoundException;
import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.exception.LoggedException;
import ir.bankingSystem.model.dto.response.ApiResponse;
import ir.bankingSystem.util.ResourceBundleUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(BaseException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(false, ex.getMessage(), null));
    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        Throwable suppressed = Arrays.stream(ex.getSuppressed()).toList().isEmpty() ? null : ex.getSuppressed()[0];
        Long logId = suppressed instanceof LoggedException ? ((LoggedException) suppressed).getLogId() : -1L;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, ResourceBundleUtil.getMessage("error.default.exception.message",String.valueOf(logId)) , null));
    }
}

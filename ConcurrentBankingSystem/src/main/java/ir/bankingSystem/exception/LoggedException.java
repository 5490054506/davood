package ir.bankingSystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoggedException extends RuntimeException{

    private Long logId;
}

package ir.bankingSystem.aspect;


import ir.bankingSystem.exception.LoggedException;
import ir.bankingSystem.model.entity.CompressedExceptionLog;
import ir.bankingSystem.aspect.api.LogService;
import ir.bankingSystem.util.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.sql.SQLException;


@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final LogService logService;
    private final HttpServletRequest request;


    @AfterReturning(pointcut = "execution(* ir.bankingSystem.service.imple.BankServiceImple.createAccount(..))" , returning = "result")
    public void savingOperationLogAspect(Object result){
        logger.info("saved request and response log in database" );
        logger.info(result.toString());
    }

    @AfterThrowing(pointcut = "execution(* ir.bankingSystem.service.imple.*.*(..))" , throwing = "ex")
    public void saveCompressedExceptionLog(Exception ex) throws SQLException {
        if(ex instanceof RuntimeException) {
            CompressedExceptionLog log = logService.saveCompressedExceptionLog(request, ExceptionUtils.getStackTraceAsString(ex));
            ex.addSuppressed(new LoggedException(log.getId()));
        }

    }
}

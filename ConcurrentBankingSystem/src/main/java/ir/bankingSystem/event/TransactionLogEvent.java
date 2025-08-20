package ir.bankingSystem.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TransactionLogEvent<T> extends ApplicationEvent {


    private final T transactionRequest;

    public TransactionLogEvent(Object source, T transactionRequest) {
        super(source);
        this.transactionRequest = transactionRequest;
    }





}

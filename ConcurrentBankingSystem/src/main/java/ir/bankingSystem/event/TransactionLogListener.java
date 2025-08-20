package ir.bankingSystem.event;

import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TransactionLogListener {

    @TransactionalEventListener( phase = TransactionPhase.AFTER_COMMIT)
    public void handleTransactionLogEvent(TransactionLogEvent<?> event) {
        if (event.getTransactionRequest() instanceof TransactionRequest)
            System.out.println("Transaction happened: " + ((TransactionRequest)event.getTransactionRequest()).getTransactionType().name());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateAccountLogEvent(TransactionLogEvent<?> event) {
        if (event.getTransactionRequest() instanceof CreateAccountRequest)
           System.out.println("CreateAccount happened: " + ((CreateAccountRequest) event.getTransactionRequest()).getAccountNumber());
    }

}

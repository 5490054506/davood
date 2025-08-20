package ir.bankingSystem.model.dto.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private  String accountNumber;
    private  String toAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;

}

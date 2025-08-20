package ir.bankingSystem.model.dto.response;

import ir.bankingSystem.model.dto.request.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private  String accountNumber;
    private  String toAccountNumber;
    private BigDecimal amount;
    private BigDecimal newAmount;
    private TransactionType transactionType;
}

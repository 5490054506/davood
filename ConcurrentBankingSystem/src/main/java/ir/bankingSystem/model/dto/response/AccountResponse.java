package ir.bankingSystem.model.dto.response;

import ir.bankingSystem.model.dto.request.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    private AccountType accountType;
    private String accountNumber;
    private String ownerName;
    private String mobileNumber;
    private BigDecimal balance;
}

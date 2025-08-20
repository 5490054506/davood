package ir.bankingSystem.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateAccountRequest {

    private AccountType accountType;
    private String accountNumber;
    private String ownerName;
    private String mobileNumber;
    private BigDecimal initialBalance;
}

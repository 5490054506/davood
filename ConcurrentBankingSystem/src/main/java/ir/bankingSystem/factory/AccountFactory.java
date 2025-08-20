package ir.bankingSystem.factory;

import ir.bankingSystem.model.dto.request.AccountType;
import ir.bankingSystem.model.entity.BankAccount;
import ir.bankingSystem.model.entity.CheckingAccount;
import ir.bankingSystem.model.entity.SavingsAccount;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {


    public static BankAccount createAccount(AccountType type) {
        return switch (type) {
            case CHECKING -> new CheckingAccount();
            case SAVINGS -> new SavingsAccount();
        };
    }
}

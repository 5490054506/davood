package ir.bankingSystem.service.imple;


import ir.bankingSystem.exception.AccountNotFoundException;
import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.request.TransactionType;
import ir.bankingSystem.model.dto.response.TransactionResponse;
import ir.bankingSystem.model.entity.BankAccount;
import ir.bankingSystem.repository.BankAccountRepository;
import ir.bankingSystem.service.api.LockManager;
import ir.bankingSystem.service.api.Transaction;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DepositService implements Transaction {


    private final LockManager lockManager;
    private final BankAccountRepository bankAccountRepository;


    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public TransactionResponse execute(TransactionRequest request) throws BaseException {

        TransactionResponse transactionResponse = new TransactionResponse();
        RLock lock = lockManager.lockOne(request.getAccountNumber());
        try {
            BankAccount bankAccount = bankAccountRepository.findByAccountNumberForUpdate(request.getAccountNumber()).orElseThrow(() -> new AccountNotFoundException("error.account.notfound"));
            bankAccount.setBalance(bankAccount.getBalance().add(request.getAmount()));
            bankAccount = bankAccountRepository.save(bankAccount);
            transactionResponse.setTransactionType(TransactionType.DEPOSIT);
            transactionResponse.setNewAmount(bankAccount.getBalance());
            transactionResponse.setAmount(request.getAmount());
            transactionResponse.setAccountNumber(bankAccount.getAccountNumber());
        } finally {
            lockManager.unlockOne(lock);
        }

        return transactionResponse;
    }
}

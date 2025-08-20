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

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class WithdrawService implements Transaction {

    private final LockManager lockManager;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }

    @Override
    public TransactionResponse execute(TransactionRequest request) throws BaseException {

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BaseException("Amount.must.be.positive");
        }

        TransactionResponse transactionWithdrawResponse = new TransactionResponse();
        RLock lock = lockManager.lockOne(request.getAccountNumber());
        try {
            BankAccount bankAccount = bankAccountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("error.account.notfound"));
            if (bankAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BaseException("error.is.not.enough.inventory");
            }
            bankAccount.setBalance(bankAccount.getBalance().subtract(request.getAmount()));
            bankAccount = bankAccountRepository.save(bankAccount);
            transactionWithdrawResponse.setTransactionType(TransactionType.WITHDRAW);
            transactionWithdrawResponse.setNewAmount(bankAccount.getBalance());
            transactionWithdrawResponse.setAmount(request.getAmount());
            transactionWithdrawResponse.setAccountNumber(bankAccount.getAccountNumber());
        } finally {
            lockManager.unlockOne(lock);
        }

        return transactionWithdrawResponse;
    }
}

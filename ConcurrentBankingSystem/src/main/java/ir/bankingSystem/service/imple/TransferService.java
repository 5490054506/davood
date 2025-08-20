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

@AllArgsConstructor
@Service
public class TransferService implements Transaction {

    private final LockManager lockManager;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public TransactionResponse execute(TransactionRequest request) throws BaseException {
        TransactionResponse transactionTransferResponse = new TransactionResponse();

        RLock[] locks = lockManager.lockTwo(request.getAccountNumber(),request.getToAccountNumber());
        try {
            BankAccount fromAccount = bankAccountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("error.from.account.notfound"));
            BankAccount toAccount = bankAccountRepository.findByAccountNumberForUpdate(request.getToAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("error.to.account.notfound"));
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BaseException("error.is.not.enough.inventory");
            }
            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
            fromAccount = bankAccountRepository.save(fromAccount);
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
            toAccount = bankAccountRepository.save(toAccount);
            transactionTransferResponse.setTransactionType(TransactionType.TRANSFER);
            transactionTransferResponse.setNewAmount(fromAccount.getBalance());
            transactionTransferResponse.setAmount(request.getAmount());
            transactionTransferResponse.setAccountNumber(fromAccount.getAccountNumber());
            transactionTransferResponse.setToAccountNumber(toAccount.getAccountNumber());
        } finally {
            lockManager.unlockTwo(locks);
        }
        return transactionTransferResponse;
    }
}

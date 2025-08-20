package ir.bankingSystem.service.imple;


import ir.bankingSystem.event.TransactionLogEvent;
import ir.bankingSystem.exception.AccountNotFoundException;
import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.exception.UnsupportedTransactionTypeException;
import ir.bankingSystem.factory.AccountFactory;
import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.request.TransactionType;
import ir.bankingSystem.model.dto.response.AccountResponse;
import ir.bankingSystem.model.dto.response.TransactionResponse;
import ir.bankingSystem.model.entity.BankAccount;
import ir.bankingSystem.model.mapperDto.BankingMapper;
import ir.bankingSystem.repository.BankAccountRepository;
import ir.bankingSystem.service.api.BankService;
import ir.bankingSystem.service.api.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class BankServiceImple implements BankService {

    private final Map<TransactionType, Transaction> strategyMap = new EnumMap<>(TransactionType.class);
    private static final Logger logger = LoggerFactory.getLogger(BankServiceImple.class);
    private final ApplicationEventPublisher publisher;
    private final BankAccountRepository bankAccountRepository;
    private final BankingMapper mapper;


    public BankServiceImple(List<Transaction> strategies, ApplicationEventPublisher publisher, BankAccountRepository bankAccountRepository, BankingMapper mapper) {
        this.publisher = publisher;
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
        strategies.forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) throws BaseException {
        if (bankAccountRepository.findBankAccountByAccountNumber(createAccountRequest.getAccountNumber()).isPresent()) {
            throw new BaseException("Account.already.exists");
        }
        BankAccount account = AccountFactory.createAccount(createAccountRequest.getAccountType());
        account.setBalance(createAccountRequest.getInitialBalance());
        account.setOwnerName(createAccountRequest.getOwnerName());
        account.setAccountNumber(createAccountRequest.getAccountNumber());
        account.setMobileNumber(createAccountRequest.getMobileNumber());
        account = bankAccountRepository.save(account);

        publisher.publishEvent(new TransactionLogEvent<>(this, createAccountRequest));
        logger.info("Account created with id={}, owner={}", account.getId(), account.getOwnerName());

        return mapper.toDtoBankAccount(account);
    }

    @Retryable(
            retryFor = {org.springframework.dao.OptimisticLockingFailureException.class, jakarta.persistence.OptimisticLockException.class},
            backoff = @Backoff(delay = 10, multiplier = 1.5)
    )
    @Transactional
    @Override
    public TransactionResponse executeTransaction(TransactionRequest request) throws BaseException {
        Transaction strategy = strategyMap.get(request.getTransactionType());
        if (strategy == null) {
            throw new UnsupportedTransactionTypeException("transaction.fail");
        }
        TransactionResponse transactionResponse = strategy.execute(request);

        publisher.publishEvent(new TransactionLogEvent<>(this, request));

        return transactionResponse;
    }


    @Transactional(readOnly = true)
    @Override
    public AccountResponse displayAllBalances(String accountNumber) throws AccountNotFoundException {
        return bankAccountRepository.findBankAccountByAccountNumber(accountNumber)
                .map(mapper::toDtoBankAccount)
                .orElseThrow(() -> new AccountNotFoundException("error.account.notfound"));
    }
}


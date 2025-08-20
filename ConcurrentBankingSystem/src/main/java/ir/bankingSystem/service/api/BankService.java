package ir.bankingSystem.service.api;

import ir.bankingSystem.exception.AccountNotFoundException;
import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.response.AccountResponse;
import ir.bankingSystem.model.dto.response.TransactionResponse;

public interface BankService {

    AccountResponse createAccount(CreateAccountRequest createAccountRequest) throws BaseException;

    AccountResponse displayAllBalances(String accountNumber) throws AccountNotFoundException;

    TransactionResponse executeTransaction(TransactionRequest request) throws BaseException;


}

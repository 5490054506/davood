package ir.bankingSystem.service.api;


import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.request.TransactionType;
import ir.bankingSystem.model.dto.response.TransactionResponse;

public interface Transaction {

    TransactionType getType();
    TransactionResponse execute(TransactionRequest request) throws BaseException;
}

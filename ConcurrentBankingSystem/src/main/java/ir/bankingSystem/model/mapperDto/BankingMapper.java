package ir.bankingSystem.model.mapperDto;

import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.response.AccountResponse;
import ir.bankingSystem.model.entity.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankingMapper {

    BankAccount toEntityBankAccount(CreateAccountRequest request);

    AccountResponse toDtoBankAccount(BankAccount bankAccount);

}

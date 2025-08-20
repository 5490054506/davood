package ir.bankingSystem.web;


import ir.bankingSystem.exception.AccountNotFoundException;
import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.response.ApiResponse;
import ir.bankingSystem.model.dto.response.AccountResponse;
import ir.bankingSystem.model.dto.response.TransactionResponse;
import ir.bankingSystem.service.imple.BankServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankController {


    private final BankServiceImple bankServiceImple;


    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> handleTransaction(@RequestBody TransactionRequest request) throws BaseException {
        return ResponseEntity.ok(bankServiceImple.executeTransaction(request));
    }

    @ResponseBody
    @PostMapping("/createAccounts")
    public ApiResponse<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) throws BaseException {
        return new ApiResponse<>(true,"success" , bankServiceImple.createAccount(request));
    }

    @GetMapping("/getAccount/{accountNumber}")
    public ApiResponse<AccountResponse> getAccount(@PathVariable String accountNumber) throws AccountNotFoundException {
        return new ApiResponse<>(true,"success" , bankServiceImple.displayAllBalances(accountNumber));
    }
}

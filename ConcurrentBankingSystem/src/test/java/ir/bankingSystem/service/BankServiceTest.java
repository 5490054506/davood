package ir.bankingSystem.service;


import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.model.dto.request.AccountType;
import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.request.TransactionType;
import ir.bankingSystem.model.dto.response.AccountResponse;
import ir.bankingSystem.service.api.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {

    @Autowired
    private BankService bankService;


    @Test
    void testConcurrentTransfers() throws BaseException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(1000);

        bankService.createAccount(new CreateAccountRequest(AccountType.CHECKING, "1234", "ALI", "09130135702", BigDecimal.valueOf(100)));

        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> {
                try {
                    TransactionRequest req = new TransactionRequest();
                    req.setTransactionType(TransactionType.DEPOSIT);
                    req.setAccountNumber("1234");
                    req.setAmount(BigDecimal.valueOf(100));
                    bankService.executeTransaction(req);
                } catch (BaseException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(20, TimeUnit.SECONDS);
        executorService.shutdown();
        AccountResponse accountResponse = bankService.displayAllBalances("1234");
        Assertions.assertEquals(100100, accountResponse.getBalance().longValue(), "The balance should be 100100.");
    }
}

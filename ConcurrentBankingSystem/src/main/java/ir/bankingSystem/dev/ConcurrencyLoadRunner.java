package ir.bankingSystem.dev;

import ir.bankingSystem.model.dto.request.AccountType;
import ir.bankingSystem.model.dto.request.CreateAccountRequest;
import ir.bankingSystem.model.dto.request.TransactionRequest;
import ir.bankingSystem.model.dto.request.TransactionType;
import ir.bankingSystem.model.dto.response.TransactionResponse;
import ir.bankingSystem.service.api.BankService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Profile("dev")
public class ConcurrencyLoadRunner implements ApplicationRunner {

    private final BankService bankService;
    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyLoadRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(1000);
        Locale testLocale = Locale.getDefault();

        bankService.createAccount(new CreateAccountRequest(AccountType.CHECKING, "1234", "ALI", "09130135702", BigDecimal.valueOf(100000000)));


        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
                try {
                    TransactionRequest req = new TransactionRequest();
                    req.setTransactionType(TransactionType.DEPOSIT);
                    req.setAccountNumber("1234");
                    req.setAmount(BigDecimal.valueOf(1000));
                    TransactionResponse transactionResponse = bankService.executeTransaction(req);
                    logger.info("transaction success amount={} , newAmount={} , accountNumber={} , AccountType={}", transactionResponse.getAmount(), transactionResponse.getNewAmount(), transactionResponse.getAccountNumber(), transactionResponse.getTransactionType());

                } catch (Exception e) {
                    logger.info("error transaction");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(20, TimeUnit.SECONDS);
        pool.shutdown();
    }
}

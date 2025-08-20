package ir.bankingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ir.bankingSystem.repository"})
@EnableAspectJAutoProxy
public class ConcurrentBankingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentBankingSystemApplication.class, args);
    }

}

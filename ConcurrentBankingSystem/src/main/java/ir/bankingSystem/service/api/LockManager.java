package ir.bankingSystem.service.api;

import ir.bankingSystem.exception.BaseException;
import org.redisson.api.RLock;

public interface LockManager {

    RLock lockOne(String accountNumber) throws BaseException;

    RLock[] lockTwo(String fromAccount, String toAccount) throws BaseException;

    void unlockOne(RLock l);

    void unlockTwo(RLock[] ls);
}

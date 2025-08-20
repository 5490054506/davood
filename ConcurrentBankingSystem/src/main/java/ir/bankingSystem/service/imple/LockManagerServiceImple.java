package ir.bankingSystem.service.imple;

import ir.bankingSystem.exception.BaseException;
import ir.bankingSystem.service.api.LockManager;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class LockManagerServiceImple implements LockManager {

    private final RedissonClient redisson;

    private RLock getLock(String key) {
        return redisson.getLock("account:" + key);
    }


    @Override
    public RLock lockOne(String accountNumber) throws BaseException {
        RLock l = getLock(accountNumber);
        try {
            l.lock(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BaseException("lock.failed");
        }
        return l;
    }

    @Override
    public RLock[] lockTwo(String fromAccount, String toAccount) throws BaseException {

        String first = fromAccount.compareTo(toAccount) < 0 ? fromAccount : toAccount;
        String  second = fromAccount.compareTo(toAccount) < 0 ? toAccount : fromAccount;

        RLock l1 = getLock(first), l2 = getLock(second);
        try {
            l1.lock(20, TimeUnit.SECONDS);
            l2.lock(20, TimeUnit.SECONDS); }
        catch (Exception e) {
            try {
                if (l2.isHeldByCurrentThread())
                    l2.unlock();
            }
            finally {
                if (l1.isHeldByCurrentThread())
                    l1.unlock();
            }
            throw new BaseException("lock.failed");
        }
        return new RLock[]{ l1, l2 };
    }

    @Override
    public void unlockOne(RLock l) {
        if (l.isHeldByCurrentThread())
            l.unlock();
    }

    @Override
    public void unlockTwo(RLock[] ls) {

        if (ls == null) return;
        try {
            if (ls[1].isHeldByCurrentThread())
                ls[1].unlock();
        }
        finally {
            if (ls[0].isHeldByCurrentThread())
                ls[0].unlock();
        }
    }
}

package me.galaxy.lock.sample;

import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.spring.ActionLockTemplate;
import me.galaxy.lock.spring.annotation.LockAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ActionLockTemplate firstActionLockTemplate;

    public void save() {
        SimpleLock lock = firstActionLockTemplate.create("testLock");
        lock.lock(5000L, 500L);
        lock.unlock();
    }

    @LockAction(actionLockTemplate = "secondActionLockTemplate")
    public void select() {

    }

}

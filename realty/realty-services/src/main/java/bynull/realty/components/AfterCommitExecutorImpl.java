package bynull.realty.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author dionis on 05/12/14.
 */
@Component
public class AfterCommitExecutorImpl extends TransactionSynchronizationAdapter implements AfterCommitExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfterCommitExecutorImpl.class);

    private static final ThreadLocal<Set<Runnable>> RUNNABLES = new ThreadLocal<>();

    @Override
    public void execute(Runnable runnable) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            runnable.run();
            return;
        }
        Set<Runnable> threadRunnables = RUNNABLES.get();
        if (threadRunnables == null) {
            threadRunnables = new LinkedHashSet<>();
            RUNNABLES.set(threadRunnables);
            TransactionSynchronizationManager.registerSynchronization(this);
        }
        threadRunnables.add(runnable);
    }

    @Override
    public void executeMultiple(Runnable... runnables) {
        if (runnables == null || runnables.length == 0) {
            return;
        }
        for (Runnable runnable : runnables) {
            execute(runnable);
        }
    }

    @Override
    public void afterCommit() {
        Set<Runnable> threadRunnables = RUNNABLES.get();

        for (Runnable runnable : threadRunnables) {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                LOGGER.error("Failed to execute runnable " + runnable, e);
                throw e;
            }
        }
    }

    @Override
    public void afterCompletion(int status) {
        RUNNABLES.remove();
    }

}

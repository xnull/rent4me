package bynull.realty.components;

import bynull.realty.services.api.AsyncExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author dionis on 05/12/14.
 */
@Component
public class AfterCommitExecutorImpl extends TransactionSynchronizationAdapter implements AfterCommitExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfterCommitExecutorImpl.class);

    private static final ThreadLocal<Set<Runnable>> RUNNABLES = new ThreadLocal<>();

    @Resource
    AsyncExecutor asyncExecutor;

    @Resource
    TransactionOperations transactionOperations;

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
    public void executeAsynchronously(Runnable command) {
        execute(()->asyncExecutor.execute(command));
    }

    @Override
    public void executeAsynchronouslyInTransaction(Runnable command) {
        executeAsynchronously(()->transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                command.run();
            }
        }));
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

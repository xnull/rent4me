package bynull.realty.components;

import java.util.concurrent.Executor;

/**
 * @author dionis on 05/12/14.
 */
public interface AfterCommitExecutor extends Executor {
    void executeMultiple(Runnable... runnables);
}

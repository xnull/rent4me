package bynull.realty.services.impl;

import bynull.realty.services.api.AsyncExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 4/30/15.
 */
@Component
public class AsyncExecutorImpl implements AsyncExecutor {
    @Async
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}

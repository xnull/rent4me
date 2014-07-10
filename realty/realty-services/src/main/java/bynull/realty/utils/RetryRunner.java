package bynull.realty.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author dionis on 09/07/14.
 */
public class RetryRunner<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryRunner.class);
    private final int retryAttempts;

    public RetryRunner(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public T doWithRetry(Callable<T> callable) throws RetryFailedException {
        int attempt = 0;
        do {
            attempt++;
            try {
                return callable.call();
            } catch (Exception e) {
                LOGGER.error("Exception happened at attempt ["+attempt+"]/ "+retryAttempts+".", e);
            }
        } while (attempt < retryAttempts);
        throw new RetryFailedException("Failed to execute job in "+retryAttempts+" attempts.");
    }

    public static class RetryFailedException extends Exception {
        private RetryFailedException() {
        }

        private RetryFailedException(String message) {
            super(message);
        }

        private RetryFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        private RetryFailedException(Throwable cause) {
            super(cause);
        }

        private RetryFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}

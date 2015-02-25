package realty.db.backup;

/**
 * @author dionis on 2/25/15.
 */
public class DbToolException extends RuntimeException {
    public DbToolException() {
    }

    public DbToolException(String message) {
        super(message);
    }

    public DbToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbToolException(Throwable cause) {
        super(cause);
    }

    public DbToolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

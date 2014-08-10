package bynull.realty.services.metro;

/**
 * Created by null on 11.08.14.
 */
public class MetroServiceException extends Exception {
    public MetroServiceException(Exception e) {
        super(e);
    }

    public MetroServiceException() {
        super();
    }

    public MetroServiceException(String message) {
        super(message);
    }

    public MetroServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetroServiceException(Throwable cause) {
        super(cause);
    }

    public MetroServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

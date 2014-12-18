package bynull.realty.exeptions;

/**
 * User: trierra
 * Date: 11/14/14
 */
public class EmptyHiddenVkValue extends Throwable {

    public EmptyHiddenVkValue() {
        super();
    }

    public EmptyHiddenVkValue(String field) {
        super("Oops! Can not find " + field + " field while parsing VK authorisation page in auth_token getting process");
    }
}

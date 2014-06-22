package bynull.realty;

/**
 * @author dionis on 22/06/14.
 */
public class LauncherRest {
    private static final String DEFAULT_PATH = "src/main/webapp";
    private static final String MULTI_MODULE_DEFAULT_PATH = "realty-web/src/main/webapp";

    public static void main(String[] args) throws Exception {
        new BaseLauncher(LauncherRest.class.getResource("LauncherRest.class"), LauncherRest.class, DEFAULT_PATH, MULTI_MODULE_DEFAULT_PATH, "").doStart("8888");
    }
}

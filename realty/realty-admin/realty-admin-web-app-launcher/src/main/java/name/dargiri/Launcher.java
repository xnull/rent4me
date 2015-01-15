package name.dargiri;

/**
 * Created by dionis on 2/2/14.
 */
public class Launcher {
    private static final String DEFAULT_PATH = "src/main/webapp";
    private static final String MULTI_MODULE_DEFAULT_PATH = "realty-admin/web/src/main/webapp";

    public static void main(String[] args) throws Exception {
        new BaseLauncher(Launcher.class.getResource("Launcher.class"), Launcher.class, DEFAULT_PATH, MULTI_MODULE_DEFAULT_PATH, "admin").doStart("8888");
    }
}

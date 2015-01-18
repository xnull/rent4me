package name.dargiri;

/**
 * Created by dionis on 2/2/14.
 */
public class LauncherAdmin {
    private static final String DEFAULT_PATH = "src/main/webapp";
    private static final String MULTI_MODULE_DEFAULT_PATH = "realty-admin/realty-admin-web/src/main/webapp";

    public static void main(String[] args) throws Exception {
        new BaseLauncher(LauncherAdmin.class.getResource("LauncherAdmin.class"), LauncherAdmin.class, DEFAULT_PATH, MULTI_MODULE_DEFAULT_PATH, "admin").doStart("8888");
    }
}

package bynull.realty;

import java.util.Arrays;

/**
 * @author dionis on 22/06/14.
 */
public final class LauncherAdmin {
    private LauncherAdmin() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("spring.profiles.active", "local");
//        System.setProperty("spring.profiles.active", "dev");

        new BaseLauncher(LauncherAdmin.class.getResource("LauncherAdmin.class"), LauncherAdmin.class, Arrays.asList(
                new BaseLauncher.HandlerDesc("src/main/webapp", "realty-admin/realty-admin-web/src/main/webapp", "admin")
        )).doStart("18080");
    }
}

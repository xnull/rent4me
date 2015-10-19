package bynull.realty;

import java.util.Arrays;

/**
 * @author dionis on 22/06/14.
 */
public final class LauncherRest {
    private LauncherRest() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("spring.profiles.active", "local");
//        System.setProperty("spring.profiles.active", "dev");

        new BaseLauncher(LauncherRest.class.getResource("LauncherRest.class"), LauncherRest.class, Arrays.asList(
                new BaseLauncher.HandlerDesc("src/main/webapp", "realty-web/src/main/webapp", "")
//                , new BaseLauncher.HandlerDesc("build-js", "realty-client/build-js", "dev")
//                , new BaseLauncher.HandlerDesc("app/start", "realty-client/app/start", "start")
//                , new BaseLauncher.HandlerDesc("app/personal", "realty-client/app/personal", "personal")
//                , new BaseLauncher.HandlerDesc("node_modules", "realty-client/node_modules", "node_modules")
        )).doStart("8888");
    }
}

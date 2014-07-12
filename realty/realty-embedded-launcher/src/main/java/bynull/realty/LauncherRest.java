package bynull.realty;

import java.util.Arrays;

/**
 * @author dionis on 22/06/14.
 */
public final class LauncherRest {
    private LauncherRest() {
    }

    public static void main(String[] args) throws Exception {
        new BaseLauncher(LauncherRest.class.getResource("LauncherRest.class"), LauncherRest.class, Arrays.asList(
                new BaseLauncher.HandlerDesc("src/main/webapp", "realty-web/src/main/webapp", "")
                , new BaseLauncher.HandlerDesc("app", "realty-client/app", "client")
        )).doStart("8888");
    }
}

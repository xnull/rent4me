package name.dargiri;

import name.dargiri.util.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author dionis
 *         1/21/14 10:29 AM
 */
public class BaseLauncher {
    private Logger logger;
    private final Class<?> targetClass;
    private final String defaultPath;
    private final String multiModuleDefaultPath;
    private final URL url;
    private final String webAppPath;

    /**
     * @param url                    Class your using this code from should be used to obtain url instance. Example: {@code Launcher.class.getResource("Launcher.class")}
     * @param targetClass            Class your using this code from should be used. Example: {@code Launcher.class}
     * @param defaultPath
     * @param multiModuleDefaultPath
     * @param webAppPath
     */
    public BaseLauncher(URL url, Class<?> targetClass, String defaultPath, String multiModuleDefaultPath, String webAppPath) {
        Assert.notNull(url);
        Assert.notNull(targetClass);
        Assert.notNull(defaultPath);
        Assert.notNull(multiModuleDefaultPath);
        Assert.notNull(webAppPath);
        this.url = url;
        this.targetClass = targetClass;
        this.defaultPath = defaultPath;
        this.multiModuleDefaultPath = multiModuleDefaultPath;
        this.webAppPath = webAppPath;
    }

    public void doStart(String... args) throws Exception {
        if (isLaunchFromIDE()) {
            setUpLogDirectory();
//            if (StringUtils.containsIgnoreCase(System.getProperty("user.home"), "dionis")) {
//                getLogger().info("Using profile for dionis.");
//                System.setProperty(Constants.SPRING_PROFILES_ACTIVE, SupportedSpringProfiles.RUN_PROFILE_DEV);
//                //System.setProperty(Constants.SPRING_PROFILES_ACTIVE, SupportedSpringProfiles.RUN_PROFILE_DEV);
//            } else {
//                System.setProperty(Constants.SPRING_PROFILES_ACTIVE, SupportedSpringProfiles.RUN_PROFILE_DEV_LOCAL);
//            }
//        }
            // System.setProperty(Constants.SPRING_PROFILES_ACTIVE, SupportedSpringProfiles.RUN_PROFILE_DEV_LOCAL);
//        if (System.getProperty(Constants.SPRING_PROFILES_ACTIVE) == null) {
//            getLogger().error("No spring profile selected");
//            System.exit(1);
//        }
        }

        startServer(getPortOrDefault(args, 8888));
    }

    private void setUpLogDirectory() {
        final String logPathBase = ".";
        System.setProperty("catalina.base", logPathBase);
        final String logPath = logPathBase + "/logs";
        final boolean mkdirs = new File(logPath).mkdirs();
        getLogger().info("Log dir created? [{}]", mkdirs);
    }

    private void startServer(int port) throws Exception {
        long start = System.currentTimeMillis();
        Server server = new Server();
        SelectChannelConnector selectChannelConnector = new SelectChannelConnector();
        selectChannelConnector.setAcceptors(Math.max(Runtime.getRuntime().availableProcessors(), 1));
        selectChannelConnector.setPort(port);
        selectChannelConnector.setAcceptQueueSize(10000);
        selectChannelConnector.setThreadPool(new QueuedThreadPool());
        server.setConnectors(new Connector[]{selectChannelConnector});

        WebAppContext handler = new WebAppContext(getWebAppPath(), "/" + webAppPath);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.addHandler(handler);
        server.setHandler(contextHandlerCollection);
        server.start();
        long end = System.currentTimeMillis();
        getLogger().info("Initialization took {} milliseconds", end - start);
        logMemoryUsage();
        server.join();
//        Config config = new Config();
//        config.setProperty("DEBUG", "true");
//        Main.main(new String[]{"jetty.xml"});
    }

    private static int getPortOrDefault(String[] args, int defaultPort) {
        return args.length == 1 ? Integer.valueOf(args[0]) : defaultPort;
    }

    private void logMemoryUsage() {
        getLogger().info("Memory usage after start: {} MB", currentMemoryUsageMB());
    }

    private static double currentMemoryUsageMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024.0;
    }

    /**
     * Depending on whether it is launched from IDE or with command java -jar hm.jar return appropriate path.
     *
     * @return
     * @throws java.io.IOException
     */
    private String getWebAppPath() throws IOException {
        if (isLaunchFromIDE()) {
            getLogger().info("Launched from IDE: Using default path");
            if (isStandaloneDefaultPath())
                return getStandaloneDefaultPath();
            else if (isMultiModuleDefaultPath()) {
                return getMultiModuleDefaultPath();
            } else {
                throw new IllegalStateException("Unsupported default path");
            }
        } else {
            final File localTemporaryDirectory = new File("./tmp/");
            localTemporaryDirectory.mkdir();
            localTemporaryDirectory.deleteOnExit();

            System.out.println("Class: " + BaseLauncher.class.getName());
            URL url = this.url;
            System.out.println("URL: " + url);
            String scheme = url.getProtocol();
            if (!"jar".equals(scheme))
                throw new IllegalArgumentException("Unsupported scheme: " + scheme);
            JarURLConnection con = (JarURLConnection) url.openConnection();
            JarFile archive = con.getJarFile();
            Enumeration<JarEntry> entries = archive.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                final String path = entry.getName();
                if (path.startsWith("WEB-INF") || path.startsWith("static")) {

                    if (entry.isDirectory()) {
                        final File dir = new File("./tmp/" + path);
                        dir.deleteOnExit();
                        dir.mkdir();
                    } else {
                        File temporaryFile = new File(localTemporaryDirectory, entry.getName());
                        temporaryFile.deleteOnExit();
                        FileUtils.writeByteArrayToFile(temporaryFile, IOUtils.toByteArray(targetClass.getResourceAsStream(entry.getName())));
                    }
                }
            }
            return "./tmp";
        }
    }

    private String getStandaloneDefaultPath() {
        return defaultPath;
    }

    private boolean isLaunchFromIDE() {
        return isStandaloneDefaultPath() || isMultiModuleDefaultPath();
    }

    private boolean isStandaloneDefaultPath() {
        return new File(getStandaloneDefaultPath()).exists();
    }

    private boolean isMultiModuleDefaultPath() {
        return new File(getMultiModuleDefaultPath()).exists();
    }

    private String getMultiModuleDefaultPath() {
        return multiModuleDefaultPath;
    }

    private synchronized Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(targetClass);
        }
        return logger;
    }
}

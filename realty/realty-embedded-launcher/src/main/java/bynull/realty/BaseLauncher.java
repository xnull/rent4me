package bynull.realty;

import bynull.realty.util.Assert;
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
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author dionis on 22/06/14.
 */
public class BaseLauncher {
    private final Class<?> targetClass;
    private final List<HandlerDesc> handlerDescs;
    private final URL url;
    private Logger logger;

    /**
     * @param url          Class your using this code from should be used to obtain url instance. Example: {@code Launcher.class.getResource("Launcher.class")}
     * @param targetClass  Class your using this code from should be used. Example: {@code Launcher.class}
     * @param handlerDescs
     */
    public BaseLauncher(URL url, Class<?> targetClass, List<HandlerDesc> handlerDescs) {
        Assert.notNull(url);
        Assert.notNull(targetClass);
        Assert.notNull(handlerDescs);
        this.url = url;
        this.targetClass = targetClass;
        this.handlerDescs = handlerDescs;
    }

    private static int getPortOrDefault(String[] args, int defaultPort) {
        return args.length == 1 ? Integer.parseInt(args[0]) : defaultPort;
    }

    private static double currentMemoryUsageMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024.0;
    }

    public void doStart(String... args) throws Exception {
        if (isLaunchFromIDE(handlerDescs.get(0))) {
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

        final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        for (HandlerDesc handlerDesc : handlerDescs) {
            WebAppContext handler = new WebAppContext(getWebAppPath(handlerDesc), "/" + handlerDesc.webAppPath);
            contextHandlerCollection.addHandler(handler);
        }
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

    private void logMemoryUsage() {
        getLogger().info("Memory usage after start: {} MB", currentMemoryUsageMB());
    }

    /**
     * Depending on whether it is launched from IDE or with command java -jar hm.jar return appropriate path.
     *
     * @return
     * @throws java.io.IOException
     */
    private String getWebAppPath(HandlerDesc handlerDesc) throws IOException {
        if (isLaunchFromIDE(handlerDesc)) {
            getLogger().info("Launched from IDE: Using default path");
            if (isStandaloneDefaultPath(handlerDesc))
                return getStandaloneDefaultPath(handlerDesc);
            else if (isMultiModuleDefaultPath(handlerDesc)) {
                return getMultiModuleDefaultPath(handlerDesc);
            } else {
                throw new IllegalStateException("Unsupported default path");
            }
        } else {
            final File localTemporaryDirectory = new File("./tmp/");
            if (!localTemporaryDirectory.mkdir()) {
                throw new IllegalStateException("Could not create local temporary directory " + localTemporaryDirectory);
            }
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
                        if (!dir.mkdir()) {
                            throw new IllegalStateException("Could not create directory for entry " + dir);
                        }
                        dir.deleteOnExit();
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

    private String getStandaloneDefaultPath(HandlerDesc handlerDesc) {
        return handlerDesc.defaultPath;
    }

    private boolean isLaunchFromIDE(HandlerDesc handlerDesc) {
        return isStandaloneDefaultPath(handlerDesc) || isMultiModuleDefaultPath(handlerDesc);
    }

    private boolean isStandaloneDefaultPath(HandlerDesc handlerDesc) {
        return new File(getStandaloneDefaultPath(handlerDesc)).exists();
    }

    private boolean isMultiModuleDefaultPath(HandlerDesc handlerDesc) {
        return new File(getMultiModuleDefaultPath(handlerDesc)).exists();
    }

    private String getMultiModuleDefaultPath(HandlerDesc handlerDesc) {
        return handlerDesc.multiModuleDefaultPath;
    }

    private synchronized Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(targetClass);
        }
        return logger;
    }

    public static class HandlerDesc {
        public final String defaultPath, multiModuleDefaultPath, webAppPath;

        public HandlerDesc(String defaultPath, String multiModuleDefaultPath, String webAppPath) {
            this.defaultPath = defaultPath;
            this.multiModuleDefaultPath = multiModuleDefaultPath;
            this.webAppPath = webAppPath;
        }
    }
}

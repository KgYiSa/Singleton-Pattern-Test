package com.mj.tcs.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides generic information about the MJTCS environment.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public final class Environment {

    /**
     * Our properties file.
     */
    private static final String propsFile = "/mjtcs.properties";
    /**
     * The MJTCS version as a string.
     */
    private static final String versionString;
    /**
     * The build date as a string.
     */
    private static final String buildDate;
    /**
     * This class's Logger.
     */
    private static final Logger log = Logger.getLogger(Environment.class);

    static {
        Properties props = new Properties();
        try {
            props.load(Environment.class.getResourceAsStream(propsFile));
        }
        catch (IOException exc) {
            log.warn("Exception loading properties from " + propsFile,
                    exc);
        }
        versionString = props.getProperty("opentcs.version", "unknown version");
        buildDate = props.getProperty("opentcs.builddate", "unknown build date");
    }

    /**
     * Prevents undesired instantiation.
     */
    private Environment() {
        // Do nada.
    }

    /**
     * Returns the version of MJTCS (i.e. the base library) as a string.
     *
     * @return The version of MJTCS (i.e. the base library) as a string.
     */
    public static String getVersionString() {
        return versionString;
    }

    /**
     * Returns the build date of MJTCS (i.e. the base library) as a string.
     *
     * @return The build date of MJTCS (i.e. the base library) as a string.
     */
    public static String getBuildDate() {
        return buildDate;
    }

    /**
     * Write information about the OpenTCS version, the operating system and
     * the running Java VM to the log.
     */
    public static void logSystemInfo() {
        String systemInfo = new StringBuilder()
                .append("MJ-TCS-SERVER: ")
                .append(versionString)
                .append(" (build date: ")
                .append(buildDate)
                .append("); ")
                .append("Java: ")
                .append(System.getProperty("java.version"))
                .append(", ")
                .append(System.getProperty("java.vendor"))
                .append("; ")
                .append("JVM: ")
                .append(System.getProperty("java.vm.version"))
                .append(", ")
                .append(System.getProperty("java.vm.vendor"))
                .append("; ")
                .append("OS: ")
                .append(System.getProperty("os.name"))
                .append(", ")
                .append(System.getProperty("os.arch"))
                .toString();
        log.info(systemInfo);
    }
}

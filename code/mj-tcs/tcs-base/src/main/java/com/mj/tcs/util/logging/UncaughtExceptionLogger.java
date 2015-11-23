package com.mj.tcs.util.logging;

import org.apache.log4j.Logger;

/**
 * An UncaughtExceptionHandler that logs everything not caught and then exits.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class UncaughtExceptionLogger implements Thread.UncaughtExceptionHandler {

    /**
     * This class's Logger.
     */
    private static final Logger log = Logger.getLogger(UncaughtExceptionLogger.class);
    /**
     * A flag indicating whether to exit on uncaught exceptions or not.
     */
    private final boolean doExit;

    /**
     * Creates a new UncaughtExceptionLogger.
     *
     * @param exitOnException A flag indicating whether to exit on uncaught
     * exceptions or not.
     */
    public UncaughtExceptionLogger(boolean exitOnException) {
        super();
        doExit = exitOnException;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Log the exception, and then get out of here.
        log.error("Unhandled exception", e);
        if (doExit) {
            System.exit(1);
        }
    }
}
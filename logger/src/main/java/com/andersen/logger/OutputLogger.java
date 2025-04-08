package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A utility class for logging messages using SLF4J.
 * This class provides static methods to log informational messages,
 * warnings, and errors, outputting them to both the console and a logging framework.
 */
@Component
public class OutputLogger {
    private static final Logger logger = LoggerFactory.getLogger(OutputLogger.class);

    /**
     * Logs an informational message.
     *
     * @param message the message to be logged
     */
    public static void log(String message) {
        logger.info(message);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message to be logged
     */
    public static void error(String message) {
        logger.error(message);

    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message to be logged
     */
    public static void warn(String message) {
        logger.warn(message);

    }
}
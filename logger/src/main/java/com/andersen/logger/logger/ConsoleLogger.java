package com.andersen.logger.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A utility class for logging messages to the console using SLF4J.
 * This class provides a static method to log information-level messages.
 */
@Component
public class ConsoleLogger {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);

    /**
     * Logs an information message to the console.
     *
     * @param message the message to be logged
     */
    public static void log(String message) {
        logger.info(message);
    }
}
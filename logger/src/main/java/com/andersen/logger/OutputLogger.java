package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputLogger {
    private static final Logger logger = LoggerFactory.getLogger(OutputLogger.class);

    public static void log(String message) {
        logger.info(message);
        System.out.println(message);
    }

    public static void error(String message) {
        logger.error(message);
        System.err.println("ERROR: " + message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
        System.err.println("WARNING: " + message);
    }

    public static void logWithDivider(String message) {
        String divider = "=".repeat(message.length());
        String formattedMessage = String.format("\n%s\n%s\n%s", divider, message, divider);
        log(formattedMessage);
    }

    public static void logAction(String action) {
        logger.info("User action: {}", action);
        System.out.println("> " + action);
    }
}
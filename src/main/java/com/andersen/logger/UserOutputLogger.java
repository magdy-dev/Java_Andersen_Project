package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserOutputLogger {
    private static final Logger logger = LoggerFactory.getLogger("UserOutput");

    public static void log(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
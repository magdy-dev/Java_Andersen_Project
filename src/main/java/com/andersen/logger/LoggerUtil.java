package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class LoggerUtil {


    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}


package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loge {
    private static final Logger logger = LoggerFactory.getLogger("UserOutputLogger");

    public static void log(String message) {
        logger.info(message);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
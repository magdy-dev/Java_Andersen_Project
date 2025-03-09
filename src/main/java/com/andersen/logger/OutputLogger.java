package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputLogger {
    private static final Logger logger = LoggerFactory.getLogger(OutputLogger.class);

    public static void log(String message) {
        logger.info(message);
        System.out.println(message);
    }
}
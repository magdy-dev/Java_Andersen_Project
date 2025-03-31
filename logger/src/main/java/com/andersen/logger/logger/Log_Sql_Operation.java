package com.andersen.logger.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A utility class for logging SQL operation messages using SLF4J.
 * This class allows logging of informational messages and errors related
 * to SQL operations, outputting them both to the console and a logging framework.
 */
@Component
public class Log_Sql_Operation {

    private static final Logger logger = LoggerFactory.getLogger(Log_Sql_Operation.class);

    /**
     * Logs an informational message about an SQL operation.
     *
     * @param message the message to be logged
     */
    public static void log(String message) {
        logger.info(message);
        System.out.println(message);
    }

    /**
     * Logs an error message related to an SQL operation.
     *
     * @param message the error message to be logged
     */
    public static void error(String message) {
        logger.error(message);
        System.err.println("ERROR: " + message);
    }
}
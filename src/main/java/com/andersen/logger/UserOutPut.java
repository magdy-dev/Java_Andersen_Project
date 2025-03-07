package com.andersen.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserOutPut {
    private static final Logger logger = LoggerFactory.getLogger(UserOutPut.class);

    public static void log(String message) {
        logger.info(message);
        System.out.println(message);
    }
}
package com.andersen;

import com.andersen.config.RootConfig;
import com.andersen.controller.Application;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.getBeanFactory().registerSingleton("scanner", new Scanner(System.in));

        context.register(RootConfig.class);

        context.refresh();

        Application application = context.getBean(Application.class);

        try {
            application.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}

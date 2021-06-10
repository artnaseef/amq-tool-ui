package com.artnaseef.amqtool.ui;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private ClassPathXmlApplicationContext applicationContext;

    public static void main(String[] args) {
        Main instance = new Main();

        instance.instanceMain(args);
    }

    public void instanceMain(String[] args) {
        this.applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        this.applicationContext.start();

        // System.out.println("Hello World");
    }
}

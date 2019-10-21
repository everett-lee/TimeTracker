package com.timetracker.timetracker;

import com.coxautodev.graphql.tools.SchemaParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TimetrackerApplication {


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TimetrackerApplication.class, args);
//
//        ctx.getBean("GraphQLSchema");

    }
}

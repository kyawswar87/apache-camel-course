package com.spring.camel.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class SimpleExpression extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new SimpleExpression());
        main.run(args);
    }

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .log("${date:now:yyyy-MM-dd HH:mm:ss}")
                .to("mock:result");
    }
}

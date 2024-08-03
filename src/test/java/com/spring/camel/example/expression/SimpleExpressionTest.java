package com.spring.camel.example.expression;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class SimpleExpressionTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("${headers}")
                        .choice()
//                        .when().simple("${headers.total} > 1")
//                        .when(header("total").isGreaterThan(1000))
//                        .when(body().contains("Hello"))
                        .when().simple("${body} contains 'Hello'")
                        .transform().simple("Welcome")
                        .setHeader("isOver", simple("${header.total} > 100", boolean.class))
                        .log("${date:now:yyyy-MM-dd HH:mm:ss}")
                        .log("${body} ${headers}")
                        .otherwise()
                        .log("${body}")
                        .to("mock:result");
            }
        };
    }

    @Test
    public void testSimpleExpression() throws Exception {
        template.sendBodyAndHeader("direct:start", "Hello World", "total", "100");
    }
}

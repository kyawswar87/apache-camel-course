package com.spring.camel.example.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class CustomProcessorTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:custom")
                        .process(new MyProcessor())
                        .log("${body}")
                        .to("mock:out");
            }
        };
    }

    @Test
    public void testCustomProcessor() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:out");
        quote.expectedBodiesReceived("Hello World");
        quote.expectedMessageCount(1);

        template.sendBody("direct:custom", "Hello");

        quote.assertIsSatisfied();
    }
}

package com.spring.camel.example.exchange;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class MessageExchange extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:startRequestReply")
                .setExchangePattern(ExchangePattern.InOnly) // Set the MEP to InOut
                .log("Received request: ${body}")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    // Process the request and set the reply message
                    exchange.getMessage().setBody("Processed: " + body);
                })
                .log("Sending reply: ${body}");
    }

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new MessageExchange());
        context.start();

        // Send a test message and get the response
        String response = context.createProducerTemplate().requestBody("direct:startRequestReply", "Hello, Camel with Request-Reply!", String.class);
        System.out.println("Response: " + response);

        // Let the application run for a while
        Thread.sleep(3000);
        context.stop();
    }
}

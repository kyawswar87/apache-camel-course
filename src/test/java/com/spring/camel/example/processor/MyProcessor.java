package com.spring.camel.example.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);

        exchange.getIn().setBody(input.concat(" World"));
    }
}

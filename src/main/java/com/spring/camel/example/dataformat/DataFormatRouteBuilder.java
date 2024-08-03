package com.spring.camel.example.dataformat;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

public class DataFormatRouteBuilder extends RouteBuilder {

    public void configure() {
        BindyCsvDataFormat bindy = new BindyCsvDataFormat(Person.class);
        from("file:src/format/data?noop=true&fileName=data.csv")
                .unmarshal(bindy)
                .marshal().json()
                .log("JSON output: ${body}")
                .log("Converted object: ${body}"); // Log
    }

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new DataFormatRouteBuilder());
        context.start();
        Thread.sleep(3000); // Keep the application running for a while to process the file
        context.stop();
    }
}

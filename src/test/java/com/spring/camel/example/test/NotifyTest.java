package com.spring.camel.example.test;

import com.spring.camel.example.FileRouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotifyTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new FileRouteBuilder();
    }

    @Test
    public void notifyTest() {
        NotifyBuilder notify = new NotifyBuilder(context)
                .from("file:src/data?noop=true")
                .whenDone(2).create();
        template.sendBodyAndHeader("file://src/data", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<person user=\"james\">\n" +
                        "  <firstName>James</firstName>\n" +
                        "  <lastName>Strachan</lastName>\n" +
                        "  <city>London</city>\n" +
                        "</person>",
                Exchange.FILE_NAME, "hello_1.xml");
        template.sendBodyAndHeader("file://src/data", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<person user=\"james\">\n" +
                        "  <firstName>James</firstName>\n" +
                        "  <lastName>Strachan</lastName>\n" +
                        "  <city>other</city>\n" +
                        "</person>",
                Exchange.FILE_NAME, "hello_2.xml");

        boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);
    }
}

package com.spring.camel.example;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.apache.camel.language.xpath.XPathBuilder.xpath;

public class MyRouterBuilderTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
//        return new MyRouteBuilder();
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:src/data?noop=true")
                        .choice()
                        .when(xpath("/person/city = 'London'"))
                        .log("UK message")
                        .to("mock:uk")
                        .otherwise()
                        .log("Other message")
                        .to("mock:others");
            }
        };
    }

    @Test
    public void fileFilter() throws InterruptedException {

        template.sendBodyAndHeader("file://src/data", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<person user=\"james\">\n" +
                        "  <firstName>James</firstName>\n" +
                        "  <lastName>Strachan</lastName>\n" +
                        "  <city>London</city>\n" +
                        "</person>",
                Exchange.FILE_NAME, "hello_uk.xml");
        Thread.sleep(2000);
    }

    @Test
    public void testMock() throws InterruptedException {
        MockEndpoint quote = getMockEndpoint("mock:others");
        quote.expectedMessageCount(1);

        MockEndpoint quote2 = getMockEndpoint("mock:uk");
        quote2.expectedMessageCount(1);

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
        quote.assertIsSatisfied();
        quote2.assertIsSatisfied();
    }
}

package com.spring.camel.example.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class AggregateBuilderTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:in")
                        .log("${headers}")
                        .aggregate(header("correlationId"),new NumberAggregationStrategy())
                        .completionPredicate(simple("${body} contains 'STOP'"))
                        .transform(simple("The result is: ${body}"))
                        .to("mock:out");
            }
        };
    }

    @Test
    public void testAggregateExpression() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:out");
        quote.expectedBodiesReceived("The result is: 6 STOP");
        quote.expectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "1", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "2", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "3", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "STOP", "correlationId", "1");

        quote.assertIsSatisfied();
    }

    @Test
    public void testAggregateExpressionWithCorrelationId() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:out");
        quote.expectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "1", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "2", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "3", "correlationId", "1");
        template.sendBodyAndHeader("direct:in", "STOP", "correlationId", "1");

        template.sendBodyAndHeader("direct:in", "3", "correlationId", "2");
        template.sendBodyAndHeader("direct:in", "STOP", "correlationId", "2");
        quote.assertIsSatisfied();


    }
}

package com.spring.camel.example.rest;

import org.apache.camel.BeanInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class RestDslBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .contextPath("api")
                .component("servlet")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");
        rest("/orders")
                // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
                .get("{id}").outType(Order.class)
                .to("bean:orderService?method=getOrder(${header.id})")
                // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
                .post().type(Order.class)
                .to("bean:orderService?method=createOrder")
                // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
                .put().type(Order.class)
                .to("bean:orderService?method=updateOrder")
                .delete("{id}")
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}

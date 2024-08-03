package com.spring.camel.example.rest;

import org.apache.camel.spi.annotations.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component("orderService")
public class OrderServiceImpl implements OrderService{

    private Map<Integer, Order> orders = new HashMap<>();

    private final AtomicInteger idGen = new AtomicInteger();

    public OrderServiceImpl() {
        // setup some dummy orders to start with
        setupDummyOrders();
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public void updateOrder(Order order) {
        int id = order.getId();
        orders.remove(id);
        orders.put(id, order);
    }

    public String createOrder(Order order) {
        int id = idGen.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        return "" + id;
    }

    public void cancelOrder(int orderId) {
        orders.remove(orderId);
    }

    public void setupDummyOrders() {
        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");
        createOrder(order);

        order = new Order();
        order.setAmount(3);
        order.setPartName("brake");
        order.setCustomerName("toyota");
        createOrder(order);
    }
}

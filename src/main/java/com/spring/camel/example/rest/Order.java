package com.spring.camel.example.rest;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private String partName;
    private int amount;
    private String customerName;

}

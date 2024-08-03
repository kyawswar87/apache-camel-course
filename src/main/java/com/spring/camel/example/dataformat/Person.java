package com.spring.camel.example.dataformat;

import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Data
@CsvRecord(separator = ",", skipFirstLine = true)
public class Person {
    @DataField(pos = 1)
    private String id;
    @DataField(pos = 2)
    private String name;
    @DataField(pos = 3)
    private int age;
}

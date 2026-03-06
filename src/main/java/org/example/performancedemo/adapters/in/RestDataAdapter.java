package org.example.performancedemo.adapters.in;

import org.example.performancedemo.ports.DataSourcePort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RestDataAdapter implements DataSourcePort {

    @Override
    public List<Integer> getValues() {
        return Arrays.asList(5, 15, 25);
    }
}
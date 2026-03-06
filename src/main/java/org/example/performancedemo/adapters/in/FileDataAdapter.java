package org.example.performancedemo.adapters.in;

import org.example.performancedemo.ports.DataSourcePort;

import java.util.Arrays;
import java.util.List;

public class FileDataAdapter implements DataSourcePort {

    @Override
    public List<Integer> getValues() {
        return Arrays.asList(10, 20, 30, 40);
    }
}
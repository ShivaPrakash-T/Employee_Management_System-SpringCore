package com.employee.utility_layer;

import java.util.UUID;

public class EmployeeIdGenerator {

    public String generateTrackingId() {
        return "EMP-TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
package com.employee.service;

import com.employee.entity.Employee;
import com.employee.notification.NotificationService;
import com.employee.repository.EmployeeRepository;
import com.employee.utility_layer.EmployeeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("singleton") // Demonstrates singleton scope (default)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final NotificationService defaultNotificationService;
    private final NotificationService smsNotificationService;
    private final EmployeeIdGenerator employeeIdGenerator;

    @Value("${notification.default.channel:Email}")
    private String defaultChannel;

    // Constructor-based Dependency Injection
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           NotificationService defaultNotificationService,
                           @Qualifier("smsNotification") NotificationService smsNotificationService,
                           EmployeeIdGenerator employeeIdGenerator) {
        this.employeeRepository = employeeRepository;
        this.defaultNotificationService = defaultNotificationService;
        this.smsNotificationService = smsNotificationService;
        this.employeeIdGenerator = employeeIdGenerator;
    }

    @Transactional
    public void registerEmployee(Employee employee) {
        // Business Validation
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty.");
        }
        if (employee.getSalary() == null || employee.getSalary() < 0) {
            throw new IllegalArgumentException("Invalid salary amount.");
        }

        // Persist via Repository
        employeeRepository.save(employee);

        // Generate tracking ID using custom bean
        String trackingId = employeeIdGenerator.generateTrackingId();
        System.out.println("Generated Tracking ID: " + trackingId);

        // Trigger default notification (@Primary -> Email)
        defaultNotificationService.sendNotification(
                employee.getEmail(),
                "Welcome to the organization! Your registration is complete."
        );
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void updateEmployee(Long id, String newDepartment, Double newSalary) {
        Employee employee = employeeRepository.findById(id);
        if (employee != null) {
            employee.setDepartment(newDepartment);
            employee.setSalary(newSalary);
            employeeRepository.update(employee);

            // Send SMS notification using explicit @Qualifier bean injection
            smsNotificationService.sendNotification(
                    employee.getEmail(),
                    "Your employee profile has been updated."
            );
        } else {
            System.out.println("Employee with ID " + id + " not found for update.");
        }
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.delete(id);
        System.out.println("Employee record removed successfully.");
    }
}

package com.employee;


import com.employee.AppConfig;
import com.employee.entity.Employee;
import com.employee.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Spring IoC Container without Spring Boot
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. Retrieve EmployeeService bean from the container
        EmployeeService employeeService = context.getBean(EmployeeService.class);
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=============================================");
            System.out.println("   EMPLOYEE MANAGEMENT SYSTEM (SPRING CORE)  ");
            System.out.println("=============================================");
            System.out.println("1. Register New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Find Employee by ID");
            System.out.println("4. Update Employee Details");
            System.out.println("5. Delete Employee Record");
            System.out.println("6. Exit ");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = scanner.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = scanner.nextDouble();

                    try {
                        Employee newEmp = new Employee(name, email, dept, salary);
                        employeeService.registerEmployee(newEmp);
                        System.out.println("Employee registered successfully!");
                    } catch (Exception e) {
                        System.out.println("Error registering employee: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("\n--- Registered Employees ---");
                    var employees = employeeService.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employee records found.");
                    } else {
                        employees.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.print("Enter Employee ID: ");
                    Long findId = scanner.nextLong();
                    Employee emp = employeeService.getEmployeeById(findId);
                    if (emp != null) {
                        System.out.println(emp);
                    } else {
                        System.out.println("Employee with ID " + findId + " not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Employee ID to update: ");
                    Long updateId = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Enter New Department: ");
                    String newDept = scanner.nextLine();
                    System.out.print("Enter New Salary: ");
                    double newSalary = scanner.nextDouble();

                    employeeService.updateEmployee(updateId, newDept, newSalary);
                    break;

                case 5:
                    System.out.print("Enter Employee ID to delete: ");
                    Long deleteId = scanner.nextLong();
                    employeeService.deleteEmployee(deleteId);
                    break;

                case 6:
                    System.out.println("Exiting application. Closing Spring Container...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        // 3. Gracefully close container to trigger @PreDestroy lifecycle hooks
        context.close();
        scanner.close();
    }
}

package com.employee.repository;

import com.employee.entity.Employee;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepositoryImp implements EmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        System.out.println(">> [Lifecycle] EmployeeRepositoryImpl bean initialized.");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println(">> [Lifecycle] EmployeeRepositoryImpl bean is being destroyed.");
    }

    @Override
    public void save(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    public Employee findById(Long id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public List<Employee> findAll() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public void update(Employee employee) {
        entityManager.merge(employee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = findById(id);
        if (employee != null) {
            entityManager.remove(employee);
        }
    }
}

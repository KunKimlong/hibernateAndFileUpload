package edu.etec.hibernateemployee.service;

import edu.etec.hibernateemployee.model.entities.Employee;
import edu.etec.hibernateemployee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}

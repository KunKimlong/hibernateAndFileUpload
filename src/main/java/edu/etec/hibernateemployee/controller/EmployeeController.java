package edu.etec.hibernateemployee.controller;

import edu.etec.hibernateemployee.model.entities.Employee;
import edu.etec.hibernateemployee.model.request.EmployeeRequest;
import edu.etec.hibernateemployee.model.response.EmployeeResponse;
import edu.etec.hibernateemployee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Random;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponse> addEmployee(@ModelAttribute EmployeeRequest employeeRequest) {
        String path_dir = "public/Profile/";
        EmployeeResponse employeeResponse;
        try{
            Random rand = new Random();
            String fileName = rand.nextInt(999999)+"_"+employeeRequest.getProfile().getOriginalFilename();
            Files.copy(employeeRequest.getProfile().getInputStream(), Paths.get(path_dir + fileName));
            Employee emp = new Employee();
            emp.setName(employeeRequest.getName());
            emp.setGender(employeeRequest.getGender());
            emp.setSalary(employeeRequest.getSalary());
            emp.setProfile(fileName);
            employeeService.addEmployee(emp);

        }catch (Exception e){
            e.printStackTrace();
            employeeResponse = EmployeeResponse.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.valueOf(400))
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .build();
            return new ResponseEntity<>(employeeResponse, HttpStatusCode.valueOf(400));
        }

        employeeResponse = EmployeeResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status(HttpStatus.CREATED)
                .message("add Success")
                    .build();
        return new ResponseEntity<>(employeeResponse, HttpStatusCode.valueOf(201));
    }
}

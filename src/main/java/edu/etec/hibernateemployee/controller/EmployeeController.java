package edu.etec.hibernateemployee.controller;

import edu.etec.hibernateemployee.model.entities.Employee;
import edu.etec.hibernateemployee.model.request.EmployeeRequest;
import edu.etec.hibernateemployee.model.response.EmployeeResponse;
import edu.etec.hibernateemployee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.hibernate.boot.archive.scan.internal.ScanResultImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    private String getPublicProfile(String profileName){
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return url+"/Profile/"+profileName;
    }

    @GetMapping("/")
    public ResponseEntity<EmployeeResponse<List<Employee>>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee employee : employees) {
            employee.setProfile(getPublicProfile(employee.getProfile()));
        }
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .message("GET ALL EMPLOYEES")
                .status(HttpStatus.OK)
                .payload(employees)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return  new ResponseEntity<>(employeeResponse, HttpStatus.OK);
    }
    // localhost:8080/Profile/imagename..

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse<Employee>> getEmployeeById(@PathVariable int id) {
       try{
           Employee employee = employeeService.getEmployeeById(id);
           employee.setProfile(getPublicProfile(employee.getProfile()));
           EmployeeResponse employeeResponse = EmployeeResponse.builder()
                   .message("Get successfully")
                   .status(HttpStatus.OK)
                   .timestamp(new Timestamp(System.currentTimeMillis()))
                   .payload(employee)
                   .build();
           return new ResponseEntity<>(employeeResponse,HttpStatus.OK);
       }
       catch (Exception ignored){
           EmployeeResponse employeeResponse = EmployeeResponse.builder()
                   .message("Not found")
                   .status(HttpStatus.NOT_FOUND)
                   .timestamp(new Timestamp(System.currentTimeMillis()))
                   .build();
           return new ResponseEntity<>(employeeResponse,HttpStatus.NOT_FOUND);
       }
    }


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

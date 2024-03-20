package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Exception.ResouceNotFoundException;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.model.Employee;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	//get all employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}
	
	//create employee
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee emp) {
		return employeeRepository.save(emp);
	}
	
	//getEmployee by Id
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		 Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Employee not exist with id:" +id));
		 return ResponseEntity.ok(employee);
	}
	
	//UpdateEmployee
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee emp, @PathVariable Long id)
	{
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Employee not exist with id:" +id));
		employee.setFirstName(emp.getFirstName());
		employee.setLastName(emp.getLastName());
		employee.setEmailId(emp.getEmailId());
		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	//Delete Employee
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<HashMap<String, Boolean>> deleteEmployee(@PathVariable Long id){
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Employee not exist with id:" +id));
		employeeRepository.delete(employee);
		HashMap<String, Boolean> response = new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
		
	}
	//Search Employee
	@GetMapping("/employees/search")
	public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String query) {
        String queryLower = query.toLowerCase();

        List<Employee> employees = employeeRepository.findAll();

        // Filter employees by first name or last name containing the lowercase query string
        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> employee.getFirstName().toLowerCase().contains(queryLower)
                        || employee.getLastName().toLowerCase().contains(queryLower))
                .collect(Collectors.toList());

        if (filteredEmployees.isEmpty()) {
            throw new ResouceNotFoundException("No employees found with the search query: " + query);
        }

        return ResponseEntity.ok(filteredEmployees);
    }
}

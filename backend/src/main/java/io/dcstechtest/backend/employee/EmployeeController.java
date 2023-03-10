package io.dcstechtest.backend.employee;


import java.util.List;
import java.util.Optional;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
	
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService service;
	
	@GetMapping
	public ResponseEntity<List<Employee>> getAll() {
		List<Employee> allEmployees = this.service.getAll();
		logger.info("Employees retrieved successfully");
		return new ResponseEntity<>(allEmployees, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getById(@PathVariable Long id) {
		Optional<Employee> maybeEmployee = this.service.getById(id);
		if (maybeEmployee.isEmpty()) {
			logger.error("Employee by id" + id + " was not found.");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		logger.info("Employee of id" + id + " successfully found.");
		return new ResponseEntity<>(maybeEmployee.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Employee> create(@RequestBody EmployeeCreateDTO data) {
		Employee createdEmployee = this.service.create(data);
		return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Employee> delete(@PathVariable Long id) {
		boolean isDeleted = this.service.delete(id);
		
		if (isDeleted) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id,
            @RequestBody EmployeeUpdateDTO data) {

        Optional<Employee> maybeEmployee = this.service.getById(id);
        if (maybeEmployee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No job found with id: " + id);
        }

        Employee employee = maybeEmployee.get();

        Employee updatedEmployee = this.service.update(id, data, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }
}


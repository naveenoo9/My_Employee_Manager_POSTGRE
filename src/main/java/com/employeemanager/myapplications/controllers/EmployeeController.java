package com.employeemanager.myapplications.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.employeemanager.myapplications.exceptions.ResourceNotFoundException;
import com.employeemanager.myapplications.model.Employee;
import com.employeemanager.myapplications.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/")
public class EmployeeController {
	
	private @Autowired EmployeeRepository employeeRepository;
	
	// Get list of All Employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployees()	{
		List<Employee> listOfEmployees = this.employeeRepository.findAll();
		if(listOfEmployees.isEmpty())
		{
			throw new ResourceNotFoundException("There are no records yet to display.");
		} else {
			return listOfEmployees;
		}
	}
	
	// Get employee by ID
	@GetMapping(value = "/employees/{empId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "empId") Long empId)	{
		
		Employee employee = this.employeeRepository.findById(empId).orElseThrow(
				() -> new ResourceNotFoundException("No records found with employee id: " + empId));
		return ResponseEntity.ok().body(employee);
	}
	
	// Save or Add an employee
	@PostMapping(value = "/employees")
	public ResponseEntity<Employee> createEmployee(@RequestBody(required = true) Employee employee) {
		
		Employee employeeCreated = this.employeeRepository.save(employee);
		return ResponseEntity.ok(employeeCreated);
	}
	
	// Update an Employee
	@PutMapping(value = "/employees/{empId}")
	public ResponseEntity<Employee> updateEmployeeById(@PathVariable(value = "empId") Long empId, 
			 @RequestBody(required = true) Employee employee)	{
				
		Employee existingEmployeeDetails = this.employeeRepository.findById(empId).orElseThrow(
						()	->	new ResourceNotFoundException("No records found with employee id: " + empId + " to update. "));
		//existingEmployeeDetails = new Employee(employee.getFirstName(), employee.getLastName(), employee.getEmail());
		//System.out.println(existingEmployeeDetails);
		existingEmployeeDetails.setFirstName(employee.getFirstName());
		existingEmployeeDetails.setLastName(employee.getLastName());
		existingEmployeeDetails.setEmail(employee.getEmail());
		//System.out.println(existingEmployeeDetails);
		Employee updatedEmployee = this.employeeRepository.save(existingEmployeeDetails);
		//System.out.println(updatedEmployee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	// Delete employee
	@DeleteMapping(value = "/employees/{empId}")
	public Map<String,Boolean> deleteEmployeeById(@PathVariable(value = "empId") long empId)	{
		
		Employee employee = this.employeeRepository.findById(empId).orElseThrow(
				()	->	new ResourceNotFoundException("No records found with employee id: " + empId + " to delete. "));
		this.employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted employee with id: " + empId, Boolean.TRUE);
		return response;
	}
}

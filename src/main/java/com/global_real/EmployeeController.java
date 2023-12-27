package com.global_real;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"/employees"})
public class EmployeeController {
   @Autowired
   private EmployeeService employeeService;

   @PostMapping
   public ResponseEntity<UUID> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
      UUID employeeId = this.employeeService.addEmployee(employeeDTO);
      return new ResponseEntity<UUID>(employeeId, HttpStatus.CREATED);
   }

   @GetMapping
   public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String sortBy) {
      List<EmployeeDTO> employees = this.employeeService.getAllEmployees(page, size, sortBy);
      return new ResponseEntity<List<EmployeeDTO>>(employees, HttpStatus.OK);
   }

   @DeleteMapping({"/{employeeId}"})
   public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
      this.employeeService.deleteEmployee(employeeId);
      return new ResponseEntity<Void>(HttpStatus.OK);
   }

   @PutMapping({"/{employeeId}"})
   public ResponseEntity<Void> updateEmployee(@PathVariable UUID employeeId, @RequestBody EmployeeDTO employeeDTO) {
      this.employeeService.updateEmployee(employeeId, employeeDTO);
      return new ResponseEntity<Void>(HttpStatus.OK);
   }

   @GetMapping({"/manager/{employeeId}/{level}"})
   public ResponseEntity<EmployeeDTO> getNthLevelManager(@PathVariable UUID employeeId, @PathVariable int level) {
      EmployeeDTO manager = this.employeeService.getNthLevelManager(employeeId, level);
      return new ResponseEntity<EmployeeDTO>(manager, HttpStatus.OK);
   }
}

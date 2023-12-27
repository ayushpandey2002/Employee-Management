package com.global_real;


import java.util.List;
import java.util.UUID;

public interface EmployeeService {
   UUID addEmployee(EmployeeDTO employeeDTO);

   List<EmployeeDTO> getAllEmployees(Integer page, Integer size, String sortBy);

   void deleteEmployee(UUID employeeId);

   void updateEmployee(UUID employeeId, EmployeeDTO employeeDTO);

   EmployeeDTO getNthLevelManager(UUID employeeId, int level);
}

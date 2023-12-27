package com.global_real;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmployeeServiceImpl implements EmployeeService {
   @Autowired
   private EmployeeRepo employeeRepo;
   @Autowired
   private JavaMailSender javaMailSender;
   
   @Value("${mail.sender}")
   private String mailSender;

   public UUID addEmployee(EmployeeDTO employeeDTO) {
      UUID employeeId = UUID.randomUUID();
      EmployeeModel employee = EmployeeModel.createEmployee(
    		  employeeDTO.getEmployeeName(),
    		  employeeDTO.getPhoneNumber(), 
    		  employeeDTO.getEmail(), 
    		  employeeDTO.getReportsTo(), 
    		  employeeDTO.getProfileImage());
      employee.setId(employeeId);
      this.sendEmailToLevel1Manager(employeeDTO);
      this.employeeRepo.save(employee);
      return (employeeId);
   }

   private void sendEmailToLevel1Manager(EmployeeDTO employeeDTO) {
      try {
         int managerLevel = 1;
         EmployeeDTO manager = this.getNthLevelManager(UUID.fromString(employeeDTO.getReportsTo()), managerLevel);
         if (manager == null) {
            throw new IllegalArgumentException("Level 1 Manager not found for employee with ID " + String.valueOf(employeeDTO.getId()));
         }

         String subject = "New Employee Added";
         String message = String.format("%s will now work under you. Mobile number is %s and email is %s",
        		 employeeDTO.getEmployeeName(), 
        		 employeeDTO.getPhoneNumber(), 
        		 employeeDTO.getEmail());
         this.sendEmail(manager.getEmail(), subject, message);
      } catch (MessagingException var6) {
         var6.printStackTrace();
      }

   }

   private void sendEmail(String to, String subject, String body) throws MessagingException {
      MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
      helper.setFrom("sample404user@gmail.com");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);
      this.javaMailSender.send(mimeMessage);
   }

   public List<EmployeeDTO> getAllEmployees(Integer page, Integer size, String sortBy) {
      if (page == null && size == null && sortBy == null) {
         List<EmployeeModel> employees = this.employeeRepo.findAll();
         List<EmployeeDTO> employeeDTOs = (List<EmployeeDTO>)employees.stream().map(this::convertToDTO).collect(Collectors.toList());
         return employeeDTOs;
      } else {
         PageRequest pageable = PageRequest.of(page, size, Sort.by(new String[]{sortBy}));
         Page<EmployeeModel> employeesPage = this.employeeRepo.findAll(pageable);
         Page<EmployeeDTO> employeeDTOPage = employeesPage.map(this::convertToDTO);
         List<EmployeeDTO> actualList = employeeDTOPage.getContent();
         return actualList;
      }
   }

   private EmployeeDTO convertToDTO(EmployeeModel employee) {
      EmployeeDTO employeeDTO = new EmployeeDTO();
      employeeDTO.setId(employee.getId());
      employeeDTO.setEmployeeName(employee.getEmployeeName());
      employeeDTO.setPhoneNumber(employee.getPhoneNumber());
      employeeDTO.setEmail(employee.getEmail());
      employeeDTO.setReportsTo(employee.getReportsTo());
      employeeDTO.setProfileImage(employee.getProfileImage());
      return employeeDTO;
   }

   public void deleteEmployee(UUID employeeId) {
      Optional<EmployeeModel> optionalEmployee = this.employeeRepo.findById(employeeId.toString());
      if (optionalEmployee.isPresent()) {
         this.employeeRepo.deleteById(employeeId.toString());
      } else {
         throw new IllegalArgumentException("Employee with ID " + String.valueOf(employeeId) + " not found");
      }
   }

   public void updateEmployee(UUID employeeId, EmployeeDTO employeeDTO) {
      Optional<EmployeeModel> optionalEmployee = this.employeeRepo.findById(employeeId.toString());
      if (optionalEmployee.isPresent()) {
         EmployeeModel existingEmployee = (EmployeeModel)optionalEmployee.get();
         this.updateEmployeeDetails(existingEmployee, employeeDTO);
         this.employeeRepo.save(existingEmployee);
      } else {
         throw new IllegalArgumentException("Employee with ID " + String.valueOf(employeeId) + " not found");
      }
   }

   private void updateEmployeeDetails(EmployeeModel existingEmployee, EmployeeDTO employeeDTO) {
      BeanUtils.copyProperties(employeeDTO, existingEmployee, this.getNullPropertyNames(employeeDTO));
   }

   private String[] getNullPropertyNames(Object source) {
      BeanInfo beanInfo;
      try {
         beanInfo = Introspector.getBeanInfo(source.getClass());
      } catch (IntrospectionException var12) {
         throw new IllegalArgumentException(var12);
      }

      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      Set<String> emptyNames = new HashSet<String>();
      PropertyDescriptor[] var8 = propertyDescriptors;
      int var7 = propertyDescriptors.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         PropertyDescriptor propertyDescriptor = var8[var6];

         Object value;
         try {
            value = propertyDescriptor.getReadMethod().invoke(source);
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException var11) {
            throw new IllegalArgumentException(var11);
         }

         if (value == null) {
            emptyNames.add(propertyDescriptor.getName());
         }
      }

      String[] result = new String[emptyNames.size()];
      return (String[])emptyNames.toArray(result);
   }

   public EmployeeDTO getNthLevelManager(UUID employeeId, int level) {
      if (level < 1) {
         throw new IllegalArgumentException("Level must be greater than or equal to 1");
      } else {
         return this.findNthLevelManager(employeeId.toString(), level);
      }
   }

   private EmployeeDTO findNthLevelManager(String employeeId, int level) {
      Optional<EmployeeModel> optionalEmployee = this.employeeRepo.findById(employeeId);
      if (optionalEmployee.isPresent()) {
         EmployeeModel employee = (EmployeeModel)optionalEmployee.get();
         if (level == 1) {
            return this.convertToDTO(employee);
         } else {
            String managerId = employee.getReportsTo();
            if (managerId != null) {
               return this.findNthLevelManager(managerId, level - 1);
            } else {
               throw new IllegalArgumentException("Manager not found for employee with ID " + employeeId);
            }
         }
      } else {
         throw new IllegalArgumentException("Employee with ID " + employeeId + " not found");
      }
   }
}
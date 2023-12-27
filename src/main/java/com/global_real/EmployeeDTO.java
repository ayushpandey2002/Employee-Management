package com.global_real;

import java.util.UUID;

public class EmployeeDTO {
   private UUID id;
   private String employeeName;
   private String phoneNumber;
   private String email;
   private String reportsTo;
   private String profileImage;

   public UUID getId() {
      return this.id;
   }

   public void setId(UUID id) {
      this.id = id;
   }

   public String getEmployeeName() {
      return this.employeeName;
   }

   public void setEmployeeName(String employeeName) {
      this.employeeName = employeeName;
   }

   public String getPhoneNumber() {
      return this.phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getReportsTo() {
      return this.reportsTo;
   }

   public void setReportsTo(String reportsTo) {
      this.reportsTo = reportsTo;
   }

   public String getProfileImage() {
      return this.profileImage;
   }

   public void setProfileImage(String profileImage) {
      this.profileImage = profileImage;
   }
}

README FILE FOR BACKEND ASSESSMENT (Global_Assign).
*******************************************************************************************

HOW TO RUN PROJECT:
*******************

1. Extract the Global_Assign-master zip file in your local storage.
2. Open the folder using any IDE ( I used SpringToolSuite ).
3. Run (Update Maven Project) to configure all dependencies.
4. Replace the credentials in application.properties file:
	-Replace couchbase username, password, bucket-name with your CouchDB credentials.
	-Replace spring mail username, password with your sender's mail.
5. Run the project as Spring Boot App.

API DOCUMENTATIONS FOR ROUTES
*****************************

Note that base url is (http://localhost:8080/)
-------------------------------------------------------------------------------------------

1.POST(/employees) for addEmployee with JSON body-
	{
	  "employeeName"
	  "phoneNumber"
	  "email"
	  "reportsTo"
  	  "profileImage"
	}

returns a unique UUID for each created user and mails an email to the manager.
------------------------------------------------------------------------------------------

2.GET(/employees?page={page}&size={size}&sortBy={sortBy}) for getALLEmployees from database
	with use of params (page, size, sortBy).

Returns List of employees based on page, size, sortBy.
------------------------------------------------------------------------------------------

3. DELETE(/employees/emp_Id) for deleteEmployee with id=emp_Id from database.

------------------------------------------------------------------------------------------

4. PUT(/employees/emp_Id) for updateEmployee, to update user with id=emp_Id.
 JSON Body
	{
	  "employeeName"
	  "phoneNumber"
	  "email"
	  "reportsTo"
  	  "profileImage"
	}
-------------------------------------------------------------------------------------------

5. GET(/employees/manager/emp_Id/Nth_Level) to fetch manager of Nth level for a employee
  with id=emp_Id.

Returns UUID of the Nth manager.

*******************************************************************************************


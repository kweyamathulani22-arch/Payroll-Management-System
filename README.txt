PAYROLL MANAGEMENT SYSTEM

1. PROJECT OVERVIEW

The Payroll Management System is a Java Swing desktop application
designed to help small businesses manage employee information and
process monthly salaries. It calculates normal pay, overtime, bonuses,
deductions, gross pay and net pay. It also stores payroll records,
tracks payment status and generates payslips.

Each registered user has a separate company space. Users cannot view or
manage another user’s company records.

2. MAIN FEATURES

-   Create an account and log in.
-   Add, search, update and manage employee records.
-   Calculate and save employee payroll.
-   Add bonuses and deductions.
-   Mark payroll as Pending or Paid.
-   Generate, view, print and save payslips as PDF.
-   View dashboard totals and payroll reports.
-   Filter, sort and search dashboard records.
-   Export employee and payroll records to CSV.
-   Set company details and payroll calculation settings.

3. TECHNOLOGIES AND REQUIREMENTS

-   Java JDK 17 or newer (JDK 8 or newer may also compile the project).
-   Apache NetBeans (Ant project).
-   MySQL Server 8.x and MySQL Workbench.
-   MySQL Connector/J.
-   JCalendar 1.4.

4. DATABASE SETUP

1.  Start MySQL Server and open MySQL Workbench.
2.  Connect to MySQL as root.
3.  Open the SQL script: database/payroll_management_system.sql
4.  Run the complete script.

The script creates the payroll_management_system database, its tables,
views and application database user. It creates an empty database
without sample accounts or sample records.

WARNING: Running the SQL script again deletes the existing accounts and
data. Only run it again if you intend to reset the system.

5. PROJECT SETUP AND RUNNING

1.  Open the project in Apache NetBeans.
2.  Right-click the project and select Properties > Libraries >
    Classpath.
3.  Add the MySQL Connector/J JAR file and jcalendar-1.4.jar.
4.  Check the database connection settings in db.properties.
5.  Make sure the database name, username and password match your MySQL
    setup.
6.  Run the project using F6.

The main class is payrollSystem.Main. The Login screen should open.

6. FIRST USE

1.  Select Sign Up and create an account.
2.  Log in using your email address and password.
3.  Open Settings and enter the company name and address.
4.  Open Employees and add employee records.
5.  Use Payroll Processing to calculate and save payroll.

To process payroll, search for an employee, select the month and year,
enter normal and overtime hours, and add any deductions or bonus. Select
Calculate Payroll, check the summary, and then select Save Payroll. The
record starts as Pending. Select Pay Employee to change its status to
Paid. Generate Payslip to view or print the payslip.

Company name and address must be entered in Settings before a payslip
can be generated.

7. PAYROLL CALCULATION RULES

Normal Pay = Hourly Rate x Normal Hours Overtime Pay = Hourly Rate x
Overtime Multiplier x Overtime Hours Gross Pay = Normal Pay + Overtime
Pay + Bonus Total Deductions = Sum of all entered deductions Net Pay =
Gross Pay - Total Deductions

The default settings are 160 standard hours per month and an overtime
multiplier of 1.5. Normal and overtime hours combined cannot exceed 744
hours in one month. Deductions cannot be greater than gross pay.

A payroll record starts as Pending. Once it is marked Paid, it cannot be
changed, replaced or paid again. Bonuses can only be changed while the
payroll is Pending.

8. SECURITY AND DATA VALIDATION

-   Passwords are stored using a random salt and SHA-256 hashing.
-   Database queries use PreparedStatement.
-   Each user’s records are restricted using owner_id.
-   Users must be logged in to access screens other than Login and Sign
    Up.
-   Input is checked before records are saved.
-   Employee records are marked Not Active instead of being permanently
    deleted.

Examples of input rules: - Passwords must contain at least 8
characters. - Employee IDs must be 1 to 9 digits and cannot start with
zero. - Phone numbers must use the South African 0 or +27 format. - Post
codes must contain 4 digits. - Hourly rates must be greater than zero
and no more than 100 000. - Dates must be valid and the employee’s age
must be between 15 and 100.

9. TROUBLESHOOTING

-   MySQL connection error: Make sure MySQL Server is running.
-   Login/database access error: Check the username and password in
    db.properties.
-   Database not found: Run the SQL script in MySQL Workbench.
-   MySQL driver error: Add the Connector/J JAR to the NetBeans
    libraries.
-   Dashboard compilation error: Add the JCalendar JAR to the project
    libraries.
-   Payslip cannot be generated: Enter the company name and address in
    Settings.

10. KNOWN LIMITATIONS

-   Tax, pension, medical aid and other deductions must be entered
    manually.
-   The system does not include automatic tax tables.
-   There is only one account type; user roles are not available.
-   Password reset is not available in this version.
-   Settings save when Enter is pressed in a field or Home is clicked.
-   Payroll records and payment details are stored in the database;
    there is no separate text log file.


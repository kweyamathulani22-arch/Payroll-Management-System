PAYROLL MANAGEMENT SYSTEM - setup (3 steps)

1. DATABASE
   Open database/payroll_management_system.sql in MySQL Workbench
   (File > Open SQL Script) and press the lightning-bolt Execute button.
   It creates an EMPTY database (no demo accounts or data) and the application user.
   Run it as root (or an account allowed to CREATE USER).
   WARNING: running it again deletes every account and all data - this is also the way to wipe it clean.

2. MYSQL DRIVER (Connector/J)
   Download "MySQL Connector/J" (Platform Independent) from dev.mysql.com/downloads/connector/j
   NetBeans: right-click the project > Properties > Libraries > Classpath >
   Add JAR/Folder > choose mysql-connector-j-x.x.x.jar
   (the JCalendar jar used by the dashboard must stay in that list too)

3. RUN
   db.properties (project root folder) holds the connection details; the defaults match the SQL script.
   Run the project (F6). Click "Sign Up" to create the first account, log in,
   then open Settings and type your company name and address
   (they are printed at the top of every payslip).

Every user only sees their own employees, payroll, payslips, reports and settings.
Screens other than Login/Sign Up open the login window if nobody is logged in
(so running a single screen with Shift+F6 sends you to Login first).

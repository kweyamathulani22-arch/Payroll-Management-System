package payrollSystem;

import java.io.Serializable;
import java.math.BigDecimal;

// One row of the dashboard table: an employee and what happened to his/her pay in the chosen month.
// It implements Serializable (file streams and object serialization) and
// Comparable (the natural order is by name, like we did with Collections.sort).
public class PayrollEntry implements Serializable, Comparable<PayrollEntry> {

    // the three possible payment statuses
    public static final String PAID = "Paid";
    public static final String PENDING = "Pending";
    public static final String NOT_PROCESSED = "Not processed";   // the employee has no payroll for this month yet

    private static final long serialVersionUID = 1L;

    private final int employeeId;
    private final String name;
    private final String department;
    private final int payrollId;            // 0 when there is no payroll record for the month
    private final BigDecimal normalPay;     // shown as "Salary"
    private final BigDecimal overtimePay;
    private final BigDecimal bonusPay;      // shown as "Bonuses & Incentives"
    private final BigDecimal grossPay;
    private final BigDecimal deductions;
    private final BigDecimal netPay;
    private final String status;

    public PayrollEntry(int employeeId, String name, String department, int payrollId,
            BigDecimal normalPay, BigDecimal overtimePay, BigDecimal bonusPay,
            BigDecimal grossPay, BigDecimal deductions, BigDecimal netPay, String status) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.payrollId = payrollId;
        this.normalPay = normalPay;
        this.overtimePay = overtimePay;
        this.bonusPay = bonusPay;
        this.grossPay = grossPay;
        this.deductions = deductions;
        this.netPay = netPay;
        this.status = status;
    }

    // getters (the fields are private, this is encapsulation)
    public int getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getPayrollId() { return payrollId; }
    public BigDecimal getNormalPay() { return normalPay; }
    public BigDecimal getOvertimePay() { return overtimePay; }
    public BigDecimal getBonusPay() { return bonusPay; }
    public BigDecimal getGrossPay() { return grossPay; }
    public BigDecimal getDeductions() { return deductions; }
    public BigDecimal getNetPay() { return netPay; }
    public String getStatus() { return status; }

    // true if a payroll record exists for this employee in the chosen month
    public boolean hasPayroll() {
        return payrollId > 0;
    }

    // natural order: by name (ignoring capital letters), and by employee id if the names are the same
    @Override
    public int compareTo(PayrollEntry other) {
        int result = name.compareToIgnoreCase(other.name);
        if (result != 0) {
            return result;
        }
        return Integer.compare(employeeId, other.employeeId);
    }

    @Override
    public String toString() {
        return employeeId + " " + name + " (" + status + ")";
    }
}

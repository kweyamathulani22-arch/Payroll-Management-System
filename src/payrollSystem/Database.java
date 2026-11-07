package payrollSystem;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// This class does all the talking to the MySQL database, so the screens
// (JFrames) do not need to know how the connection works.
// IMPORTANT: every query uses currentUserId (the person who logged in),
// so one user can never see or change another user's information.
public class Database {

    // default connection details, they can be changed in the file db.properties
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/payroll_management_system"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "payroll_app";
    private static final String DEFAULT_PASSWORD = "Payroll@2026";

    private static String url = DEFAULT_URL;
    private static String user = DEFAULT_USER;
    private static String password = DEFAULT_PASSWORD;

    // who is logged in right now (0 means nobody)
    public static int currentUserId = 0;
    public static String currentUserName = "";

    // this block runs once when the class is loaded: it reads db.properties if the file exists
    static {
        File file = new File("db.properties");
        if (file.exists()) {
            Properties p = new Properties();
            try (FileInputStream in = new FileInputStream(file)) {
                p.load(in);
                url = p.getProperty("db.url", DEFAULT_URL).trim();
                user = p.getProperty("db.user", DEFAULT_USER).trim();
                password = p.getProperty("db.password", DEFAULT_PASSWORD);
            } catch (IOException ex) {
                System.err.println("Could not read db.properties, using the defaults: " + ex.getMessage());
            }
        }
    }

    // private constructor because this class only has static methods
    private Database() { }

    // opens a new connection to MySQL (the caller must close it, try-with-resources does that)
    public static Connection getConnection() throws SQLException {
        return java.sql.DriverManager.getConnection(url, user, password);
    }

    // Every screen except login and sign up calls this first.
    // If nobody is logged in, it sends the person to the login screen instead.
    public static boolean requireLogin(final JFrame screen) {
        if (currentUserId > 0) {
            return true;
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
                screen.dispose();
            }
        });
        return false;
    }

    // ------------------------------------------------------------ company settings

    // a small class to carry the settings around
    public static class Settings {
        public String companyName = "";
        public String companyAddress = "";
        public String currency = "ZAR - South African rand (R)";
        public BigDecimal hoursWeek = new BigDecimal("40");
        public BigDecimal multiplier = new BigDecimal("1.5");
        public BigDecimal hoursMonth = new BigDecimal("160");
    }

    // Reads the settings of the logged-in user. New users get a row with default values.
    public static Settings loadSettings() throws SQLException {
        Settings s = new Settings();
        String sql = "SELECT company_name, company_address, currency, standard_hours_week, "
                + "overtime_multiplier, default_hours_month FROM company_settings WHERE owner_id = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.companyName = rs.getString(1);
                    s.companyAddress = rs.getString(2);
                    s.currency = rs.getString(3);
                    s.hoursWeek = rs.getBigDecimal(4);
                    s.multiplier = rs.getBigDecimal(5);
                    s.hoursMonth = rs.getBigDecimal(6);
                } else {
                    // no row yet, so create one with the default values
                    try (PreparedStatement ins = con.prepareStatement(
                            "INSERT INTO company_settings (owner_id) VALUES (?)")) {
                        ins.setInt(1, currentUserId);
                        ins.executeUpdate();
                    }
                }
            }
        }
        Util.currencySymbol = currencySymbolOf(s.currency);
        return s;
    }

    // Saves the settings (insert the row if it is not there yet, otherwise update it)
    public static void saveSettings(Settings s) throws SQLException {
        String sql = "INSERT INTO company_settings (owner_id, company_name, company_address, currency, "
                + "standard_hours_week, overtime_multiplier, default_hours_month) VALUES (?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE company_name = VALUES(company_name), "
                + "company_address = VALUES(company_address), currency = VALUES(currency), "
                + "standard_hours_week = VALUES(standard_hours_week), "
                + "overtime_multiplier = VALUES(overtime_multiplier), "
                + "default_hours_month = VALUES(default_hours_month)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ps.setString(2, s.companyName);
            ps.setString(3, s.companyAddress);
            ps.setString(4, s.currency);
            ps.setBigDecimal(5, s.hoursWeek);
            ps.setBigDecimal(6, s.multiplier);
            ps.setBigDecimal(7, s.hoursMonth);
            ps.executeUpdate();
        }
        Util.currencySymbol = currencySymbolOf(s.currency);
    }

    // takes the symbol out of the brackets, e.g. "ZAR - South African rand (R)" gives "R"
    private static String currencySymbolOf(String currency) {
        Matcher m = Pattern.compile("\\(([^)]{1,4})\\)").matcher(currency);
        return m.find() ? m.group(1) : "R";
    }

    // ------------------------------------------------------------------- employees

    // a small class that holds one employee's main details
    public static class EmployeeInfo {
        public int id;
        public String name;
        public String department;
        public BigDecimal rate;
        public boolean active;

        // this is what shows in the "choose one" list
        @Override
        public String toString() {
            return id + " - " + name + " (" + department + ")";
        }
    }

    private static final String EMPLOYEE_SELECT =
            "SELECT e.employee_id, CONCAT(e.first_name, ' ', e.last_name), d.name, e.hourly_rate, e.is_active "
            + "FROM employees e JOIN departments d ON d.department_id = e.department_id "
            + "AND d.owner_id = e.owner_id ";

    // turns the current row of a ResultSet into an EmployeeInfo
    private static EmployeeInfo readEmployee(ResultSet rs) throws SQLException {
        EmployeeInfo e = new EmployeeInfo();
        e.id = rs.getInt(1);
        e.name = rs.getString(2);
        e.department = rs.getString(3);
        e.rate = rs.getBigDecimal(4);
        e.active = rs.getInt(5) == 1;
        return e;
    }

    // Finds one of MY employees from what the user typed: the exact employee number,
    // or part of the name. If more than one name matches the user picks from a list.
    // Returns null when nobody matches (or when the user cancels the list).
    public static EmployeeInfo chooseEmployee(Component parent, String text, boolean activeOnly) throws SQLException {
        text = text.trim();
        String activeFilter = activeOnly ? "AND e.is_active = 1 " : "";
        List<EmployeeInfo> found = new ArrayList<>();
        try (Connection con = getConnection()) {
            // first try the employee number
            if (text.matches("\\d{1,9}")) {
                try (PreparedStatement ps = con.prepareStatement(EMPLOYEE_SELECT
                        + "WHERE e.owner_id = ? AND e.employee_id = ? " + activeFilter)) {
                    ps.setInt(1, currentUserId);
                    ps.setInt(2, Integer.parseInt(text));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return readEmployee(rs);
                        }
                    }
                }
            }
            // then try the name
            String like = "%" + Util.escapeLike(text) + "%";
            try (PreparedStatement ps = con.prepareStatement(EMPLOYEE_SELECT
                    + "WHERE e.owner_id = ? AND CONCAT(e.first_name, ' ', e.last_name) LIKE ? " + activeFilter
                    + "ORDER BY e.first_name, e.last_name LIMIT 20")) {
                ps.setInt(1, currentUserId);
                ps.setString(2, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        found.add(readEmployee(rs));
                    }
                }
            }
        }
        if (found.isEmpty()) {
            return null;
        }
        if (found.size() == 1) {
            return found.get(0);
        }
        Object choice = JOptionPane.showInputDialog(parent, "Several employees match. Choose one:",
                "Choose employee", JOptionPane.QUESTION_MESSAGE, null, found.toArray(), found.get(0));
        return (EmployeeInfo) choice;
    }

    // Gives back the id of MY department with this name. If I do not have it yet, it is created.
    public static int findOrCreateDepartment(Connection con, String name) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT department_id FROM departments WHERE owner_id = ? AND name = ?")) {
            ps.setInt(1, currentUserId);
            ps.setString(2, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO departments (owner_id, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, currentUserId);
            ps.setString(2, name);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    // -------------------------------------------------------------------- payslips

    // Builds the payslip as an HTML page (the payslip window shows it and can print it).
    // The company name and address come from the Settings screen of the logged-in user.
    public static String buildPayslipHtml(int payslipId) throws SQLException {
        Settings s = loadSettings();
        StringBuilder html = new StringBuilder();
        try (Connection con = getConnection()) {
            int payrollId;
            String sql = "SELECT s.payslip_number, s.generated_at, v.* FROM payslips s "
                    + "JOIN v_payroll_detail v ON v.payroll_id = s.payroll_id AND v.owner_id = s.owner_id "
                    + "WHERE s.payslip_id = ? AND s.owner_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, payslipId);
                ps.setInt(2, currentUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return "<html><body><p>Payslip not found.</p></body></html>";
                    }
                    payrollId = rs.getInt("payroll_id");
                    String number = String.format("PS-%06d", rs.getInt("payslip_number"));
                    String issued = rs.getString("generated_at");
                    if (issued.length() > 10) {
                        issued = issued.substring(0, 10);   // keep only the date
                    }
                    BigDecimal mult = rs.getBigDecimal("overtime_multiplier");
                    BigDecimal otRate = rs.getBigDecimal("hourly_rate").multiply(mult);

                    html.append("<html><body style='font-family:SansSerif; font-size:12pt'>");

                    // 1. top banner with the company details (left) and the payslip number (right)
                    html.append("<table width='100%' cellspacing='0' cellpadding='12' bgcolor='#8B0000'><tr>");
                    html.append("<td><font color='#FFFFFF' size='5'><b>").append(Util.html(s.companyName)).append("</b></font><br>");
                    html.append("<font color='#FFFFFF' size='3'>").append(Util.html(s.companyAddress)).append("</font></td>");
                    html.append("<td align='right' valign='top'><font color='#FFFFFF' size='5'><b>PAYSLIP</b></font><br>");
                    html.append("<font color='#FFFFFF' size='3'>No. ").append(number).append("</font></td>");
                    html.append("</tr></table><br>");

                    // 2. employee details (left) and pay period details (right)
                    html.append("<table width='100%' cellspacing='0' cellpadding='4'>");
                    html.append("<tr><td width='50%'><b>EMPLOYEE DETAILS</b></td><td width='50%'><b>PAY DETAILS</b></td></tr>");
                    html.append("<tr><td>Name: ").append(Util.html(rs.getString("employee_name"))).append("</td>");
                    html.append("<td>Pay period: ").append(rs.getString("pay_period")).append("</td></tr>");
                    html.append("<tr><td>Employee ID: ").append(rs.getInt("employee_id")).append("</td>");
                    html.append("<td>Date issued: ").append(issued).append("</td></tr>");
                    html.append("<tr><td>Department: ").append(Util.html(rs.getString("department"))).append("</td>");
                    html.append("<td>Currency: ").append(Util.html(s.currency)).append("</td></tr>");
                    // payment status: Paid (with the date) or Pending
                    String paidAt = rs.getString("paid_at");
                    html.append("<tr><td>&nbsp;</td><td>Payment status: <b>").append(rs.getString("payment_status")).append("</b>");
                    if (paidAt != null) {
                        html.append(" (").append(paidAt.length() > 10 ? paidAt.substring(0, 10) : paidAt).append(")");
                    }
                    html.append("</td></tr>");
                    html.append("</table><br>");

                    // 3. earnings table
                    html.append("<table width='100%' cellspacing='0' cellpadding='6' border='1'>");
                    html.append("<tr bgcolor='#EEEEEE'><td><b>EARNINGS</b></td><td align='right'><b>Hours</b></td>");
                    html.append("<td align='right'><b>Rate</b></td><td align='right'><b>Amount</b></td></tr>");
                    html.append("<tr><td>Normal pay</td><td align='right'>").append(Util.plain(rs.getBigDecimal("normal_hours")));
                    html.append("</td><td align='right'>").append(Util.money(rs.getBigDecimal("hourly_rate")));
                    html.append("</td><td align='right'>").append(Util.money(rs.getBigDecimal("normal_pay"))).append("</td></tr>");
                    html.append("<tr><td>Overtime pay (x").append(Util.plain(mult)).append(")</td><td align='right'>");
                    html.append(Util.plain(rs.getBigDecimal("overtime_hours"))).append("</td><td align='right'>");
                    html.append(Util.money(otRate)).append("</td><td align='right'>");
                    html.append(Util.money(rs.getBigDecimal("overtime_pay"))).append("</td></tr>");
                    // a bonus row is only shown when there is a bonus
                    if (rs.getBigDecimal("bonus_pay").signum() > 0) {
                        String bonusNote = rs.getString("bonus_description");   // what the bonus is for, e.g. Birthday gift
                        html.append("<tr><td>Bonus &amp; incentives");
                        if (bonusNote != null && !bonusNote.isEmpty()) {
                            html.append(" (").append(Util.html(bonusNote)).append(")");
                        }
                        html.append("</td><td align='right'>-</td><td align='right'>-</td>");
                        html.append("<td align='right'>").append(Util.money(rs.getBigDecimal("bonus_pay"))).append("</td></tr>");
                    }
                    html.append("<tr bgcolor='#F7F7F7'><td colspan='3'><b>GROSS PAY</b></td><td align='right'><b>");
                    html.append(Util.money(rs.getBigDecimal("gross_pay"))).append("</b></td></tr>");
                    html.append("</table><br>");

                    // 4. deductions table (rows are added below, after this query is finished)
                    html.append("<table width='100%' cellspacing='0' cellpadding='6' border='1'>");
                    html.append("<tr bgcolor='#EEEEEE'><td><b>DEDUCTIONS</b></td><td align='right'><b>Amount</b></td></tr>");
                    html.append("@@DEDUCTION_ROWS@@");
                    html.append("<tr bgcolor='#F7F7F7'><td><b>TOTAL DEDUCTIONS</b></td><td align='right'><b>");
                    html.append(Util.money(rs.getBigDecimal("total_deductions"))).append("</b></td></tr>");
                    html.append("</table><br>");

                    // 5. net pay box
                    html.append("<table width='100%' cellspacing='0' cellpadding='10' bgcolor='#8B0000'><tr>");
                    html.append("<td><font color='#FFFFFF' size='5'><b>NET PAY</b></font></td>");
                    html.append("<td align='right'><font color='#FFFFFF' size='5'><b>");
                    html.append(Util.money(rs.getBigDecimal("net_pay"))).append("</b></font></td></tr></table><br>");

                    // 6. small footer
                    html.append("<font color='#666666' size='2'>This payslip was generated by the Payroll Management System ");
                    html.append("and is valid without a signature.</font>");
                    html.append("</body></html>");
                }
            }
            // now get the deductions of this payroll record and fill in the table rows
            StringBuilder rows = new StringBuilder();
            try (PreparedStatement ps = con.prepareStatement("SELECT description, amount FROM payroll_deductions "
                    + "WHERE payroll_id = ? AND owner_id = ? ORDER BY deduction_id")) {
                ps.setInt(1, payrollId);
                ps.setInt(2, currentUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.append("<tr><td>").append(Util.html(rs.getString(1))).append("</td><td align='right'>")
                            .append(Util.money(rs.getBigDecimal(2))).append("</td></tr>");
                    }
                }
            }
            if (rows.length() == 0) {
                rows.append("<tr><td>No deductions</td><td align='right'>").append(Util.money(BigDecimal.ZERO)).append("</td></tr>");
            }
            int at = html.indexOf("@@DEDUCTION_ROWS@@");
            html.replace(at, at + "@@DEDUCTION_ROWS@@".length(), rows.toString());
        }
        return html.toString();
    }
}

package payrollSystem;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// The choices the user made in the Filter window. The object is saved to a file with
// object serialization, so the dashboard remembers the choices the next time it opens.
// Every user has his/her own file (the file name has the user id).
public class FilterOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    // the choices that appear in the combo boxes
    public static final String[] STATUS_CHOICES = {"All", PayrollEntry.PAID, PayrollEntry.PENDING, PayrollEntry.NOT_PROCESSED};
    public static final String[] SORT_FIELDS = {"Name", "Employee ID", "Salary", "Overtime", "Payment Status", "Bonuses & Incentives"};
    public static final String[] ORDERS = {"Ascending (A-Z, small to big)", "Descending (Z-A, big to small)"};
    public static final String[] METHODS = {"Merge sort", "Insertion sort", "Selection sort"};

    public String status = "All";
    public String department = "All";
    public int year = 0;      // 0 means "the latest month that has payroll"
    public int month = 0;
    public String sortBy = "Name";
    public boolean ascending = true;
    public String method = "Merge sort";

    // true if nothing is filtered and the order is the normal one
    public boolean isDefault() {
        return "All".equals(status) && "All".equals(department) && year == 0
                && "Name".equals(sortBy) && ascending && "Merge sort".equals(method);
    }

    // the file of one user (stored in the folder "data" next to the program)
    private static File fileOf(int userId) {
        return new File("data", "dashboard_filter_user" + userId + ".dat");
    }

    // writes this object to the file (ObjectOutputStream + writeObject)
    public void save(int userId) {
        File file = fileOf(userId);
        file.getParentFile().mkdirs();   // make the folder "data" if it is not there yet
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(this);
        } catch (IOException ex) {
            System.err.println("Could not save the filter choices: " + ex.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing the file.");
            }
        }
    }

    // reads the saved choices of a user. If there is no file (or it is broken) we start with the defaults.
    public static FilterOptions load(int userId) {
        File file = fileOf(userId);
        if (!file.exists()) {
            return new FilterOptions();
        }
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(file));
            return (FilterOptions) input.readObject();
        } catch (EOFException ex) {
            return new FilterOptions();           // empty file
        } catch (ClassNotFoundException | IOException ex) {
            return new FilterOptions();           // broken file, just start again
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing the file.");
            }
        }
    }
}

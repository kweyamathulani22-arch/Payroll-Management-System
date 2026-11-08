package payrollSystem;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// The window that opens when the user presses the Filter button on the dashboard.
// It shows the options the table can be filtered and sorted on.
public class FilterDialog {

    private FilterDialog() { }

    // The panel with the combo boxes. It is a small class of its own so it can be built and read without showing a window.
    public static class FilterPanel extends JPanel {
        private final List<int[]> periods;             // each int[] is {year, month}
        private final JComboBox<String> periodBox = new JComboBox<>();
        private final JComboBox<String> statusBox = new JComboBox<>(FilterOptions.STATUS_CHOICES);
        private final JComboBox<String> departmentBox = new JComboBox<>();
        private final JComboBox<String> sortBox = new JComboBox<>(FilterOptions.SORT_FIELDS);
        private final JComboBox<String> orderBox = new JComboBox<>(FilterOptions.ORDERS);
        private final JComboBox<String> methodBox = new JComboBox<>(FilterOptions.METHODS);

        public FilterPanel(FilterOptions current, List<int[]> periods, List<String> departments) {
            super(new GridLayout(0, 2, 10, 8));   // 2 columns: a label and a combo box
            this.periods = periods;

            // fill the combo boxes
            periodBox.addItem("Latest month with payroll");
            int selectedPeriod = 0;
            for (int i = 0; i < periods.size(); i++) {
                periodBox.addItem(Util.monthName(periods.get(i)[1]) + " " + periods.get(i)[0]);
                if (periods.get(i)[0] == current.year && periods.get(i)[1] == current.month) {
                    selectedPeriod = i + 1;
                }
            }
            departmentBox.addItem("All");
            for (String department : departments) {
                departmentBox.addItem(department);
            }

            // show the choices that are active now
            periodBox.setSelectedIndex(selectedPeriod);
            statusBox.setSelectedItem(current.status);
            departmentBox.setSelectedItem(current.department);
            sortBox.setSelectedItem(current.sortBy);
            orderBox.setSelectedIndex(current.ascending ? 0 : 1);
            methodBox.setSelectedItem(current.method);

            add(new JLabel("Payroll month:"));
            add(periodBox);
            add(new JLabel("Payment status:"));
            add(statusBox);
            add(new JLabel("Department:"));
            add(departmentBox);
            add(new JLabel("Sort by:"));
            add(sortBox);
            add(new JLabel("Order:"));
            add(orderBox);
            add(new JLabel("Sorting method:"));
            add(methodBox);
        }

        // turns what is selected in the combo boxes into a FilterOptions object
        public FilterOptions read() {
            FilterOptions options = new FilterOptions();
            int p = periodBox.getSelectedIndex();
            if (p > 0) {
                options.year = periods.get(p - 1)[0];
                options.month = periods.get(p - 1)[1];
            }
            options.status = (String) statusBox.getSelectedItem();
            options.department = (String) departmentBox.getSelectedItem();
            options.sortBy = (String) sortBox.getSelectedItem();
            options.ascending = orderBox.getSelectedIndex() == 0;
            options.method = (String) methodBox.getSelectedItem();
            return options;
        }
    }

    // Shows the window. Gives back the new options when the user clicks Apply,
    // a fresh default FilterOptions for Reset, and null for Cancel.
    public static FilterOptions show(Component parent, FilterOptions current, List<int[]> periods, List<String> departments) {
        FilterPanel panel = new FilterPanel(current, periods, departments);
        String[] buttons = {"Apply", "Reset", "Cancel"};
        int choice = JOptionPane.showOptionDialog(parent, panel, "Filter and sort the table",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
        if (choice == 0) {
            return panel.read();
        }
        if (choice == 1) {
            return new FilterOptions();
        }
        return null;
    }
}

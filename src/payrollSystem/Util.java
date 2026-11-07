package payrollSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

// Small helper methods that many screens share: message boxes, numbers, text and window sizing.
public class Util {

    // the money symbol in front of amounts (taken from the currency in Settings, default R)
    public static String currencySymbol = "R";

    // pattern used to check an email address
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");

    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};

    private Util() { }

    // ------------------------------------------------------------ message boxes

    // red error message
    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Payroll Management System", JOptionPane.ERROR_MESSAGE);
    }

    // normal information message
    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Payroll Management System", JOptionPane.INFORMATION_MESSAGE);
    }

    // yes / no question, gives true when the user clicks Yes
    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Payroll Management System",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // Turns a database error into a message the user can understand
    public static void dbError(Component parent, SQLException ex) {
        String state = ex.getSQLState() == null ? "" : ex.getSQLState();
        String msg = ex.getMessage() == null ? "" : ex.getMessage();
        String text;
        if (msg.contains("No suitable driver") || msg.contains("ClassNotFound")) {
            text = "The MySQL driver (Connector/J) was not found.\n\nIn NetBeans: right-click the project > Properties >"
                    + " Libraries > Add JAR/Folder, then pick mysql-connector-j-x.x.x.jar.";
        } else if (ex.getErrorCode() == 1049) {
            text = "The database payroll_management_system does not exist yet.\n\n"
                    + "Run payroll_management_system.sql in MySQL Workbench first.";
        } else if (ex.getErrorCode() == 1045 || state.startsWith("28")) {
            text = "MySQL refused the user name or password.\n\nCheck db.properties (db.user and db.password).";
        } else if (state.startsWith("08")) {
            text = "Cannot reach the MySQL server.\n\nMake sure MySQL is running (normally on port 3306).";
        } else {
            text = "Database error:\n" + msg;
        }
        error(parent, text);
    }

    // -------------------------------------------------------------- the payslip window

    // Shows the payslip (HTML text) in its own window with a Print button.
    // The user can choose "Microsoft Print to PDF" in the print window to get a PDF.
    public static void showPayslip(Component parent, String html) {
        // the box that displays the HTML page
        final JEditorPane page = new JEditorPane();
        page.setContentType("text/html");
        page.setEditable(false);
        page.setText(html);
        page.setCaretPosition(0);

        // the dialog window
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        final JDialog dialog = new JDialog(owner, "Payslip", java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        // the two buttons at the bottom
        JButton print = new JButton("Print");
        JButton close = new JButton("Close");
        print.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    page.print();
                } catch (PrinterException ex) {
                    error(dialog, "The payslip could not be printed:\n" + ex.getMessage());
                }
            }
        });
        close.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dialog.dispose();
            }
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(print);
        buttons.add(close);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new JScrollPane(page), BorderLayout.CENTER);
        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);

        // make the window about A4 shaped but never bigger than the screen
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setSize(Math.min(700, screen.width - 60), Math.min(820, screen.height - 80));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // ------------------------------------------------------------ window sizing

    // This panel is used to keep a form in the middle of a big (maximised) window.
    // When the window is bigger than the form it stretches to the window size,
    // when the window is smaller the scroll bars appear.
    private static class CenterPanel extends JPanel implements Scrollable {

        CenterPanel() {
            super(new GridBagLayout());   // GridBagLayout puts the single component in the middle
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return 64;
        }

        // true means "make me as wide as the window" (only when the window is wider than the form)
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport && getParent().getWidth() > getPreferredSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() > getPreferredSize().height;
        }
    }

    // Puts the designer's form in the middle of the window, so when the user maximises
    // the window the form is still tidy and not stretched or stuck in the corner.
    // maximised = true makes the window start full screen.
    public static void fitScreen(JFrame frame, boolean maximised) {
        Container oldPane = frame.getContentPane();   // the form made by the NetBeans designer

        // The colour around the form: login and sign up use the colour of their own panel (red),
        // the other screens use a soft grey so the white form looks like a card in the middle
        Color background = new Color(236, 238, 242);
        if (!maximised && oldPane.getComponentCount() > 0) {
            background = oldPane.getComponent(0).getBackground();
        }
        CenterPanel middle = new CenterPanel();
        middle.setBackground(background);
        middle.add(oldPane);

        JScrollPane scroll = new JScrollPane(middle);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        frame.setContentPane(scroll);

        openWindow(frame, maximised);
    }

    // Sets the normal size, a small minimum size, centres the window and (if asked) maximises it
    public static void openWindow(JFrame frame, boolean maximised) {
        frame.pack();
        // the size the window has when the user "restores" it
        frame.setSize(Math.max(frame.getWidth(), 700), Math.max(frame.getHeight(), 450));
        frame.setMinimumSize(new Dimension(420, 300));
        frame.setLocationRelativeTo(null);
        if (maximised) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    // ------------------------------------------------------------------------ tables

    // sets the width of every column of a table (JTable shrinks them to fit if there is not enough room)
    public static void setColumnWidths(javax.swing.JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    // "September 2026" becomes "Sep 2026" so it fits in a small column
    public static String shortPeriod(String period) {
        return period.length() > 9 ? period.substring(0, 3) + period.substring(period.indexOf(' ')) : period;
    }

    // ----------------------------------------------------------------------- numbers

    // Reads a number from text. It accepts 1234.50 and 1234,50 and "R 1234.50".
    // If the text is not a number a NumberFormatException is thrown.
    public static BigDecimal parseDecimal(String text) {
        String t = text == null ? "" : text.trim().replace(" ", "");
        if (t.startsWith("R") || t.startsWith("r")) {
            t = t.substring(1);
        }
        t = t.replace(',', '.');
        return new BigDecimal(t);
    }

    // 1234.5 becomes "1234.50" (always two decimals)
    public static String plain(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // 12345.67 becomes "R 12,345.67"
    public static String money(BigDecimal value) {
        return currencySymbol + " " + String.format(Locale.US, "%,.2f", value.setScale(2, RoundingMode.HALF_UP));
    }

    // ------------------------------------------------------------------------- text

    // true if the text looks like an email address
    public static boolean isEmail(String text) {
        return EMAIL.matcher(text).matches();
    }

    // month number to name, 9 gives "September"
    public static String monthName(int month) {
        return MONTHS[month - 1];
    }

    // Reads a month typed by the user: 1 to 12, or a name like "September" or "sep".
    // Gives -1 if it is not a month.
    public static int parseMonth(String text) {
        String t = text == null ? "" : text.trim();
        if (t.matches("\\d{1,2}")) {
            int m = Integer.parseInt(t);
            return (m >= 1 && m <= 12) ? m : -1;
        }
        if (t.length() >= 3) {
            for (int i = 0; i < MONTHS.length; i++) {
                if (MONTHS[i].toLowerCase().startsWith(t.toLowerCase())) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    // true if a box still shows the grey example text from the designer, like "e.g UIF"
    public static boolean isPlaceholder(String text) {
        return text != null && text.trim().toLowerCase().startsWith("e.g");
    }

    // makes % and _ normal characters, so they can be used safely inside a LIKE search
    public static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    // makes text safe to put inside HTML (the payslip), e.g. "<" becomes "&lt;"
    public static String html(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // wraps a value for a CSV file (adds quotes if it has a comma or a quote)
    public static String csv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

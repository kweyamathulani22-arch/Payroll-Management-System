package payrollSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// The filter classes of the dashboard. Each one implements the EntryFilter interface.
public class EntryFilters {

    private EntryFilters() { }

    // keeps only the rows with a chosen payment status ("All" keeps everything)
    public static class StatusFilter implements EntryFilter {
        private final String status;

        public StatusFilter(String status) {
            this.status = status;
        }

        @Override
        public boolean accepts(PayrollEntry entry) {
            return "All".equals(status) || entry.getStatus().equals(status);
        }
    }

    // keeps only the employees of one department ("All" keeps everything)
    public static class DepartmentFilter implements EntryFilter {
        private final String department;

        public DepartmentFilter(String department) {
            this.department = department;
        }

        @Override
        public boolean accepts(PayrollEntry entry) {
            return "All".equals(department) || entry.getDepartment().equalsIgnoreCase(department);
        }
    }

    // the search box: every word typed must appear in the employee id, name, department or status
    public static class SearchFilter implements EntryFilter {
        private final String[] words;

        public SearchFilter(String text) {
            // trim removes the spaces around the text, split("\\s+") cuts it at every group of spaces
            String clean = text == null ? "" : text.trim().toLowerCase();
            this.words = clean.isEmpty() ? new String[0] : clean.split("\\s+");
        }

        @Override
        public boolean accepts(PayrollEntry entry) {
            // put everything we search in one text (StringBuilder is faster than adding Strings with +)
            StringBuilder haystack = new StringBuilder();
            haystack.append(entry.getEmployeeId()).append(' ').append(entry.getName()).append(' ')
                    .append(entry.getDepartment()).append(' ').append(entry.getStatus());
            String text = haystack.toString().toLowerCase();
            for (String word : words) {
                if (text.indexOf(word) < 0) {
                    return false;   // one word is missing, so this row is not a match
                }
            }
            return true;
        }
    }

    // Gives a new list with only the rows that every filter accepts.
    // Big O: O(n * f) where n = number of rows and f = number of filters.
    public static List<PayrollEntry> apply(List<PayrollEntry> all, List<EntryFilter> filters) {
        List<PayrollEntry> result = new ArrayList<>();
        Iterator<PayrollEntry> rows = all.iterator();   // an Iterator goes through the list one by one
        while (rows.hasNext()) {
            PayrollEntry entry = rows.next();
            boolean keep = true;
            for (EntryFilter filter : filters) {
                if (!filter.accepts(entry)) {
                    keep = false;
                    break;
                }
            }
            if (keep) {
                result.add(entry);
            }
        }
        return result;
    }
}

package payrollSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Searching and sorting for the dashboard table.
// These are the algorithms from our lectures (Searching, Sorting and Big O + Recursion),
// written as generic methods so they work for any list.
public class TableTools {

    private TableTools() { }

    // ----------------------------------------------------------------- comparators

    // Makes the Comparator for the "Sort by" choice (a Comparator tells how to compare two rows).
    public static Comparator<PayrollEntry> comparatorFor(String sortBy, boolean ascending) {
        Comparator<PayrollEntry> cmp;
        if ("Employee ID".equals(sortBy)) {
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return Integer.compare(a.getEmployeeId(), b.getEmployeeId());
                }
            };
        } else if ("Salary".equals(sortBy)) {
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return a.getNormalPay().compareTo(b.getNormalPay());
                }
            };
        } else if ("Overtime".equals(sortBy)) {
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return a.getOvertimePay().compareTo(b.getOvertimePay());
                }
            };
        } else if ("Bonuses & Incentives".equals(sortBy)) {
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return a.getBonusPay().compareTo(b.getBonusPay());
                }
            };
        } else if ("Payment Status".equals(sortBy)) {
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return Integer.compare(statusRank(a.getStatus()), statusRank(b.getStatus()));
                }
            };
        } else {
            // "Name" (the natural order of PayrollEntry)
            cmp = new Comparator<PayrollEntry>() {
                @Override
                public int compare(PayrollEntry a, PayrollEntry b) {
                    return a.compareTo(b);
                }
            };
        }
        // Collections.reverseOrder turns the comparator around for descending order
        return ascending ? cmp : Collections.reverseOrder(cmp);
    }

    // Paid first, then Pending, then Not processed
    private static int statusRank(String status) {
        if (PayrollEntry.PAID.equals(status)) {
            return 0;
        }
        return PayrollEntry.PENDING.equals(status) ? 1 : 2;
    }

    // ---------------------------------------------------------------------- sorting

    // Sorts a list with the method chosen in the Filter window and gives back the sorted list.
    public static List<PayrollEntry> sort(List<PayrollEntry> rows, FilterOptions options) {
        Comparator<PayrollEntry> cmp = comparatorFor(options.sortBy, options.ascending);
        if ("Insertion sort".equals(options.method)) {
            List<PayrollEntry> copy = new ArrayList<>(rows);
            insertionSort(copy, cmp);
            return copy;
        }
        if ("Selection sort".equals(options.method)) {
            List<PayrollEntry> copy = new ArrayList<>(rows);
            selectionSort(copy, cmp);
            return copy;
        }
        return mergeSort(rows, cmp);
    }

    // Merge sort uses RECURSION: split the list in two halves, sort each half (the recursive calls),
    // then merge the two sorted halves. The base case is a list with 0 or 1 element.
    // Big O: O(n log n), the fastest of our three algorithms.
    public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp) {
        if (list.size() <= 1) {
            return new ArrayList<>(list);   // base case: already sorted
        }
        int middle = list.size() / 2;
        List<T> left = mergeSort(new ArrayList<>(list.subList(0, middle)), cmp);              // recursion step
        List<T> right = mergeSort(new ArrayList<>(list.subList(middle, list.size())), cmp);   // recursion step
        return merge(left, right, cmp);
    }

    // puts two sorted lists together into one sorted list
    private static <T> List<T> merge(List<T> left, List<T> right, Comparator<? super T> cmp) {
        List<T> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            // take the smaller of the two current elements (<= keeps equal rows in their old order)
            if (cmp.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex++));
            } else {
                result.add(right.get(rightIndex++));
            }
        }
        // one list is finished, copy what is left of the other one
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex++));
        }
        return result;
    }

    // Insertion sort: take the next element and insert it at the right place in the part that is already sorted.
    // Big O: O(n squared), simple but slow for big lists.
    public static <T> void insertionSort(List<T> list, Comparator<? super T> cmp) {
        for (int next = 1; next < list.size(); next++) {
            T insert = list.get(next);        // the element we want to place
            int moveItem = next;
            // shift bigger elements one place to the right
            while (moveItem > 0 && cmp.compare(list.get(moveItem - 1), insert) > 0) {
                list.set(moveItem, list.get(moveItem - 1));
                moveItem--;
            }
            list.set(moveItem, insert);
        }
    }

    // Selection sort: find the smallest element of the unsorted part and swap it to the front.
    // Big O: O(n squared).
    public static <T> void selectionSort(List<T> list, Comparator<? super T> cmp) {
        for (int i = 0; i < list.size() - 1; i++) {
            int smallest = i;
            for (int index = i + 1; index < list.size(); index++) {
                if (cmp.compare(list.get(index), list.get(smallest)) < 0) {
                    smallest = index;
                }
            }
            Collections.swap(list, i, smallest);   // swap the smallest into place
        }
    }

    // --------------------------------------------------------------------- searching

    // Binary search for an employee id. The list MUST be sorted by employee id.
    // Each step throws away half of the list. Big O: O(log n). Gives the index or -1 if not found.
    public static int binarySearchById(List<PayrollEntry> sortedById, int id) {
        int low = 0;
        int high = sortedById.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            int middleId = sortedById.get(middle).getEmployeeId();
            if (id == middleId) {
                return middle;            // found
            } else if (id < middleId) {
                high = middle - 1;        // look in the lower half
            } else {
                low = middle + 1;         // look in the upper half
            }
        }
        return -1;                        // not found
    }

    // Recursive linear search (like exercise 19.8 in the lecture): look at one row per call.
    // Base case 1: we passed the end of the list. Base case 2: this row matches.
    // Big O: O(n). Gives the index of the first match or -1.
    public static int recursiveSearch(List<PayrollEntry> rows, EntryFilter matcher, int index) {
        if (index >= rows.size()) {
            return -1;                                   // base case: no more rows
        }
        if (matcher.accepts(rows.get(index))) {
            return index;                                // base case: found it
        }
        return recursiveSearch(rows, matcher, index + 1); // recursion step: try the next row
    }
}

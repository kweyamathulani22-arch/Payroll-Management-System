package payrollSystem;

// An interface: a promise that every filter class has an accepts method.
// The dashboard only knows about this interface, so it can use any filter the same way (polymorphism).
public interface EntryFilter {

    // true if the row must stay in the table, false if the filter removes it
    boolean accepts(PayrollEntry entry);
}

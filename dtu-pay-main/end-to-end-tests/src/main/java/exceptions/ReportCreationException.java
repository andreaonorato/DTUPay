package exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ReportCreationException extends Exception {
    public ReportCreationException() {
        super("There are no transactions to report");
    }
}

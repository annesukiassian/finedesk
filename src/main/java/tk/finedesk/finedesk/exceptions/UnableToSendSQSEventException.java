package tk.finedesk.finedesk.exceptions;

public class UnableToSendSQSEventException extends Exception {
    public UnableToSendSQSEventException(String s) {
        super(s);
    }
}

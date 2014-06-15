package utils;

public class FormatNotSupportedException extends Exception {
    private static final long serialVersionUID = 1L;

    public FormatNotSupportedException(String msg)
    {
    	super(msg);
    }
}

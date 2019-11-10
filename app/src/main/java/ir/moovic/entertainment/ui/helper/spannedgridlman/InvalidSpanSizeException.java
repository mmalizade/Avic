package ir.moovic.entertainment.ui.helper.spannedgridlman;

public class InvalidSpanSizeException extends RuntimeException {

    public InvalidSpanSizeException(int errorSize, int maxSpanSize) {
        super("Invalid item span size: " + errorSize + ". Span size must be in the range: (1..." + maxSpanSize + ")");
    }
    
}

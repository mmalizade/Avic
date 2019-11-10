package ir.moovic.entertainment.ui.helper.spannedgridlman;

public class InvalidMaxSpansException extends RuntimeException {

    public InvalidMaxSpansException(int maxSpanSize) {
        super("Invalid layout spans: " + maxSpanSize + ". Span size must be at least 1.");
    }

}

package ir.moovic.entertainment.ui.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CircleTextSpan extends ReplacementSpan {

    private final int backgroundColor;
    private final int textColor;
    private final int padding;

    public CircleTextSpan(int backgroundColor, int textColor, int padding) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.padding = padding;
    }
    @Override
    public int getSize(@NonNull Paint paint,
                       CharSequence text,
                       int start,
                       int end,
                       @Nullable Paint.FontMetricsInt fontMetricsInt)
    {
        return Math.round(measureWidth(paint, text, start, end));
    }

    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text,
                     int start,
                     int end,
                     float x,
                     int top,
                     int y,
                     int bottom,
                     @NonNull Paint paint
    ) {
        paint.setColor(backgroundColor);
        float size = measureWidth(paint, text, start, end);
        canvas.drawCircle(x + size / 2, (bottom - top) / 2, size / 2, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x + padding, y, paint);
    }
    private float measureWidth(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end) + 2 * padding;
    }
}
package ir.moovic.entertainment.ui.helper.spannedgridlman;

import android.graphics.Rect;

public class RectExtenstion {


    public static boolean isAdjacentTo(Rect item, Rect rect) {
        return (item.right == rect.left
                || item.top == rect.bottom
                || item.left == rect.right
                || item.bottom == rect.top);
    }

    public static boolean intersects(Rect item, Rect rect) {
        return item.intersects(rect.left, rect.top, rect.right, rect.bottom);
    }

}

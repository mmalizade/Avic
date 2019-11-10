package ir.moovic.entertainment.utils;


import android.util.Log;
import android.view.View;

public abstract class MultipleClickListener implements View.OnClickListener {
    private int maxCount = 10;
    private int counter = 0;
    private long lastClickTime = 0;

    private static final int RESET_TIME_MILLIS = 1500;

    public MultipleClickListener(int maxCount) {
        this.maxCount = maxCount > 1 ? maxCount : 1;
    }

    @Override
    public void onClick(View view) {
        long time = System.currentTimeMillis();
        if( (time - lastClickTime) > RESET_TIME_MILLIS ) {
            counter = 0;
        }
        lastClickTime = time;
        Log.e("counter", "counter = " + counter);
        if(++counter >= maxCount){
            performClick(view);
            counter = 0;
        }
    }

    public abstract void performClick(View v);
}

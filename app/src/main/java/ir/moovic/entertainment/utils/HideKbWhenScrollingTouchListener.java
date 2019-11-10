package ir.moovic.entertainment.utils;


import android.util.Log;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

public class HideKbWhenScrollingTouchListener implements  RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d("HideKbHelper", "onTouchEvent: action = "  + e.getAction());
        Utils.hideInputMethod(rv);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

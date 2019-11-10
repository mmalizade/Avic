package ir.moovic.entertainment.utils;

import android.os.Handler;

import java.lang.ref.WeakReference;

public class CounterRunnable implements Runnable{
    private boolean kill = false;
    private int maxValue = 8;
    private int minValue = 0;
    private long duration = 5000L;
    private WeakReference<Handler> handlerRef;
    private int value ;
    private OnValueUpdateListener listener;

    public CounterRunnable(Handler handler, int minValue, int maxValue, long duration) {
        this.minValue = this.value = minValue;
        this.maxValue = maxValue;
        this.duration = duration;
        this.handlerRef = new WeakReference<>(handler);
    }

    public void setListener(OnValueUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        if(kill) return;
        Handler h = handlerRef.get();
        if(h == null || duration <= 0 || maxValue < 2) return;
        value++;
        if(value > maxValue) {
            value = minValue;
        }
        h.postDelayed(this, duration);
        if(listener != null){
            listener.valueUpdated(value);
        }
    }

    public void start(){
        this.kill = false;
        Handler h = handlerRef.get();
        if(h != null && duration > 0){
            value = minValue;
            h.postDelayed(this, duration);
        }
    }

    public void cancel(){
        this.kill = true;
        value = minValue;
    }

    public interface OnValueUpdateListener{
        void valueUpdated(int value);
    }
}
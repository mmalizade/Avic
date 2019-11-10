package ir.moovic.entertainment.utils;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OnTouchScaleListener implements View.OnTouchListener {
    private static final float ON_TOUCH_SCALE = 0.9f;
    private static final long DURATION = 100;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!v.hasOnClickListeners()){
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("OnTouchScaleListener", "onTouch: action down");
//                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v, "scaleX", ON_TOUCH_SCALE);
//                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v, "scaleY", ON_TOUCH_SCALE);
//                scaleDownX.setDuration(DURATION);
//                scaleDownY.setDuration(DURATION);
//                AnimatorSet scaleDown = new AnimatorSet();
//                scaleDown.play(scaleDownX).with(scaleDownY);
//                scaleDown.start();
                v.setScaleX(ON_TOUCH_SCALE);
                v.setScaleY(ON_TOUCH_SCALE);
                return false;
            case MotionEvent.ACTION_UP:
                Log.e("OnTouchScaleListener", "onTouch: action up");
//                ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(v, "scaleX", 1f);
//                ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(v, "scaleY", 1f);
//                scaleDownX2.setDuration(DURATION);
//                scaleDownY2.setDuration(DURATION);
//
//                AnimatorSet scaleDown2 = new AnimatorSet();
//                scaleDown2.play(scaleDownX2).with(scaleDownY2);
//                scaleDown2.start();
                v.setScaleX(1f);
                v.setScaleY(1f);
            return false;
        }
        Log.d("OnTouchScaleListener", "onTouch: other action");
        return onTouchView(v, event);
    }

    public boolean onTouchView(View v, MotionEvent event) {
        return false;
    }
}

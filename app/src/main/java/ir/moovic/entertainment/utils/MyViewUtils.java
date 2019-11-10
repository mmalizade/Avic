package ir.moovic.entertainment.utils;


import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MyViewUtils {

    public static void expand(View v, Animation.AnimationListener listener) {
        expand(v, listener, -1);
    }

    public static void collapse(View v, Animation.AnimationListener listener) {
        collapse(v, listener, -1);
    }

    public static void collapse(final View v, Animation.AnimationListener listener, long duration) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if(duration < 0) {
            // 1dp/ms
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        } else {
            a.setDuration(duration);
        }

        if(listener != null){
            a.setAnimationListener(listener);
        }
        v.startAnimation(a);

    }

    public static void expand(final View v, Animation.AnimationListener listener, long duration){
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if(duration < 0 ) {
            // 1dp/ms
            a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        } else {
            a.setDuration(duration);
        }
        if(listener != null){
            a.setAnimationListener(listener);
        }
        v.startAnimation(a);
    }

    public static void setSelectableItemViewBackground(View v){
        if(v == null || v.getContext() == null) return;
        TypedValue outValue = new TypedValue();
        v.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        v.setBackgroundResource(outValue.resourceId);
    }
}

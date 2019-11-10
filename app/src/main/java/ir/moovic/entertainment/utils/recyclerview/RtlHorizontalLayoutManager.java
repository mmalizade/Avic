package ir.moovic.entertainment.utils.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RtlHorizontalLayoutManager extends LinearLayoutManager {


    public RtlHorizontalLayoutManager(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public RtlHorizontalLayoutManager(Context context, boolean reverseLayout) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
    }

    public RtlHorizontalLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
    @Override
    protected boolean isLayoutRTL(){
        return true;
    }

}

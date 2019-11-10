package ir.moovic.entertainment.ui.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.utils.Utils;


public class MyImageView extends AppCompatImageView {

    private int ratioW = 9;
    private int ratioH = 16;
    private boolean isFixedWidth = true;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.myiv, 0, 0);
        ratioW = ta.getInteger(R.styleable.myiv_myiv_ratioW, 9);
        ratioH = ta.getInteger(R.styleable.myiv_myiv_ratioH, 16);
        isFixedWidth = ta.getBoolean(R.styleable.myiv_myiv_byW, true);
        boolean centerCrop = ta.getBoolean(R.styleable.myiv_myiv_centerCrop, true);
        if(centerCrop) {
            setScaleType(ScaleType.CENTER_CROP);
        }
        ta.recycle();
    }

    public void setRatio(int w, int h){
        this.ratioH = h > 0 ? h : 16;
        this.ratioW = w > 0 ? w : 9;
        int gcd = Utils.gcdInt(ratioW, ratioH);
        ratioW /= gcd;
        ratioH /= gcd;
        requestLayout();
    }

    public void setFixedWidth(boolean fixedWidth) {
        isFixedWidth = fixedWidth;
    }

    public int getRatioW() {
        return ratioW;
    }

    public int getRatioH() {
        return ratioH;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(isFixedWidth) {
//            int width = getMeasuredWidth();
            int height = widthSize * ratioH / ratioW;
            setMeasuredDimension(widthSize, height);
        } else {
//            int height = getMeasuredHeight();
            int width = heightSize * ratioW / ratioH;
            setMeasuredDimension(width, heightSize);
        }
    }
}

package ir.moovic.entertainment.ui.helper;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.utils.Utils;

@BindingMethods({
        @BindingMethod(type= MyButton.class, attribute = "app:mb_text", method = "setText"),
        @BindingMethod(type= MyButton.class, attribute = "app:mb_bgColor", method = "setBgColor"),
        @BindingMethod(type= MyButton.class, attribute = "app:mb_bgColor_2", method = "setBgColor2"),
})
public class MyButton extends RelativeLayout {

    public static final int MODE_SINGLE = 0;
    public static final int MODE_DOUBLE_W_TEXT = 1;
    public static final int MODE_DOUBLE_W_ICON = 2;
    private static final int FONT_IRANSANS = 0;
    private static final int FONT_IRANSAN_BOLD = 1;
    private static final int FONT_YEKAN = 2;
    private static final int FONT_YEKAN_BOLD = 3;

    private int mode = MODE_SINGLE;
    private int textColor = Color.WHITE;
    private int progressColor = Color.WHITE;
    private int textSize;
    private int progressSize = -1;
    private String text = "";
    private String textSecond = "";
    private int icon;
    private int iconSize;
    private int shadowColor;
    private int shadowSize;
    private boolean shadowEnable = false;
    private int bgColor, bgColor2;

    private int cornerRadius;
    private String loadingIndicator = "BallPulseIndicator";
    private int paddingSecondaryRightLeft = 0;
    private boolean showOutline = false;
    private int font = FONT_IRANSANS;
    private ColorStateList normalColorState = null;
    private View view2;
    private AppCompatTextView tv1;
    private AppCompatTextView tv2;
    private ProgressBar progressView;
    private AppCompatImageView iconView;
    private boolean mb_en = true;
    private boolean colorStateEnable = true;

    public MyButton(Context context) {
        super(context);
        init(context);
        refresh();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        refresh();
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        init(context);
        mode = MODE_SINGLE;
        textColor = ContextCompat.getColor(context, R.color.my_btn_text_color_default);
        progressColor = ContextCompat.getColor(context, R.color.my_btn_text_color_default);
        textSize = context.getResources().getDimensionPixelSize(R.dimen.my_btn_text_size_default);
        text = "";
        textSecond = null;
        icon = -1;
        iconSize = context.getResources().getDimensionPixelSize(R.dimen.my_btn_icon_size_default);
        shadowColor = ContextCompat.getColor(context, R.color.my_btn_shadow_color_default);
        shadowSize = context.getResources().getDimensionPixelSize(R.dimen.my_btn_shadow_size_default);
        shadowEnable = false;
        bgColor = Color.WHITE;
        bgColor2 = bgColor;
        cornerRadius = context.getResources().getDimensionPixelSize(R.dimen.my_btn_corner_radius);
        loadingIndicator = "BallPulseIndicator";
        paddingSecondaryRightLeft = context.getResources().getDimensionPixelSize(R.dimen.my_btn_secondary_view_padding_right_left_def);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyButton, 0, 0);
        mode = ta.getInteger(R.styleable.MyButton_mb_mode, MODE_SINGLE);
        textColor = ta.getColor(R.styleable.MyButton_mb_textColor, textColor);
        progressColor = ta.getColor(R.styleable.MyButton_mb_textColor, progressColor);
        textSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_textSize, textSize);
        progressSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_progressViewSize, -1);
        paddingSecondaryRightLeft = ta.getDimensionPixelSize(R.styleable.MyButton_mb_secondaryPaddingRightLeft, paddingSecondaryRightLeft);
        if(ta.hasValue(R.styleable.MyButton_mb_text)){
            text = ta.getString(R.styleable.MyButton_mb_text);
        }
        if(ta.hasValue(R.styleable.MyButton_mb_text2)){
            textSecond = ta.getString(R.styleable.MyButton_mb_text2);
        }
        if(ta.hasValue(R.styleable.MyButton_mb_loadingIndicator)){
            loadingIndicator = ta.getString(R.styleable.MyButton_mb_loadingIndicator);
        }
        font = ta.getInt(R.styleable.MyButton_mb_font, FONT_IRANSANS);
        icon = ta.getResourceId(R.styleable.MyButton_mb_icon, 0);
        iconSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_iconSize, iconSize);
        shadowColor = ta.getColor(R.styleable.MyButton_mb_shadowColor, shadowColor);
        shadowSize = ta.getDimensionPixelSize(R.styleable.MyButton_mb_shadowSize, shadowSize);
        shadowEnable = ta.getBoolean(R.styleable.MyButton_mb_shadowSize, shadowEnable);
        bgColor = ta.getColor(R.styleable.MyButton_mb_bgColor, bgColor);
        bgColor2 = ta.getColor(R.styleable.MyButton_mb_bgColor_2, bgColor);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.MyButton_mb_cornerRadius, cornerRadius);
        showOutline = ta.getBoolean(R.styleable.MyButton_mb_showOutline, false);
        normalColorState = ta.getColorStateList(R.styleable.MyButton_mb_colorState);
        setEnabled(ta.getBoolean(R.styleable.MyButton_mb_enable, true));
        ta.recycle();
    }

    private void init(Context context) {
        inflate(context, R.layout.view_my_button, this);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        progressView = findViewById(R.id.progress_view);
        iconView = findViewById(R.id.icon_view);
        view2 = findViewById(R.id.frame2);
        this.colorStateEnable = true;
    }

    public void refresh() {
        setMode(mode);
//        tv1.setTextColor(showOutline ? bgColor : textColor);
        tv1.setTextColor(textColor);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv1.setText(text);
//        tv2.setTextColor(showOutline ? bgColor : textColor);
        tv2.setTextColor(textColor);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv2.setText(textSecond);
        typeface();
        iconView.setImageResource(icon);
        iconView.setColorFilter(showOutline ? bgColor : textColor);
        setIconSize(iconSize);
        progressSize();
//        progressView.setIndicatorColor(progressColor);
//        progressView.setIndicator(loadingIndicator);
        try {
            progressView.getIndeterminateDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
        } catch (Exception e){}
        view2.setPadding(paddingSecondaryRightLeft, 0, paddingSecondaryRightLeft, 0);
        bg();
    }


    public void clearIconColorFilter(){
        iconView.clearColorFilter();
        iconView.setBackgroundResource(icon);
    }

    private void typeface() {
        if(font == FONT_IRANSAN_BOLD) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.default_font_bold));
            tv1.setTypeface(tf);
            tv2.setTypeface(tf);
        } else {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.default_font));
            tv1.setTypeface(tf);
            tv2.setTypeface(tf);
        }
    }

    private void bg() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        if(showOutline){
            int strokeWidth = getContext().getResources().getDimensionPixelSize(R.dimen.my_btn_stroke_width_def);
            shape.setStroke(strokeWidth, bgColor);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_pressed },
                        new int[] { android.R.attr.state_focused },
                        new int[] { android.R.attr.state_enabled },
                        new int[] {-android.R.attr.state_enabled },
                };

                int c = Utils.decreaseRgbChannels(bgColor, 0.4f);
                int[] colors = new int[] {
                        c, c,
                        Color.TRANSPARENT, Color.TRANSPARENT
                };

                ColorStateList myList = new ColorStateList(states, colors);
                shape.setColor(myList);
            }else {
                shape.setColor(Color.TRANSPARENT);
            }
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                if(colorStateEnable && normalColorState != null) {
                    shape.setColor(normalColorState);
                } else {
                    shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                    shape.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    shape.mutate();
                    shape.setColors(new int[]{bgColor, bgColor2});
                }
            } else {
                shape.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                shape.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                shape.mutate();
                shape.setColors(new int[]{bgColor, bgColor2});
            }
        }
        setBackground(shape);
    }

    public void setIconSize(int iconSize){
        if(iconSize < 0) return;
        this.iconSize = iconSize;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iconView.getLayoutParams();
        params.width = iconSize;
        params.height = iconSize;
        iconView.setLayoutParams(params);
    }

    private void progressSize(){
        if(progressSize < 0){
            tv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressView.getLayoutParams();
                    int size = Math.min(tv1.getWidth(), tv1.getHeight());
                    params.width = size;
                    params.height = size;
                    progressView.setLayoutParams(params);
                    tv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressView.getLayoutParams();
            params.width = progressSize;
            params.height = progressSize;
            progressView.setLayoutParams(params);
        }
    }

    public void setText(String text){
        if(text != null){
            this.text = text;
            tv1.setText(text);
            progressSize();
        }
    }

    public void setIcon(int iconResId){
        this.icon = iconResId;
        iconView.setImageResource(iconResId);
    }

    public void setMode(int mode){
        this.mode = mode;
        hideProgress();
        if(mode == MODE_SINGLE){
            view2.setVisibility(GONE);
        } else if(mode == MODE_DOUBLE_W_TEXT){
            view2.setVisibility(VISIBLE);
            tv2.setVisibility(VISIBLE);
            iconView.setVisibility(GONE);
        } else if(mode == MODE_DOUBLE_W_ICON){
            view2.setVisibility(VISIBLE);
            tv2.setVisibility(GONE);
            iconView.setVisibility(VISIBLE);
        }
    }

    public void setColorStateEnable(boolean en){
        this.colorStateEnable = en;
        bg();
    }


    public void hideProgress(){
        progressView.setVisibility(INVISIBLE);
        tv1.setVisibility(VISIBLE);
    }

    public void showProgress() {
        progressView.setVisibility(VISIBLE);
        tv1.setVisibility(INVISIBLE);
    }

    public void progress(boolean show){
        if(show) {
            showProgress();
        } else {
            hideProgress();
        }
    }
    public boolean isInProgress(){
        return progressView.getVisibility() == VISIBLE;
    }

    public void setBgColor(int color){
        this.bgColor = color;
        bg();
    }

    public void setBgColor2(int bgColor2) {
        this.bgColor2 = bgColor2;
        bg();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

}
package ir.moovic.entertainment.utils;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import ir.moovic.entertainment.R;


public class ToastMma {

    public static final int INFO = 4;
    public static final int ERROR = 3;
    public static final int WARNING = 2;
    public static final int DEFAULT = 5;
    public static final int SUCCESS = 1;


    public static void show(Context context, String message, int type){
        if(message == null) return;
        Toast toast = simpleToast(context, message, type);
//        int yOffset =  context.getResources().getDisplayMetrics().heightPixels / 5;
//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.show();
    }

    public static Toast make(Context context, String message, int type){
        return simpleToast(context, message, type);
    }
    public static void show(Context context, String message){
        show(context, message, DEFAULT);
    }

    private static Toast simpleToast(Context context, String text, int type){
        Toast toast = new Toast(context);
        AppCompatTextView tv = (AppCompatTextView) LayoutInflater.from(context).
                inflate(R.layout.toast_simple_layout, null, false);

        tv.setText(text);
        if(type > 0 ){
            tv.setBackgroundResource(bgDrawableId(type));
            tv.setTextColor(type == DEFAULT ? Color.BLACK : Color.WHITE);
        }

        tv.setPadding(100, 35, 100, 35);

        toast.setView(tv);
        int yOffset = Utils.displayMetrics(context).heightPixels / 6;
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    private static int bgDrawableId(int type){
        int id = 0;
        switch (type){
            case SUCCESS :
                id = R.drawable.toast_simple_bg_success;
                break;
            case INFO :
                id = R.drawable.toast_simple_bg_info;
                break;
            case WARNING :
                id = R.drawable.toast_simple_bg_warning;
                break;
            case ERROR :
                id = R.drawable.toast_simple_bg_error;
                break;
            case DEFAULT :
            default:
                id = R.drawable.toast_simple_bg;
                break;
        }
        return id;
    }

}

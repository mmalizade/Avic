package ir.moovic.entertainment.app.config.base;

import android.os.Handler;

public interface IActivityDoubleBackToExit {
    void enableDoubleBackPress(boolean en);
    boolean isDoubleBackPressEnabled();
    void setBackPressedOnce(boolean pressedOnce);
    boolean isBackPressedOnce();
    Handler getHandler();
}

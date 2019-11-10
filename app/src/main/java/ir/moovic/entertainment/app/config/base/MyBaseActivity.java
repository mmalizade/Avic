package ir.moovic.entertainment.app.config.base;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.network.RetrofitCallback;
import ir.moovic.entertainment.utils.Utils;
import retrofit2.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public abstract class MyBaseActivity extends AppCompatActivity implements IActivityDoubleBackToExit, IRetroComponent {
    public static final String KEY_PAGE_TITLE = "toolbartitle";

    protected RetrofitManager retrofitManager;

    @Nullable @BindView(R.id.toolbar_title)     public AppCompatTextView tvToolbarTitle;
    @Nullable @BindView(R.id.appbar)            public AppBarLayout appBarLayout;
    @Nullable @BindView(R.id.toolbar_back)      public AppCompatImageView btnToolbarBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        retrofitManager = new RetrofitManager(this);
        cancelBackPressRunnable = new CancelBackPressRunnable(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        onSetContentView();
    }

    private void onSetContentView(){
        if(getIntent().hasExtra(KEY_PAGE_TITLE)) {
            String title = getIntent().getStringExtra(KEY_PAGE_TITLE);
            setToolbarTitle(title);
        }

        if(appBarLayout != null) {
            if(isAppBarElavationEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayout.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this, R.animator.appbar_elevation));
                }
            }
        }
    }

    protected boolean isAppBarElavationEnabled() {
        return true;
    }

    protected void setToolbarTitle(String title) {
        if(tvToolbarTitle != null) {
            tvToolbarTitle.setText(title);
        }
    }

    protected void setToolbarTextColor(int color) {
        if(tvToolbarTitle != null) {
            tvToolbarTitle.setTextColor(color);
        }
        if(btnToolbarBack != null) {
            btnToolbarBack.setColorFilter(color);
        }
    }

    @Optional
    @OnClick(R.id.toolbar_back)
    public void onToolbarBackClick(View v) {
        onBackPressed();
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(retrofitManager != null) {
            retrofitManager.cancelAllRequests();
        }
    }

    public <T> void enqueue(Call<T> call, RetrofitCallback<T> callback) {
        if(retrofitManager == null) {
            retrofitManager = new RetrofitManager(this);
        }
        callback.setRetrofitManager(retrofitManager);
        retrofitManager.enqueue(call, callback);
    }

    public void changeStatusBarColor(@ColorInt int color) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public void changeStatusBarWithColorResId(@ColorRes int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        changeStatusBarColor(color);
    }

    public void transparentStatusBarFullScreen(){
        transparentStatusBar(true);
    }

    public void transparentStatusBar(boolean fullscreen){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(fullscreen){
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            window.getDecorView().setSystemUiVisibility(flags);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for(Fragment f : fragmentList) {
            if(f instanceof IFrBackPressed) {
                boolean handled = ((IFrBackPressed)f).onBackPressed();
                if(handled) {
                    return;
                }
            }
        }

        boolean popBackstack = popFragmentBackStack();
        if(popBackstack) return;

        if(isDoubleBackPressEnabled()){
            if(isBackPressedOnce()){
                super.onBackPressed();
            } else {
                setBackPressedOnce(true);
                if(cancelBackPressRunnable != null) {
                    cancelBackPressRunnable.postDelayed(3000);
                    cancelBackPressRunnable.showToast1(this);
                }
            }
        } else {
            super.onBackPressed();
        }
    }


    /*********************************
     Fragment Utils
     **********************************/

    protected FragmentTransaction ft() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        return transaction;
    }

    protected FragmentTransaction ft(int animIn, int animOut, int popEnter, int popExit) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut, popEnter, popExit);
        return transaction;
    }

    protected FragmentTransaction ft(int animIn, int animOut) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        return transaction;
    }

    protected void animatedReplaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction transaction = ft();
        if(addToBackStack){
            int cnt = getSupportFragmentManager().getBackStackEntryCount();
            transaction.addToBackStack(String.valueOf(cnt + 1));
        }
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public boolean popFragmentBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
//            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    public void loading(boolean show) {

    }

    @Override
    public void error(String message) {

    }

    /***********************************************
     If Double Back Press Needed To Exit Activity
     *************************************************/

    public boolean doubleBackPressToExit = false;
    public boolean backPressedOnce = false;
    public Handler handler;

    private CancelBackPressRunnable cancelBackPressRunnable;

    @Override
    public void enableDoubleBackPress(boolean en) {
        if(cancelBackPressRunnable == null) {
            cancelBackPressRunnable = new CancelBackPressRunnable(this);
        }
        this.doubleBackPressToExit = en;
    }

    @Override
    public boolean isDoubleBackPressEnabled() {
        return this.doubleBackPressToExit;
    }

    @Override
    public void setBackPressedOnce(boolean pressedOnce) {
        this.backPressedOnce = true;
    }

    @Override
    public boolean isBackPressedOnce(){
        return backPressedOnce;
    }

    @Override
    public Handler getHandler(){
        if(handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    private static class CancelBackPressRunnable implements Runnable {

        private WeakReference<IActivityDoubleBackToExit> activityRef;

        public CancelBackPressRunnable(IActivityDoubleBackToExit activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            IActivityDoubleBackToExit activity = activityRef.get();
            if(activity == null) return;
            activity.setBackPressedOnce(false);
        }

        public void postDelayed(long delay) {
            IActivityDoubleBackToExit activity = activityRef.get();
            if(activity == null) return;
            if(activity.isDoubleBackPressEnabled()) {
                activity.getHandler().postDelayed(this, delay);
            }
        }

        public void removeCallbacks(){
            IActivityDoubleBackToExit activity = activityRef.get();
            if(activity == null) return;
            if(activity.isDoubleBackPressEnabled()) {
                activity.getHandler().removeCallbacks(this);
            }
        }

        public void showToast1(Context context) {
            Toast toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.toast_simple_layout, null, false);
            toast.setView(view);
            int yOffset = Utils.displayMetrics(context).heightPixels / 10;
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /*

     */
}

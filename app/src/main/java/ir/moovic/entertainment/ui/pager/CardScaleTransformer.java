package ir.moovic.entertainment.ui.pager;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

public class CardScaleTransformer implements ViewPager.PageTransformer {

    private int baseElevation;
    private int raisingElevation;
    private float smallerScale;

    public CardScaleTransformer(int baseElevation, int raisingElevation, float smallerScale) {
        this.baseElevation = baseElevation;
        this.raisingElevation = raisingElevation;
        this.smallerScale = smallerScale;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {

        float absPosition = Math.abs(position);

        if (absPosition >= 1) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                setViewElevation(page, baseElevation);
            }
            page.setScaleY(smallerScale);
        } else {
            // This will be during transformation
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                setViewElevation(page, (1 - absPosition) * raisingElevation + baseElevation);
            }
            page.setScaleY(1 + absPosition * (smallerScale - 1));
        }
    }

    private void setViewElevation(View view, float elevation) {
        ViewCompat.setElevation(view, elevation);
    }

}

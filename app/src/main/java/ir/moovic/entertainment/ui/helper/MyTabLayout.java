package ir.moovic.entertainment.ui.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import ir.moovic.entertainment.R;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;


public class MyTabLayout extends TabLayout {
    private String fontPath;

    public MyTabLayout(Context context) {
        super(context);
        fontPath = "fonts/IranSans-Bold.ttf";
        try {
            fontPath = context.getString(R.string.default_font_bold);
        } catch (Exception e){}
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        String DEFAULT_TYPEFACE = "fonts/IranSans-Bold.ttf";
        try {
            DEFAULT_TYPEFACE = context.getString(R.string.default_font_bold);
        } catch (Exception e){
        }
        fontPath = pullFontPathFromView(context, attrs, new int[] { R.attr.fontPath });
        if(fontPath == null || fontPath.isEmpty()) {
            fontPath = DEFAULT_TYPEFACE;
        }
    }

    /**
     * Tries to pull the Custom Attribute directly from the TextView.
     *
     * @param context Activity Context
     * @param attrs View Attributes
     * @param attributeId if -1 returns null.
     * @return null if attribute is not defined or added to View
     */
    static String pullFontPathFromView(Context context, AttributeSet attrs, int[] attributeId) {
        if (attributeId == null || attrs == null) return null;

        final String attributeName;
        try {
            attributeName = context.getResources().getResourceEntryName(attributeId[0]);
        } catch (Resources.NotFoundException e) {
            // invalid attribute ID
            return null;
        }

        final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return stringResourceId > 0 ? context.getString(stringResourceId)
                : attrs.getAttributeValue(null, attributeName);
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                CalligraphyUtils.applyFontToTextView(getContext(), (TextView) tabViewChild, fontPath);
            }
        }
    }

    public void changeTabsFont(Typeface typeface) {
        if(typeface == null) return;
        ViewGroup vg = (ViewGroup) getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

}

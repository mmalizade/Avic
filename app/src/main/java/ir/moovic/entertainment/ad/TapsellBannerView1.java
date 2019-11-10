package ir.moovic.entertainment.ad;

import android.content.Context;
import android.util.AttributeSet;

import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;
import ir.tapsell.sdk.models.SdkPlatformEnum;

public class TapsellBannerView1 extends TapsellBannerView {
    public TapsellBannerView1(Context context, TapsellBannerType tapsellBannerType, String s) {
        super(context, tapsellBannerType, s);
    }

    public TapsellBannerView1(Context context, TapsellBannerType tapsellBannerType, String s, SdkPlatformEnum sdkPlatformEnum) {
        super(context, tapsellBannerType, s, sdkPlatformEnum);
    }

    public TapsellBannerView1(Context context, TapsellBannerType tapsellBannerType, String s, TapsellBannerViewEventListener tapsellBannerViewEventListener) {
        super(context, tapsellBannerType, s, tapsellBannerViewEventListener);
    }

    public TapsellBannerView1(Context context, int i, String s) {
        super(context, i, s);
    }

    public TapsellBannerView1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TapsellBannerView1(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

}

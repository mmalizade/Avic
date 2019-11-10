package ir.moovic.entertainment.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import ir.moovic.entertainment.ad.TapsellHelper;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;
import ir.tapsell.sdk.AdRequestCallback;
import ir.tapsell.sdk.nativeads.TapsellNativeBannerManager;
import ir.tapsell.sdk.nativeads.TapsellNativeBannerViewManager;

public class NativeBannerViewHolder1 extends ItemViewHolder1 implements AdRequestCallback {

    private WeakReference<Context> contextRef;
    private ViewGroup adContainer;
    TapsellNativeBannerViewManager manager;

    public NativeBannerViewHolder1(View itemView, int adLayoutId) {
        super(itemView);
        this.contextRef = new WeakReference<>(itemView.getContext());

        if(itemView instanceof ViewGroup) {
            adContainer = (ViewGroup) itemView;
            this.manager = new TapsellNativeBannerManager.Builder()
                    .setParentView(adContainer)
                    .setContentViewTemplate(adLayoutId)
                    .inflateTemplate(adContainer.getContext());
        }
    }

    @Override
    protected void onListClick(View v, int position) {

    }

    public void requestAd() {
        Context context = contextRef.get();
        if(context == null || manager == null) return;
        TapsellNativeBannerManager.getAd(context, TapsellHelper.Zone.TAPSELL_NATVIE_BANNER, this);
    }


    @Override
    public void onResponse(String[] adId) {
        Context context = contextRef.get();
        if(context == null || manager == null) return;
        if(adId == null || adId.length == 0) return;
        TapsellNativeBannerManager.bindAd(
                context,
                this.manager,
                TapsellHelper.Zone.TAPSELL_NATVIE_BANNER,
                adId[0]);
    }

    @Override
    public void onFailed(String message) {

    }
}

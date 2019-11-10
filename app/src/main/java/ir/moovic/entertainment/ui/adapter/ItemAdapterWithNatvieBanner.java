package ir.moovic.entertainment.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.utils.recyclerview.ItemAdapter1;
import ir.moovic.entertainment.utils.recyclerview.ItemViewHolder1;

public abstract class ItemAdapterWithNatvieBanner<CONTENT_MODEL> extends ItemAdapter1<CONTENT_MODEL, ItemViewHolder1> {

    private boolean adEnabled = true;
    private int adFirstPos = AdapterConstant.Ad.AD_LIST_FIRST_POS;
    private int adRepater = AdapterConstant.Ad.AD_LIST_REPEATER;
    private int bannerMode = AdapterConstant.Ad.BANNER_MODE_DEFAULT;

    public ItemAdapterWithNatvieBanner enableAd(){
        this.adEnabled = true;
        notifyDataSetChanged();
        return this;
    }

    public ItemAdapterWithNatvieBanner disableAd(){
        this.adEnabled = false;
        notifyDataSetChanged();
        return this;
    }

    public void setAdParams(boolean en, int adFirstPos, int adRepater, int bannerMode) {
        this.adEnabled = en;
        this.adFirstPos = adFirstPos > 0 ? adFirstPos : 0;
        if(adRepater > 1) {
            this.adRepater = adRepater;
        }
        if(this.bannerMode >= 0) {
            this.bannerMode = bannerMode;
        }
        notifyDataSetChanged();
    }

    public boolean isAdEnabled(){
        return this.adEnabled && this.adRepater > 0;
    }


    @NonNull
    @Override
    public ItemViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == AdapterConstant.ViewType.NATIVE_AD_BANNER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getAdContainerViewGroupLayoutId(), parent, false);
            return new NativeBannerViewHolder1(v, getNativeBannerLayoutId());
        } else {
            ViewDataBinding binding = getBinding(parent, viewType);
            return new VH(binding);
        }
    }

    protected int getAdContainerViewGroupLayoutId() {
        return R.layout.list_ad_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder1 holder, int position) {
        try {
            int viewType = getItemViewType(position);
            if(viewType == AdapterConstant.ViewType.NATIVE_AD_BANNER) {
                if(holder instanceof NativeBannerViewHolder1) {
                    NativeBannerViewHolder1 vh = (NativeBannerViewHolder1) holder;
                    vh.requestAd();
                }
            } else {
                super.onBindViewHolder(holder, position);
            }
        } catch (Exception e){}
    }

    @Override
    public int getItemViewType(int position) {
        if(isAd(position)) {
            return AdapterConstant.ViewType.NATIVE_AD_BANNER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public CONTENT_MODEL getItem(int position) {
        if(isAdEnabled()) {
            int first = getAdFirstPosition();
            int repeater = getAdRepeater();
            int adsBefore = 0;
            if( first <= 0 ) {
                adsBefore = position / repeater;
            } else if(position > first) {
                adsBefore = 1 + ((position - first) / repeater);
            }
            return super.getItem(position - adsBefore);
        } else {
            return super.getItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return getAdsCount() + super.getItemCount();
    }

    protected int getAdsCount() {
        if(isAdEnabled()) {
            int first = getAdFirstPosition();
            int items = super.getItemCount();
            if(items < first) {
                return 0;
            } else {
                int cnt = ( (items - first) / getAdRepeater() );
                if(first > 0) {
                    cnt++;
                }
                return cnt > 0 ? cnt : 0;
            }
        }
        return 0;
    }


    public boolean isAd(int position) {
        if(!isAdEnabled() || position == 0) return false;
        int repeater = getAdRepeater();
        int first = getAdFirstPosition();
        return position == first || ( (position-first) % repeater == 0);
    }

    public int getAdRepeater() {
        return this.adRepater > 1 ? adRepater : 0;
    }

    public int getAdFirstPosition() {
        return this.adFirstPos > 0 ? adFirstPos : 0;
    }

    public int getBannerMode(){
        return bannerMode >= 0 ? bannerMode : AdapterConstant.Ad.BANNER_MODE_DEFAULT;
    }

    public void setAdFirstPosition(int adFirstPos) {
        this.adFirstPos = adFirstPos;
        notifyDataSetChanged();
    }

    public void setAdRepater(int adRepater) {
        if(adRepater > 1) {
            this.adRepater = adRepater;
        }
        notifyDataSetChanged();
    }

    public void setBannerMode(int bannerMode) {
        if(bannerMode >= 0) {
            this.bannerMode = bannerMode;
            notifyDataSetChanged();
        }
    }





    public abstract void onclick(View v, int position);
    public abstract int getNativeBannerLayoutId();

    public class VH extends ItemViewHolder1 {

        public VH(ViewDataBinding binding) {
            super(binding);
        }

        @Override
        protected void onListClick(View v, int position) {
            onclick(v, position);
        }

    }
}

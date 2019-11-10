package ir.moovic.entertainment.ui.helper.imageslider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.lang.ref.WeakReference;

import androidx.appcompat.widget.AppCompatImageView;
import ir.moovic.entertainment.R;

public abstract class ImageSliderAdapterBase extends SliderViewAdapter<ImageSliderAdapterBase.VH> {

    private WeakReference<Context> contextRef;

    public ImageSliderAdapterBase(Context context) {
        this.contextRef = new WeakReference<>(context);
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        Context context = contextRef.get();
        if(context == null) return;
        Glide.with(context)
                .load(getImageUrl(position))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .dontAnimate()
                .into(viewHolder.imageView);
    }

    public class VH extends SliderViewAdapter.ViewHolder{
        private AppCompatImageView imageView;

        public VH(View itemView) {
            super(itemView);
            imageView = (AppCompatImageView) itemView;
        }
    }


    protected int getLayoutResId() {
        return R.layout.simple_image_view;
    }

    public abstract String getImageUrl(int position);

}

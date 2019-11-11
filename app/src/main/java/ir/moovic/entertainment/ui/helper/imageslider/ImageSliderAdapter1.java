package ir.moovic.entertainment.ui.helper.imageslider;

import android.content.Context;

import ir.moovic.entertainment.controller.AppConfig;

public class ImageSliderAdapter1 extends ImageSliderAdapterBase {

    private String[] images;

    public ImageSliderAdapter1(Context context) {
        super(context);
    }

    public void setImages(String[] images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public String getImageUrl(int position) {
        try {
            return AppConfig.mediaUrl(images[position]);
        } catch (Exception e){
            return "";
        }
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.length;
    }

}

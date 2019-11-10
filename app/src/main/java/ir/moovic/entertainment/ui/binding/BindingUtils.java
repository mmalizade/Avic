package ir.moovic.entertainment.ui.binding;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.willy.ratingbar.BaseRatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.football.model.MatchModel;
import ir.moovic.entertainment.app.football.ui.MatchInfoView;
import ir.moovic.entertainment.controller.Config;
import ir.moovic.entertainment.ui.adapter.TagAdapter1;
import ir.moovic.entertainment.ui.helper.MyImageView;
import ir.moovic.entertainment.ui.helper.imageslider.ImageSliderAdapter1;
import ir.moovic.entertainment.utils.Utils;


@SuppressLint("UnknownNullness")

public class BindingUtils {
    private static final String TAG = BindingUtils.class.getSimpleName();


    @BindingAdapter("bind:imageUrl")
    public static void bindimageUrl(AppCompatImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(Config.mediaUrl(imageUrl))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.skipMemoryCacheOf(false))
//                .transition(DrawableTransitionOptions.with(new GlideNullTransition()))
                .into(imageView);
    }

    @BindingAdapter("bind:imageUrlCircleCrop")
    public static void bindImageUrlCircleCrop(AppCompatImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(Config.mediaUrl(imageUrl))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    @BindingAdapter("bind:sliderImageUrls")
    public static void bindSliderImageUrl(SliderView sliderView, String[] images) {
        ImageSliderAdapter1 adapter = new ImageSliderAdapter1(sliderView.getContext());
        adapter.setImages(images);
        int count = adapter.getCount();
        try {
            sliderView.setAutoCycle(count > 1);
            if(count > 1) {
                sliderView.startAutoCycle();
            }
            sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        } catch (Exception e){}
        sliderView.setSliderAdapter(adapter);
    }


    @BindingAdapter("bind:webHtml")
    public static void bindWebContent(WebView webView, String html) {
        if(TextUtils.isEmpty(html)) {
            webView.setVisibility(View.GONE);
        } else {
            String text = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style1.css\" />" + html;
            webView.setBackgroundColor(Color.argb(1, 0,0,0));
//            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.loadDataWithBaseURL("file:///android_asset/", text, "text/html", "UTF-8", null);
            webView.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("bind:textHtml")
    public static void bindTextHtml(AppCompatTextView textView, String html) {
        if(TextUtils.isEmpty(html)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(html));
            }
        }
    }

    @BindingAdapter("bind:tagNames")
    public static void bindTagNames(RecyclerView rv, String[] tags) {
        try {
            TagAdapter1 adapter = (TagAdapter1) rv.getAdapter();
            if(adapter != null) {
                adapter.setTags(tags);
            }
        } catch (Exception e){ e.printStackTrace(); }
    }

    @BindingAdapter("bind:newsMediaIcon")
    public static void bindNewsMediaIcon(AppCompatImageView imageView, int mediaType) {
        if(mediaType == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            if(mediaType == 1) {
                Glide.with(imageView.getContext()).load(R.drawable.ic_cam).into(imageView);
            } else if(mediaType == 2){
                Glide.with(imageView.getContext()).load(R.drawable.ic_mic).into(imageView);
            }
        }
    }

    @BindingAdapter("bind:relativeTime")
    public static void bindRelativeTime(AppCompatTextView textView, String time) {
        String t = Utils.relativeTime(textView.getContext(), time);
        textView.setText(t);
    }

    @BindingAdapter("bind:myIvAspectRatio")
    public static void bindMyIvAspectRatio(MyImageView myiv, String aspectRatio) {
        try {
            String[] parts = aspectRatio.split("/");
            myiv.setRatio(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        } catch (Exception e){}
    }

    @BindingAdapter("bind:visible")
    public static void bindVisisble(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("bind:matchModel")
    public static void bindMatchModel(MatchInfoView matchInfoView, MatchModel matchModel) {
        matchInfoView.setMatch(matchModel);
    }

    @BindingAdapter("bind:npvValue")
    public static void bindNumberPickerValue(NumberPickerView npv, int value) {
        try {
            Typeface tf = Typeface.createFromAsset(npv.getResources().getAssets(), npv.getResources().getString(R.string.default_font_bold));
            npv.setContentTextTypeface(tf);
        } catch (Exception e){}

        try {
            npv.setMinValue(0);
            npv.setMaxValue(npv.getResources().getInteger(R.integer.prediction_max_value));
            npv.setValue(value);
        } catch (Exception e){}
    }


    @SuppressLint("WrongConstant")
    @BindingAdapter("bind:teamForm")
    public static void bindTeamForm(LinearLayout layout, String form) {
        layout.removeAllViews();
        if(TextUtils.isEmpty(form)){
            return;
        }

        int len = form.length();
        int padding = layout.getResources().getDimensionPixelSize(R.dimen.content_padding_0);

        for(int i = 0; i < len; i++) {
            String word = form.substring(i, i+1);
            AppCompatTextView tv  = new AppCompatTextView(layout.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(5 * padding, 5 * padding);
            lp.setMargins(padding/2, 0, padding/2, 0);
            tv.setLayoutParams(lp);


            tv.setTypeface(Typeface.MONOSPACE);
            tv.setGravity(Gravity.CENTER);
            tv.setAutoSizeTextTypeWithDefaults(TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tv.setAutoSizeTextTypeUniformWithConfiguration(8, 15, 1, TypedValue.COMPLEX_UNIT_SP);
            tv.setLines(1);
            tv.setMaxLines(1);
            tv.setPadding(padding/2, padding/2, padding/2, padding/2);

            tv.setText(word);
            tv.setTextColor(Color.WHITE);

            switch (word) {
                case "W":
                    tv.setBackgroundResource(R.drawable.circle_bg_green);
                    break;
                case "L":
                    tv.setBackgroundResource(R.drawable.circle_bg_red);
                    break;
                default:
                case "D":
                    tv.setBackgroundResource(R.drawable.circle_bg_grey);
                    break;
            }

            layout.addView(tv);
        }
    }

    @BindingAdapter("bind:mediaDuration")
    public static void bindMediaDurationText(AppCompatTextView textView, int duration) {
        String t = Utils.mediaDuration(duration);
        textView.setText(t);
    }

    @BindingAdapter("app:srb_rating")
    public static void bindRatinBarStar(BaseRatingBar ratingBar, float rating) {
        ratingBar.setRating(rating);
    }


}

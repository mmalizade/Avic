package ir.moovic.entertainment.app.newspaper.model;

import com.google.gson.annotations.SerializedName;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;


public class NewsCategoryModel extends BaseObservable {

    @SerializedName("id")           private long id;
    @SerializedName("title")        private String title;
    @SerializedName("slug")         private String slug;
    @SerializedName("images")       private String[] images;

    private ObservableField<String> imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ObservableField<String> getImageUrl(){
        if(this.imageUrl == null) {
            this.imageUrl = new ObservableField<>();
            try{
                this.imageUrl.set(images[0]);
            } catch (Exception e){}
        }
        return imageUrl;
    }

    @Bindable
    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
        String first = "";
        try {
            first = images[0];
        } catch (Exception e){}

        if(this.imageUrl == null) {
            this.imageUrl = new ObservableField<>();
        }

        this.imageUrl.set(first);

        notifyPropertyChanged(BR.images);
    }
}

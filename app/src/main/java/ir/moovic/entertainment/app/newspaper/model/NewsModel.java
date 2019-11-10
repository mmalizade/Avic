package ir.moovic.entertainment.app.newspaper.model;

import com.google.gson.annotations.SerializedName;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;


public class NewsModel extends BaseObservable implements IListItem1 {

    @SerializedName("id")               private long id;
    @SerializedName("title")            private String title;
    @SerializedName("body")             private String body;
    @SerializedName("categoryId")       private long categoryId;
    @SerializedName("thumbnail")        private String thumbnail;
    @SerializedName("resourceId")       private long resourceId;
    @SerializedName("resourceTitle")    private String resourceTitle;
    @SerializedName("resourceLogo")     private String resourceLogo;
    @SerializedName("isVideoStream")    private boolean isVideoStream;
    @SerializedName("isVocalStream")    private boolean isVocalStream;
    @SerializedName("mediaStreamUrl")   private String mediaStreamUrl;
    @SerializedName("mediaSize")        private String mediaSize;
    @SerializedName("mediaDuration")    private String mediaDuration;
    @SerializedName("isDoc")            private boolean isDoc;
    @SerializedName("isAlbum")          private boolean isAlbum;
    @SerializedName("isImageReport")    private boolean isImageReport;
    @SerializedName("publishTime")      private String publishTime;
    @SerializedName("publishDate")      private String publishDate;
    @SerializedName("images")           private String[] images;
    @SerializedName("tags")             private String[] tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        notifyPropertyChanged(BR.body);
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Bindable
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        notifyPropertyChanged(BR.thumbnail);
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    @Bindable
    public String getResourceTitle() {
        return resourceTitle;

    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
        notifyPropertyChanged(BR.resourceTitle);
    }

    @Bindable
    public String getResourceLogo() {
        return resourceLogo;
    }

    public void setResourceLogo(String resourceLogo) {
        this.resourceLogo = resourceLogo;
        notifyPropertyChanged(BR.resourceLogo);
    }

    public boolean isVideoStream() {
        return isVideoStream;
    }

    public void setVideoStream(boolean videoStream) {
        isVideoStream = videoStream;
    }

    public boolean isVocalStream() {
        return isVocalStream;
    }

    public void setVocalStream(boolean vocalStream) {
        isVocalStream = vocalStream;
    }

    public String getMediaStreamUrl() {
        return mediaStreamUrl;
    }

    public void setMediaStreamUrl(String mediaStreamUrl) {
        this.mediaStreamUrl = mediaStreamUrl;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public boolean isDoc() {
        return isDoc;
    }

    public void setDoc(boolean doc) {
        isDoc = doc;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }

    public boolean isImageReport() {
        return isImageReport;
    }

    public void setImageReport(boolean imageReport) {
        isImageReport = imageReport;
    }

    @Bindable
    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
        notifyPropertyChanged(BR.publishTime);
    }

    @Bindable
    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
        notifyPropertyChanged(BR.publishDate);
    }

    @Bindable
    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
        notifyPropertyChanged(BR.tags);
    }

    @Override
    public int mediaType() {
        if(isVideoStream) {
            return 1;
        } else if(isVocalStream) {
            return 2;
        }
        return 0;
    }

    @Override
    public String getImageUrl() {
        return getThumbnail();
    }

}
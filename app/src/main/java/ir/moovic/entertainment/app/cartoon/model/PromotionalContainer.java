package ir.moovic.entertainment.app.cartoon.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressLint("UnknownNullness")
public class PromotionalContainer implements Parcelable {
    @SerializedName("id")               public long id;
    @SerializedName("video")            public String video;
    @SerializedName("external_video")   public String externalVideo;
    @SerializedName("img")              public String img;
    @SerializedName("video_path")       public String videoPath;
    @SerializedName("banner")           public boolean banner;
    @SerializedName("is_confirmed")     public boolean isConfirmed;
    @SerializedName("priority")         public int priority;
    @SerializedName("product")          public long productId;
    @SerializedName("video_redirect")   public String videoRedirect;

    protected PromotionalContainer(Parcel in) {
        id = in.readLong();
        video = in.readString();
        externalVideo = in.readString();
        img = in.readString();
        videoPath = in.readString();
        banner = in.readByte() != 0;
        isConfirmed = in.readByte() != 0;
        priority = in.readInt();
        productId = in.readLong();
        videoRedirect = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(video);
        dest.writeString(externalVideo);
        dest.writeString(img);
        dest.writeString(videoPath);
        dest.writeByte((byte) (banner ? 1 : 0));
        dest.writeByte((byte) (isConfirmed ? 1 : 0));
        dest.writeInt(priority);
        dest.writeLong(productId);
        dest.writeString(videoRedirect);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PromotionalContainer> CREATOR = new Creator<PromotionalContainer>() {
        @Override
        public PromotionalContainer createFromParcel(Parcel in) {
            return new PromotionalContainer(in);
        }

        @Override
        public PromotionalContainer[] newArray(int size) {
            return new PromotionalContainer[size];
        }
    };
}

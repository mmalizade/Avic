package ir.moovic.entertainment.app.academy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VideoModel implements Parcelable {
    @SerializedName("id")               public long id;
    @SerializedName("title")            public String title;
    @SerializedName("thumbnail")        public String thumbnail;
    @SerializedName("duration")         public int duration;
    @SerializedName("description")      public String description;
    @SerializedName("video")            public String video;
    @SerializedName("lectureIndex")     public int lectureIndex;
    @SerializedName("position")         public int position;

    protected VideoModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        thumbnail = in.readString();
        duration = in.readInt();
        description = in.readString();
        video = in.readString();
        lectureIndex = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeInt(duration);
        dest.writeString(description);
        dest.writeString(video);
        dest.writeInt(lectureIndex);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };
}

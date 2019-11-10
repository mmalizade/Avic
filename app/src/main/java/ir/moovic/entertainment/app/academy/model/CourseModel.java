package ir.moovic.entertainment.app.academy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseModel implements Parcelable {
    @SerializedName("id")               public long id;
    @SerializedName("name")             public String name;
    @SerializedName("cover")            public String cover;
    @SerializedName("banner")           public String banner;
    @SerializedName("trailer")          public String trailer;
    @SerializedName("duration")         public int duration;
    @SerializedName("description")      public String description;
    @SerializedName("instructorId")     public long instructorId;
    @SerializedName("rate")             public RateModel rate;
    @SerializedName("instructor")       public InstructorModel instructor;
    @SerializedName("tags")             public String[] tags;
    @SerializedName("lectures")         public String[] lectures;
    @SerializedName("videoCount")       public int videoCount;
    @SerializedName("bought")           public boolean bought;
    @SerializedName("videos")           public List<VideoModel> videos;

    protected CourseModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        cover = in.readString();
        banner = in.readString();
        trailer = in.readString();
        duration = in.readInt();
        description = in.readString();
        instructorId = in.readLong();
        rate = in.readParcelable(RateModel.class.getClassLoader());
        instructor = in.readParcelable(InstructorModel.class.getClassLoader());
        tags = in.createStringArray();
        lectures = in.createStringArray();
        videoCount = in.readInt();
        bought = in.readByte() != 0;
        videos = in.createTypedArrayList(VideoModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(cover);
        dest.writeString(banner);
        dest.writeString(trailer);
        dest.writeInt(duration);
        dest.writeString(description);
        dest.writeLong(instructorId);
        dest.writeParcelable(rate, flags);
        dest.writeParcelable(instructor, flags);
        dest.writeStringArray(tags);
        dest.writeStringArray(lectures);
        dest.writeInt(videoCount);
        dest.writeByte((byte) (bought ? 1 : 0));
        dest.writeTypedList(videos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseModel> CREATOR = new Creator<CourseModel>() {
        @Override
        public CourseModel createFromParcel(Parcel in) {
            return new CourseModel(in);
        }

        @Override
        public CourseModel[] newArray(int size) {
            return new CourseModel[size];
        }
    };
}

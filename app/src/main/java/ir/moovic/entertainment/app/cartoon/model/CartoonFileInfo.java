package ir.moovic.entertainment.app.cartoon.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressLint("UnknownNullness")
public class CartoonFileInfo implements Parcelable {
    @SerializedName("id")               public long id;
    @SerializedName("name")             public String name;
    @SerializedName("file")             public String file;
    @SerializedName("img")              public String img;
    @SerializedName("price")            public Integer price;
    @SerializedName("description")      public String description;
    @SerializedName("sub_fa")           public String subFa;
    @SerializedName("sub_en")           public String subEn;
    @SerializedName("is_downloadable")  public boolean isDownloadable;
    @SerializedName("is_enable")        public boolean isEnable;
    @SerializedName("preview")          public String preview;

    protected CartoonFileInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        file = in.readString();
        img = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        description = in.readString();
        subFa = in.readString();
        subEn = in.readString();
        isDownloadable = in.readByte() != 0;
        isEnable = in.readByte() != 0;
        preview = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(file);
        dest.writeString(img);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeString(description);
        dest.writeString(subFa);
        dest.writeString(subEn);
        dest.writeByte((byte) (isDownloadable ? 1 : 0));
        dest.writeByte((byte) (isEnable ? 1 : 0));
        dest.writeString(preview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartoonFileInfo> CREATOR = new Creator<CartoonFileInfo>() {
        @Override
        public CartoonFileInfo createFromParcel(Parcel in) {
            return new CartoonFileInfo(in);
        }

        @Override
        public CartoonFileInfo[] newArray(int size) {
            return new CartoonFileInfo[size];
        }
    };
}

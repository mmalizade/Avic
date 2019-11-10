package ir.moovic.entertainment.app.cartoon.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressLint("UnknownNullness")
public class ProductModel implements Parcelable {
    @SerializedName("id")                       public long id;
    @SerializedName("name")                     public String name;
    @SerializedName("nameEnglish")              public String nameEnglish;
    @SerializedName("productType")              public int productType;
    @SerializedName("producerId")               public long producerId;
    @SerializedName("producerName")             public String producerName;
    @SerializedName("category")                 public long[] category;
    @SerializedName("categories")               public List<CategoryModel> categories;
    @SerializedName("price")                    public Integer price;
    @SerializedName("avatar")                   public String avatar;
    @SerializedName("feature_avatar")           public String featureAvatar;
    @SerializedName("rank")                     public float rank;
    @SerializedName("shortDescription")         public String shortDescription;
    @SerializedName("description")              public String description;
    @SerializedName("approvedAge")              public int approvedAge;
    @SerializedName("promotionalContainers")    public List<PromotionalContainer> promotionalContainers;
    @SerializedName("files")                    public List<CartoonFileInfo> files;
    @SerializedName("tags")                     public String[] tags;

    protected ProductModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        nameEnglish = in.readString();
        productType = in.readInt();
        producerId = in.readLong();
        producerName = in.readString();
        category = in.createLongArray();
        categories = in.createTypedArrayList(CategoryModel.CREATOR);
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        avatar = in.readString();
        featureAvatar = in.readString();
        rank = in.readFloat();
        shortDescription = in.readString();
        description = in.readString();
        approvedAge = in.readInt();
        promotionalContainers = in.createTypedArrayList(PromotionalContainer.CREATOR);
        files = in.createTypedArrayList(CartoonFileInfo.CREATOR);
        tags = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(nameEnglish);
        dest.writeInt(productType);
        dest.writeLong(producerId);
        dest.writeString(producerName);
        dest.writeLongArray(category);
        dest.writeTypedList(categories);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeString(avatar);
        dest.writeString(featureAvatar);
        dest.writeFloat(rank);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeInt(approvedAge);
        dest.writeTypedList(promotionalContainers);
        dest.writeTypedList(files);
        dest.writeStringArray(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String description(){
        StringBuilder sb = new StringBuilder();
        if(!TextUtils.isEmpty(shortDescription)) {
            sb.append(shortDescription);
            sb.append("\n");
        }
        if(!TextUtils.isEmpty(description)) {
            sb.append(description);
        }
        return sb.toString();
    }

    public boolean hasFileSerie(){
        return files != null && files.size() > 1;
    }


    public String getPromotionalImageUrl() {
        if(promotionalContainers == null || promotionalContainers.isEmpty()) return null;
        String img = promotionalContainers.get(0).img;
        if(TextUtils.isEmpty(img)) return null;
        return img;
    }


}

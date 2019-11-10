package ir.moovic.entertainment.app.cartoon.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressLint("UnknownNullness")
public class ResBundle1 {
    @SerializedName("title")        public String title;
    @SerializedName("rowType")      public int rowType;
    @SerializedName("packageId")    public long packageId;
    @SerializedName("packageType")  public String packageType;
    @SerializedName("products")     public List<ProductModel> products;
}

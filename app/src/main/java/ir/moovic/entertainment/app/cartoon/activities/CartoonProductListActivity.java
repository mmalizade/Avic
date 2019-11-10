package ir.moovic.entertainment.app.cartoon.activities;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.cartoon.fragments.FrProductList;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;

public class CartoonProductListActivity extends MyBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_product_list);

        Bundle extras = getIntent().getExtras();
        FrProductList fr = FrProductList.newInstance(extras);
        animatedReplaceFragment(fr, R.id.container, false);

        String t = getIntent().getStringExtra("listName");
        if(!TextUtils.isEmpty(t)) {
            setToolbarTitle(t);
        }
    }

}

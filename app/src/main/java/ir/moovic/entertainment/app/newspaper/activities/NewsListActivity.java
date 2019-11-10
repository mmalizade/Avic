package ir.moovic.entertainment.app.newspaper.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.newspaper.fragments.FrNewsList;

public class NewsListActivity extends MyBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Bundle extras = getIntent().getExtras();
        FrNewsList fr = FrNewsList.newInstance(extras);
        animatedReplaceFragment(fr, R.id.container_1, false);
    }

}

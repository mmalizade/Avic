package ir.moovic.entertainment.app.cartoon.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.cartoon.fragments.FrHomePage;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;

public class CartoonActivity extends MyBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon);
        setToolbarTitle(getString(R.string.donyaye_animation));
        animatedReplaceFragment(new FrHomePage(), R.id.container, false);
    }

}

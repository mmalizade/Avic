package ir.moovic.entertainment.app.football.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.base.pagecntrl.IFootballPageController;
import ir.moovic.entertainment.app.football.fragments.FrLeagues;

public class FootballActivity extends IFootballPageController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football);
        showFragment(new FrLeagues(), false);
    }

    @Override
    public void showFragment(Fragment fragment, boolean addToBackStack) {
        animatedReplaceFragment(fragment, R.id.container, addToBackStack);
    }


}

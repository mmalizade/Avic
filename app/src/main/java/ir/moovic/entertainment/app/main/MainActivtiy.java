package ir.moovic.entertainment.app.main;

import android.os.Bundle;

import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.base.pagecntrl.IMainPageController;
import ir.moovic.entertainment.app.main.fragments.FrHomePage;

public class MainActivtiy extends IMainPageController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        animatedReplaceFragment(new FrHomePage(), R.id.container_1, false);
    }


}

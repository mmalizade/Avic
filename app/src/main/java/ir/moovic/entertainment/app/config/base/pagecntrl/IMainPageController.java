package ir.moovic.entertainment.app.config.base.pagecntrl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import ir.moovic.entertainment.app.config.base.IPageController;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.main.models.ResBundle;
import ir.moovic.entertainment.utils.Utils;

@SuppressLint("Registered")
public class IMainPageController extends MyBaseActivity implements IPageController {

    @Override
    public void showFragment(Fragment fragment, boolean addToBackStack) {

    }

    @Override
    public void showLoagueInfo(LeagueModel model, String tag) {

    }

    @Override
    public void controlHomeTarget(ResBundle item) {
        if(item == null) return;
        Intent intent = null;
        String activity = getApplicationContext().getPackageName() + item.activity;
        try {
            Class<?> c = Class.forName(activity);
            intent = new Intent(this, c);
        } catch (ClassNotFoundException e) {
            Log.e("IMainPageController", "controlHomeTarget: " + e.getMessage());
        }

        if(intent != null) {
            Bundle extras = Utils.bundleFromJsonObject(item.params);
            if(extras != null) {
                intent.putExtras(extras);
            }
            startActivity(intent);
        }
    }



}

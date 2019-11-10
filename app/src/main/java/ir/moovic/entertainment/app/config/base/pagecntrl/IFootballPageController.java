package ir.moovic.entertainment.app.config.base.pagecntrl;

import androidx.fragment.app.Fragment;
import ir.moovic.entertainment.app.config.base.IPageController;
import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.main.models.ResBundle;

public class IFootballPageController extends MyBaseActivity implements IPageController {

    @Override
    public void showFragment(Fragment fragment, boolean addToBackStack) {

    }

    @Override
    public void showLoagueInfo(LeagueModel model, String tag) {

    }

    @Override
    public void controlHomeTarget(ResBundle item) {

    }

}

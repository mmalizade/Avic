package ir.moovic.entertainment.app.config.base;

import androidx.fragment.app.Fragment;
import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.main.models.ResBundle;

public interface IPageController {
    void showFragment(Fragment fragment, boolean addToBackStack);

    void showLoagueInfo(LeagueModel model, String tag);

    void controlHomeTarget(ResBundle item);
}

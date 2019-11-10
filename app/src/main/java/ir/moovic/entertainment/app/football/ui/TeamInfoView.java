package ir.moovic.entertainment.app.football.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.football.model.TeamModel;
import ir.moovic.entertainment.controller.Config;


public class TeamInfoView extends LinearLayout {

    private AppCompatImageView iv_logo;
    private AppCompatTextView tv_teamName;


    public TeamInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_team_info, this);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        iv_logo = findViewById(R.id.logo);
        tv_teamName = findViewById(R.id.teamname);
    }

    public void showTeamInfo(TeamModel team) {
        if(team == null) return;
        tv_teamName.setText(team.title);
        Glide.with(getContext())
                .load(Config.mediaUrl(team.logo))
                .apply(new RequestOptions().fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(iv_logo);
    }



}

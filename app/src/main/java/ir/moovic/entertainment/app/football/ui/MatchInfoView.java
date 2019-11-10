package ir.moovic.entertainment.app.football.ui;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.football.model.MatchModel;

public class MatchInfoView extends CardView {

    TeamInfoView teamInfoGuest, teamInfoHost;
//    AppCompatTextView tvTitle, tvGameTime;
    AppCompatTextView tvRealScore;
    AppCompatTextView tvPredictionScore;

    private MatchModel match;

    private boolean showGameDateInTitle = true;

    public MatchInfoView(Context context) {
        this(context, null);
    }

    public MatchInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_match_info, this);
        int cardBgColor = ContextCompat.getColor(context, R.color.match_list_item_bg_color);
        int cornerRadius = context.getResources().getDimensionPixelSize(R.dimen.view_match_info_corner_radius);
        int elavation = context.getResources().getDimensionPixelSize(R.dimen.match_list_item_elevation);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.match_info_view_padding);
        setCardBackgroundColor(cardBgColor);
        setRadius(cornerRadius);
        setCardElevation(elavation);
        setContentPadding(padding, padding, padding, padding);

//        tvTitle = findViewById(R.id.tv_title);
//        tvGameTime = findViewById(R.id.tv_gametime);
        teamInfoHost = findViewById(R.id.team_info_host);
        teamInfoGuest = findViewById(R.id.team_info_guest);
        tvPredictionScore = findViewById(R.id.tv_prediction_score);
        tvRealScore = findViewById(R.id.tv_real_score);
        showGameDateInTitle = true;
    }


    public void setMatch(MatchModel match){
        this.match = match;
        refresh();
    }

    public void setShowGameDateInTitle(boolean showGameDateInTitle){
        this.showGameDateInTitle = showGameDateInTitle;
        refreshTitle();
    }

    private void refreshTitle(){

    }

    public void refresh() {
        if(match == null){
            return;
        }

//        tvGameTime.setText(match.gameTime);
        // team name , logo
        teamInfoHost.showTeamInfo(match.hostTeam);
        teamInfoGuest.showTeamInfo(match.guestTeam);
        // title
        refreshTitle();

        setRealScores(match.hostScore, match.guestScore);
        try {
            setPredictScores(match.prediction.hostScore, match.prediction.guestScore);
        } catch (Exception e) {
            setPredictScores(null, null);
        }

    }

    public void setPredictScores(Integer hostScore, Integer guestScore) {
        String s = guestScore == null ? "-" : String.valueOf(guestScore);
        s += " : ";
        s += hostScore == null ? "-" : String.valueOf(hostScore);
        tvPredictionScore.setText(s);
    }

    public void setRealScores(Integer hostScore, Integer guestScore) {
        String s = guestScore == null ? "-" : String.valueOf(guestScore);
        s += " : ";
        s += hostScore == null ? "-" : String.valueOf(hostScore);
        tvRealScore.setText(s);
    }

    public MatchModel getMatch() {
        return match;
    }

}

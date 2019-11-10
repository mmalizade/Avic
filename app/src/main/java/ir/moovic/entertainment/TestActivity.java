package ir.moovic.entertainment;

import android.content.Intent;
import android.os.Bundle;

import ir.moovic.entertainment.app.config.base.MyBaseActivity;
import ir.moovic.entertainment.app.football.activities.FootballActivity;

public class TestActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        startActivity(new Intent(this, FootballActivity.class));
        finish();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

package ir.moovic.entertainment.app.football.network;

import android.annotation.SuppressLint;

import java.util.List;

import ir.moovic.entertainment.app.football.model.LeagueModel;
import ir.moovic.entertainment.app.football.model.MatchModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@SuppressLint("UnknownNullness")
public interface FootballApiService {

    @GET("league")
    Call<List<LeagueModel>> leagues();

    @GET("league/{id}")
    Call<LeagueModel> league(@Path("id") long id);

    @GET("league/{id}/standing")
    Call<LeagueModel> leagueStanding(@Path("id") long id);

    @GET("league/{id}/period/{period}/matches")
    Call<List<MatchModel>> leaguePeriodMatches(@Path("id") long leagueId, @Path("period") int period);

}

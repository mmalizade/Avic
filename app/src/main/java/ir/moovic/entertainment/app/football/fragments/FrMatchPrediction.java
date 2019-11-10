package ir.moovic.entertainment.app.football.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import butterknife.BindView;
import butterknife.OnClick;
import ir.moovic.entertainment.R;
import ir.moovic.entertainment.app.config.base.MyBaseFragment;
import ir.moovic.entertainment.app.football.model.MatchModel;
import ir.moovic.entertainment.ui.helper.MyButton;

@SuppressLint("UnknownNullness")
public class FrMatchPrediction extends MyBaseFragment {

    @BindView(R.id.btn_submit)  MyButton btnSubmit;

    ViewDataBinding binding;
    MatchModel match;


    @NonNull
    public static FrMatchPrediction newInstance(@NonNull MatchModel match) {
        FrMatchPrediction fr = new FrMatchPrediction();
        Bundle args = new Bundle();
        args.putParcelable("match", match);
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            match = args.getParcelable("match");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_match_prediction, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setVariable(BR.obj, match);
        binding.executePendingBindings();
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit(View v) {
        loading(true);
    }

    @Override
    public void loading(boolean show) {
        btnSubmit.progress(show);
    }
}

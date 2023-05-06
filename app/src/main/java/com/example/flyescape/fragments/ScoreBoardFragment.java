package com.example.flyescape.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyescape.Adapters.ScoreAdapter;
import com.example.flyescape.App;
import com.example.flyescape.R;
import com.example.flyescape.interfaces.CallBack_SendClick;
import com.example.flyescape.model.Score;
import com.example.flyescape.utilities.DataManager;
import com.example.flyescape.utilities.MySPv3;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScoreBoardFragment extends Fragment {

    private RecyclerView scoreboard_LST_scores;

    private CallBack_SendClick callBack_sendClick;

    public void setCallBack_sendClick(CallBack_SendClick callBack_sendClick){
        this.callBack_sendClick = callBack_sendClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_board, container, false);
        findViews(view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        String json = MySPv3.getInstance().getString(App.KEY,"");
        DataManager dataManager = new Gson().fromJson(json,DataManager.class);
        ArrayList<Score> scores = dataManager.getScores();
        ScoreAdapter scoreAdapter = new ScoreAdapter(scores);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        scoreboard_LST_scores.setAdapter(scoreAdapter);
        scoreboard_LST_scores.setLayoutManager(linearLayoutManager);

        scoreAdapter.setCallBack_sendClick(callBack_sendClick);
    }

    private void findViews(View view) {
        scoreboard_LST_scores = view.findViewById(R.id.scoreboard_LST_scores);
    }
}
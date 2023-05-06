package com.example.flyescape.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyescape.R;
import com.example.flyescape.interfaces.CallBack_SendClick;
import com.example.flyescape.model.Score;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<Score> scores;
    public CallBack_SendClick callBack_sendClick;

    public void setCallBack_sendClick(CallBack_SendClick callBack_sendClick){
        this.callBack_sendClick = callBack_sendClick;
    }
    public ScoreAdapter(ArrayList<Score> scores)
    {
        this.scores = scores;
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(view);
        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreAdapter.ScoreViewHolder holder, int position) {
        Score score = getItem(position);
        holder.score_LBL_name.setText(score.getName().toUpperCase());
        holder.score_LBL_rank.setText(score.getPlace() + "");
        holder.score_LBL_score.setText("Score : "+score.getScore());
        holder.score_LBL_xcord.setText("X - "+score.getX());
        holder.score_LBL_ycord.setText("Y - "+score.getY());
    }

    @Override
    public int getItemCount() {
        return this.scores == null ? 0 : this.scores.size();
    }

    private Score getItem(int position) {
        return this.scores.get(position);
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView score_LBL_name;

        private MaterialTextView score_LBL_rank;

        private MaterialTextView score_LBL_score;

        private MaterialTextView score_LBL_xcord;

        private MaterialTextView score_LBL_ycord;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            score_LBL_name = itemView.findViewById(R.id.score_LBL_name);
            score_LBL_rank = itemView.findViewById(R.id.score_LBL_rank);
            score_LBL_score = itemView.findViewById(R.id.score_LBL_score);
            score_LBL_xcord = itemView.findViewById(R.id.score_LBL_xcord);
            score_LBL_ycord = itemView.findViewById(R.id.score_LBL_ycord);
            itemView.setOnClickListener(v -> {
                if(callBack_sendClick != null)
                    callBack_sendClick.scoreChosen(
                            getItem(getAdapterPosition()).getPlace(),
                            getItem(getAdapterPosition()).getX(),
                            getItem(getAdapterPosition()).getY());
            });
        }
    }
}

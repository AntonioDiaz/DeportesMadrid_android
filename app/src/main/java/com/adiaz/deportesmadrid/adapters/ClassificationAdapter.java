package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.ClassificationRetrofit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ViewHolder> {

    Context mContext;
    List<ClassificationRetrofit> mClassificationList;
    String mIdTeam;

    public ClassificationAdapter(Context mContext, List<ClassificationRetrofit> mClassificationList, String idTeam) {
        this.mContext = mContext;
        this.mClassificationList = mClassificationList;
        this.mIdTeam = idTeam;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_classification, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.tvPosition.setText("");
            holder.tvTeam.setText(mContext.getString(R.string.classification_header_team));
            holder.tvMatches.setText(mContext.getString(R.string.classification_header_matches));
            holder.tvMatchesWon.setText(mContext.getString(R.string.classification_header_matches_won));
            holder.tvMatchesDrawn.setText(mContext.getString(R.string.classification_header_matches_drawn));
            holder.tvMatchesLost.setText(mContext.getString(R.string.classification_header_matches_lost));
            holder.tvGoalsFor.setText(mContext.getString(R.string.classification_header_goals_favor));
            holder.tvGoalsAgainst.setText(mContext.getString(R.string.classification_header_goals_against));
            holder.tvPoints.setText(mContext.getString(R.string.classification_header_points));
            int color = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
            int colorWhite = ContextCompat.getColor(mContext, R.color.colorWhite);
            holder.clClassification.setBackgroundColor(color);
            holder.tvTeam.setTextColor(colorWhite);
            holder.tvMatches.setTextColor(colorWhite);
            holder.tvMatchesWon.setTextColor(colorWhite);
            holder.tvMatchesDrawn.setTextColor(colorWhite);
            holder.tvMatchesLost.setTextColor(colorWhite);
            holder.tvGoalsFor.setTextColor(colorWhite);
            holder.tvGoalsAgainst.setTextColor(colorWhite);
            holder.tvPoints.setTextColor(colorWhite);
        } else {
            ClassificationRetrofit entity = mClassificationList.get(position - 1);
            if (entity != null) {
                holder.tvPosition.setText(entity.getPosition().toString());
                if (entity.getTeam() != null && entity.getTeam().getName() != null) {
                    holder.tvTeam.setText(entity.getTeam().getName().toString());
                }
                holder.tvMatches.setText(entity.getMatchesPlayed().toString());
                holder.tvMatchesWon.setText(entity.getMatchesWon().toString());
                holder.tvMatchesDrawn.setText(entity.getMatchesDrawn().toString());
                holder.tvMatchesLost.setText(entity.getMatchesLost().toString());
                holder.tvGoalsFor.setText(entity.getPointsFavor().toString());
                holder.tvGoalsAgainst.setText(entity.getPointsAgainst().toString());
                holder.tvPoints.setText(entity.getPoints().toString());
                if (entity.getTeam() != null && entity.getTeam().getName() != null && entity.getTeam().getName().equals(mIdTeam)) {
                    int color = ContextCompat.getColor(mContext, R.color.colorAccent);
                    holder.clClassification.setBackgroundColor(color);
                    holder.tvTeam.setTypeface(null, Typeface.BOLD);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.mClassificationList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_classification)
        ConstraintLayout clClassification;

        @BindView(R.id.tv_classification_position)
        TextView tvPosition;

        @BindView(R.id.tv_classification_team)
        TextView tvTeam;

        @BindView(R.id.tv_classification_matches)
        TextView tvMatches;

        @BindView(R.id.tv_classification_matches_won)
        TextView tvMatchesWon;

        @BindView(R.id.tv_classification_matches_drawn)
        TextView tvMatchesDrawn;

        @BindView(R.id.tv_classification_matches_lost)
        TextView tvMatchesLost;

        @BindView(R.id.tv_classification_goals_for)
        TextView tvGoalsFor;

        @BindView(R.id.tv_classification_goals_against)
        TextView tvGoalsAgainst;

        @BindView(R.id.tv_classification_points)
        TextView tvPoints;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

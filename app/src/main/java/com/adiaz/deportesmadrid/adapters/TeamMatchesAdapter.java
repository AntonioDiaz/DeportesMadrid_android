package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.entities.Match;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.StateAnnotation;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamMatchesAdapter extends RecyclerView.Adapter<TeamMatchesAdapter.ViewHolder> {

    public static final String TAG = TeamMatchesAdapter.class.getSimpleName();

    Context mContext;
    List<MatchRetrofit> mMatchList;
    String mTeamId;

    public TeamMatchesAdapter(Context context, List<MatchRetrofit> matchList, String mTeamId) {
        this.mContext = context;
        this.mMatchList = matchList;
        this.mTeamId = mTeamId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_team_match, parent, false);
        return new TeamMatchesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchRetrofit match = mMatchList.get(position);
        holder.tvWeek.setText(mContext.getString(R.string.calendar_week, match.getNumWeek()));
        String teamLocal = Constants.FIELD_EMPTY;
        if (match.getTeamLocal()!=null && StringUtils.isNotEmpty(match.getTeamLocal().getName())) {
            teamLocal = match.getTeamLocal().getName();
        }
        String teamVisitor = Constants.FIELD_EMPTY;
        if (match.getTeamVisitor()!=null && StringUtils.isNotEmpty(match.getTeamVisitor().getName())) {
            teamVisitor = match.getTeamVisitor().getName();
        }
        /* check if a team is resting */
        if (teamLocal.equals(Constants.FIELD_EMPTY) && !teamVisitor.equals(Constants.FIELD_EMPTY)) {
            teamLocal = mContext.getString(R.string.RESTING);
        }
        if (teamVisitor.equals(Constants.FIELD_EMPTY) && !teamLocal.equals(Constants.FIELD_EMPTY)) {
            teamVisitor = mContext.getString(R.string.RESTING);
        }
        boolean isLocal = false;
        if (match.getTeamLocal() != null && match.getTeamLocal().getName().equalsIgnoreCase(mTeamId)) {
            isLocal = true;
        }
        if (isLocal) {
            holder.tvLocalOrVisitor.setText(mContext.getString(R.string.calendar_local));
            holder.tvOpponent.setText(teamVisitor);
        } else {
            holder.tvLocalOrVisitor.setText(mContext.getString(R.string.calendar_visitor));
            holder.tvOpponent.setText(teamLocal);
        }
        String dateStr = Constants.FIELD_EMPTY;
        if (match.getDate()!=null) {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            dateStr = dateFormat.format(match.getDate());
        }
        holder.tvDate.setText(dateStr);
        String placeName = Constants.FIELD_EMPTY;
        if (match.getPlace()!=null) {
            placeName = match.getPlace().getName();
        }
        holder.tvPlace.setText(placeName);
        holder.tvState.setText(StateAnnotation.stringKey(match.getState()));
        if (match.getState()== StateAnnotation.FINALIZADO) {
            holder.tvScore.setText(mContext.getString(R.string.calendar_score, match.getScoreLocal(), match.getScoreVisitor()));
            int scoreTeam;
            int scoreTeamOpponent;
            if (isLocal) {
                scoreTeam = match.getScoreLocal();
                scoreTeamOpponent = match.getScoreVisitor();
            } else {
                scoreTeam = match.getScoreVisitor();
                scoreTeamOpponent = match.getScoreLocal();
            }
            if (scoreTeam>scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_up);
            } else if(scoreTeam<scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_down);
            } else {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumbs_up_down);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_week)
        TextView tvWeek;

        @BindView(R.id.tv_local_visitor)
        TextView tvLocalOrVisitor;

        @BindView(R.id.tv_opponent)
        TextView tvOpponent;

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_place)
        TextView tvPlace;

        @BindView(R.id.tv_state)
        TextView tvState;

        @BindView(R.id.tv_score)
        TextView tvScore;

        @BindView(R.id.iv_thumb_victory)
        ImageView ivThumbVictory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.StateAnnotation;
import com.adiaz.deportesmadrid.utils.Utils;

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

    final private ListItemClickListener mListItemClickListener;

    public TeamMatchesAdapter(Context context, List<MatchRetrofit> matchList, String mTeamId, ListItemClickListener listItemClickListener) {
        this.mContext = context;
        this.mMatchList = matchList;
        this.mTeamId = mTeamId;
        this.mListItemClickListener = listItemClickListener;
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
        if (match.getDate()!=null && match.getState()!=StateAnnotation.DESCANSA) {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            dateStr = dateFormat.format(match.getDate());
        }
        holder.tvDate.setText(dateStr);
        String placeName = Constants.FIELD_EMPTY;
        if (match.getPlace()!=null) {
            placeName = match.getPlace().getName();
        }
        holder.tvPlace.setText(placeName);
        holder.tvState.setText(Utils.getStringResourceByName(mContext, StateAnnotation.stringKey(match.getState())));
        if (match.getState()== StateAnnotation.FINALIZADO || match.getState() == StateAnnotation.NO_PRESENTADO) {
            holder.tvScore.setText(mContext.getString(R.string.calendar_score, match.getScoreLocal(), match.getScoreVisitor()));
            holder.tvScore.setVisibility(View.VISIBLE);
            int scoreTeam;
            int scoreTeamOpponent;
            if (isLocal) {
                scoreTeam = match.getScoreLocal();
                scoreTeamOpponent = match.getScoreVisitor();
            } else {
                scoreTeam = match.getScoreVisitor();
                scoreTeamOpponent = match.getScoreLocal();
            }
            holder.ivThumbVictory.setVisibility(View.VISIBLE);
            if (scoreTeam > scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_up);
            } else if(scoreTeam<scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_down);
            } else {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumbs_up_down);
            }
        } else {
            holder.tvScore.setVisibility(View.INVISIBLE);
            holder.ivThumbVictory.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cl_teamcalendar)
        View vTeamCalendar;

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
            vTeamCalendar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
            mListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}

package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.entities.Match;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamMatchesAdapter extends RecyclerView.Adapter<TeamMatchesAdapter.ViewHolder> {

    Context mContext;
    List<MatchRetrofit> mMatchList;

    public TeamMatchesAdapter(Context context, List<MatchRetrofit> matchList) {
        this.mContext = context;
        this.mMatchList = matchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_team_match, parent, false);
        return new TeamMatchesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchRetrofit matchRetrofit = mMatchList.get(position);
        String teamLocal = Constants.FIELD_EMPTY;
        if (matchRetrofit.getTeamLocal()!=null) {
            teamLocal = matchRetrofit.getTeamLocal().getName();
        }
        String teamVisitor = Constants.FIELD_EMPTY;
        if (matchRetrofit.getTeamVisitor()!=null) {
            teamVisitor = matchRetrofit.getTeamVisitor().getName();
        }
        holder.tvTeamLocal.setText(teamLocal);
        holder.tvTeamVisitor.setText(teamVisitor);
    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_team_local)
        TextView tvTeamLocal;

        @BindView(R.id.tv_team_visitor)
        TextView tvTeamVisitor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

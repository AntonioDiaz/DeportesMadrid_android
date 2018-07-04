package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.entities.TeamEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamSearchAdapter extends RecyclerView.Adapter<TeamSearchAdapter.ViewHolder> {

    Context mContext;
    TeamSearchAdapter.ListItemClickListener mListItemClickListener;
    List<TeamEntity> mTeams;

    public TeamSearchAdapter(Context mContext, ListItemClickListener mListItemClickListener, List<TeamEntity> mTeams) {
        this.mContext = mContext;
        this.mListItemClickListener = mListItemClickListener;
        this.mTeams = mTeams;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_favorites_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mTeams !=null && mTeams.size()>position) {
            Group group = GroupsDAO.queryCompetitionsById(mContext, mTeams.get(position).getIdGroup());
            holder.tvGroup.setText(group.nomGrupo());
            holder.tvFase.setText(group.nomFase());
            holder.tvCompetition.setText(group.nomCompeticion());
            String teamName = mTeams.get(position).getTeamName();
            if (teamName!=null) {
                holder.tvTeam.setText(teamName);
            }
            holder.tvPath.setText(group.deporte() + Constants.PATH_SEPARATOR + group.distrito() + Constants.PATH_SEPARATOR + group.categoria());
        }
    }

    @Override
    public int getItemCount() {
        return mTeams.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cv_favorites)
        CardView cvFavorites;

        @Nullable
        @BindView(R.id.tv_team)
        TextView tvTeam;

        @Nullable
        @BindView(R.id.tv_group)
        TextView tvGroup;

        @Nullable
        @BindView(R.id.tv_fase)
        TextView tvFase;

        @Nullable
        @BindView(R.id.tv_competition)
        TextView tvCompetition;

        @Nullable
        @BindView(R.id.tv_path)
        TextView tvPath;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cvFavorites.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListItemClickListener.onListItemClickTeamSearch(getAdapterPosition());
        }
    }

    public interface ListItemClickListener {
        void onListItemClickTeamSearch(int clickedItemIndex);
    }
}

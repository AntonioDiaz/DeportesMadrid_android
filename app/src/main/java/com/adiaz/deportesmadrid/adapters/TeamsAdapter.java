package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder>{

    Context mContext;
    String idCompetition;
    List<String> teamsList;

    public TeamsAdapter(Context context, String idCompetition, ArrayList<String> teamsList) {
        this.mContext = context;
        this.idCompetition = idCompetition;
        this.teamsList = teamsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_team, parent, false);
        return new TeamsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String teamName = teamsList.get(position);
        holder.tvTeamName.setText(teamName);
        holder.ivHearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Favorite favoriteTeam = FavoritesDAO.queryFavoritesTeam(mContext, idCompetition, teamName);
                if (favoriteTeam==null) {
                    holder.ivHearth.setImageResource(R.drawable.ic_favorite_fill);
                    Favorite favorite = Favorite.builder()
                            .idCompetition(idCompetition)
                            .idTeam(teamName)
                            .build();

                    FavoritesDAO.insertFavorite(mContext, favorite);
                    Toast.makeText(mContext, mContext.getString(R.string.favorites_team_added), Toast.LENGTH_SHORT).show();
                } else {
                    holder.ivHearth.setImageResource(R.drawable.ic_favorite_empty);
                    FavoritesDAO.deleteFavorite(mContext, favoriteTeam.id());
                    Toast.makeText(mContext, mContext.getString(R.string.favorites_team_removed), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (FavoritesDAO.queryFavoritesTeam(mContext, idCompetition, teamName)!=null) {
            holder.ivHearth.setImageResource(R.drawable.ic_favorite_fill);
        } else {
            holder.ivHearth.setImageResource(R.drawable.ic_favorite_empty);
        }

    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_team_name)
        TextView tvTeamName;

        @BindView(R.id.iv_hearth)
        ImageView ivHearth;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.daos.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.db.entities.Favorite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    Context mContext;
    FavoritesAdapter.ListItemClickListener mListItemClickListener;
    List<Favorite> mFavoritesList;

    public FavoritesAdapter(Context context, ListItemClickListener listItemClickListener, List<Favorite> mFavoritesList) {
        this.mContext = context;
        this.mFavoritesList = mFavoritesList;
        this.mListItemClickListener = listItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_favorites_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mFavoritesList!=null) {

            Competition competition = CompetitionsDAO.queryCompetitionsById(mContext, mFavoritesList.get(position).idCompetition());
            String competitionName = competition.nomGrupo();
            String teamName = mFavoritesList.get(position).idTeam();
            if (teamName!=null) {
                holder.tvFavoriteType.setText(mContext.getString(R.string.type_team));
                holder.tvFavoriteName.setText(teamName + "\n" + competitionName);
            } else {
                holder.tvFavoriteType.setText(mContext.getString(R.string.type_competicion));
                holder.tvFavoriteName.setText(competitionName);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFavoritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_favorite_type)
        TextView tvFavoriteType;

        @BindView(R.id.tv_favorite_name)
        TextView tvFavoriteName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}

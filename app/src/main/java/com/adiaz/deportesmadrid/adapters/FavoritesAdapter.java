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
import com.adiaz.deportesmadrid.db.entities.Favorite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    Context mContext;
    FavoritesAdapter.ListItemClickListener mListItemClickListener;
    List<Favorite> mFavoritesList;

    private static final Integer TYPE_TEAM = 1;
    private static final Integer TYPE_GROUP = 2;

    public FavoritesAdapter(Context context, ListItemClickListener listItemClickListener, List<Favorite> mFavoritesList) {
        this.mContext = context;
        this.mFavoritesList = mFavoritesList;
        this.mListItemClickListener = listItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.listitem_favorites_team;
        if (viewType==TYPE_GROUP) {
            layoutId = R.layout.listitem_favorites_group;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mFavoritesList!=null) {
            Group group = GroupsDAO.queryCompetitionsById(mContext, mFavoritesList.get(position).idGroup());
            holder.tvGroup.setText(group.nomGrupo());
            holder.tvFase.setText(group.nomFase());
            holder.tvCompetition.setText(group.nomCompeticion());
            String teamName = mFavoritesList.get(position).idTeam();
            if (teamName!=null) {
                holder.tvTeam.setText(teamName);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFavoritesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mFavoritesList.get(position).idTeam()==null) {
            return TYPE_GROUP;
        } else {
            return TYPE_TEAM;
        }
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cvFavorites.setOnClickListener(this);
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

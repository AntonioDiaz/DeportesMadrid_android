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
    private final ListItemClickListener mListItemClickListener;

    public TeamsAdapter(Context context, String idCompetition, ArrayList<String> teamsList, ListItemClickListener listItemClickListener) {
        this.mContext = context;
        this.idCompetition = idCompetition;
        this.teamsList = teamsList;
        this.mListItemClickListener = listItemClickListener;
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
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_team_name)
        TextView tvTeamName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListItemClickListener.onListItemClick(getAdapterPosition() - 1);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}

package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.utils.UtilsPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    Context mContext;
    List<Competition> mCompetitionList;
    private final GroupsAdapter.ListItemClickListener mOnClickListener;


    public GroupsAdapter(Context mContext, List<Competition> competitionList, ListItemClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mCompetitionList = competitionList;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_group, parent, false);
        return new GroupsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvGroupName.setText(mCompetitionList.get(position).nomGrupo());
        holder.tvCompetition.setText(mCompetitionList.get(position).nomCompeticion());
        holder.tvFase.setText(mCompetitionList.get(position).nomFase());
        if (UtilsPreferences.isShowCompetitionsNumber(mContext)) {
            holder.tvGroupName.append(" (" + mCompetitionList.get(position).id() + ")");
        }
    }

    @Override
    public int getItemCount() {
        return mCompetitionList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_listitem_title)
        TextView tvGroupName;

        @BindView(R.id.cv_listitem)
        CardView cvListItem;

        @BindView(R.id.tv_competition)
        TextView tvCompetition;

        @BindView(R.id.tv_fase)
        TextView tvFase;

        /*        @BindView(R.id.tv_sport_count)
        TextView tvCompetitionCount;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cvListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

}

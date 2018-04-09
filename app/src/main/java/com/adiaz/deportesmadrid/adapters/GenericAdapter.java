package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.utils.ListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

    Context mContext;
    List<ListItem> sportsList;
    private final ListItemClickListener mOnClickListener;

    public GenericAdapter(Context mContext, ListItemClickListener listItemClickListener, List<ListItem> sportsList) {
        this.mContext = mContext;
        this.mOnClickListener = listItemClickListener;
        this.sportsList = sportsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_competition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String str = sportsList.get(position).getName() + " - " + sportsList.get(position).getCount();
        holder.tvCompetitionTitle.setText(str);
        //holder.tvCompetitionCount.setText(sportCount);
    }

    @Override
    public int getItemCount() {
        return sportsList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_sport_title)
        TextView tvCompetitionTitle;

        /*        @BindView(R.id.tv_sport_count)
        TextView tvCompetitionCount;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }
}

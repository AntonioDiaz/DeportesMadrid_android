package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.utils.ListItem;
import com.adiaz.deportesmadrid.utils.Utils;
import com.adiaz.deportesmadrid.utils.UtilsPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adiaz on 10/1/18.
 */

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {

    Context mContext;
    ListItemClickListener mListItemClickListener;
    List<ListItem> mListItems;

    public SportsAdapter(Context mContext, ListItemClickListener mListItemClickListener, List<ListItem> mListItems) {
        this.mContext = mContext;
        this.mListItemClickListener = mListItemClickListener;
        this.mListItems = mListItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.listitem_sports;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new SportsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int colorPrimaryDark = ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimaryDark, null);
        int colorAccent = ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null);
        if (position==0) {
            holder.mSportName.setText(mContext.getString(R.string.favorites));
            holder.mSportName.setBackgroundColor(colorAccent);
            holder.mSportName.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.rounded_corner_accent));
            holder.mIvSport.setImageResource(R.drawable.favorite);
            holder.mIvSport.setColorFilter(colorAccent, PorterDuff.Mode.SRC_IN);
        } else {
            String sportName = mListItems.get(position-1).getName();
            String sportTag = Utils.normalizaSportName(sportName);
            String sportImage = Utils.getStringResourceByName(mContext, sportTag + "_IMG");
            String sportLocated = Utils.getStringResourceByName(mContext, sportTag);
            if (UtilsPreferences.isShowCompetitionsNumber(mContext)) {
                sportLocated += " (" +  mListItems.get(position-1).getCount() + ")";
            }
            holder.mSportName.setText(sportLocated);
            holder.mIvSport.setColorFilter(colorPrimaryDark, PorterDuff.Mode.SRC_IN);
            try {
                int identifier = mContext.getResources().getIdentifier(sportImage, "drawable", mContext.getPackageName());
                holder.mIvSport.setImageResource(identifier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListItems.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_sport)
        TextView mSportName;

        @BindView(R.id.iv_sport)
        ImageView mIvSport;

        public ViewHolder (View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
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
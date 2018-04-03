package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.ViewHolder>{


    Context mContext;
    List<String> mSportsList;

    public CompetitionAdapter(Context mContext, List<String> sportsList) {
        this.mContext = mContext;
        this.mSportsList = sportsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.listitem_competition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCompetitionTitle.setText(mSportsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSportsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_competition_title)
        TextView tvCompetitionTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

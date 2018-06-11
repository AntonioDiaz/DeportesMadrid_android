package com.adiaz.deportesmadrid.adapters.expandable;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderMatch extends ChildViewHolder {

    @Nullable
    @BindView(R.id.tv_calendar_team_local)
    TextView tvTeamLocal;

    @Nullable
    @BindView(R.id.tv_calendar_team_visitor)
    TextView tvTeamVisitor;

    @Nullable
    @BindView(R.id.tv_calendar_date)
    TextView tvDate;

    @Nullable
    @BindView(R.id.tv_calendar_place)
    TextView tvPlace;

    @Nullable
    @BindView(R.id.tv_score_local)
    TextView tvScoreLocal;

    @Nullable
    @BindView(R.id.tv_score_visitor)
    TextView tvScoreVisitor;

    @Nullable
    @BindView(R.id.tv_state)
    TextView tvState;

    Context mContext;

    View mView;

    public ViewHolderMatch(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = context;
        mView = itemView;
    }

    public void updateMatchFields(MatchChild matchChild) {
        tvTeamLocal.setText(matchChild.teamLocal());
        tvTeamVisitor.setText(matchChild.teamVisitor());
        tvScoreLocal.setText(matchChild.scoreLocal());
        tvScoreVisitor.setText(matchChild.scoreVisitor());
        tvDate.setText(matchChild.dateStr());
        tvPlace.setText(matchChild.placeName());
        tvState.setText(matchChild.state());
        mView.setTag(matchChild);
    }
}

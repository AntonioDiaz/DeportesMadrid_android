package com.adiaz.deportesmadrid.adapters.expandable;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ViewHolderWeek extends GroupViewHolder {

    private TextView tvWeekName;
    private ImageView arrow;


    public ViewHolderWeek(View itemView) {
        super(itemView);
        tvWeekName = itemView.findViewById(R.id.tv_match);
        arrow = itemView.findViewById(R.id.list_item_genre_arrow);
    }

    public void setTitle(ExpandableGroup week) {
        if (week instanceof WeekGroup) {
            tvWeekName.setText(week.getTitle());
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}

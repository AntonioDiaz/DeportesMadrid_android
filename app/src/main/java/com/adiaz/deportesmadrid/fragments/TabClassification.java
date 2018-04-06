package com.adiaz.deportesmadrid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabClassification extends Fragment {

    @BindView(R.id.tv_classification)
    TextView tvClassification;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (CompetitionDetailsActivity.classificationList!=null) {
            for (ClassificationRetrofitEntity classificationRetrofitEntity : CompetitionDetailsActivity.classificationList) {
                tvClassification.append(classificationRetrofitEntity.getPosition().toString());
                tvClassification.append("- " + classificationRetrofitEntity.getTeam().getName());
                tvClassification.append("\n");
            }
        }
    }
}

package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.callbacks.ClassificationCallback;
import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabCompetitionClassification extends Fragment {

    @BindView(R.id.tv_classification)
    TextView tvClassification;

    ClassificationCallback mClassificationCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClassificationCallback = (ClassificationCallback)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ClassificationCallback");
        }
    }

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
        if (mClassificationCallback.queryClassificationList()!=null) {
            for (ClassificationRetrofitEntity classificationRetrofitEntity : mClassificationCallback.queryClassificationList()) {
                String teamName =  classificationRetrofitEntity.getTeam()==null?" - ":classificationRetrofitEntity.getTeam().getName();
                tvClassification.append(classificationRetrofitEntity.getPosition().toString());
                tvClassification.append(" - " + teamName);
                tvClassification.append("\n");
            }
        }
    }
}

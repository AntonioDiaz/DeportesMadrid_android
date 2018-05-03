package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.GroupDetailsActivity;
import com.adiaz.deportesmadrid.adapters.GroupsAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabTeamGroups extends Fragment implements GroupsAdapter.ListItemClickListener {

    @BindView(R.id.rv_groups)
    RecyclerView mRvGroups;
    CompetitionCallback mCompetitionCallback;
    List<Group> mGroupsList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCompetitionCallback = (CompetitionCallback)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CompetitionCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_groups, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGroupsList = new ArrayList<>();
        if (mCompetitionCallback.queryTeam()!=null && mCompetitionCallback.queryTeam().getGroups()!=null) {

            for (String idGroup : mCompetitionCallback.queryTeam().getGroups()) {
                Group group = GroupsDAO.queryCompetitionsById(this.getContext(), idGroup);
                mGroupsList.add(group);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            GroupsAdapter genericAdapter = new GroupsAdapter(this.getContext(), mGroupsList, this);
            mRvGroups.setHasFixedSize(true);
            mRvGroups.setLayoutManager(layoutManager);
            mRvGroups.setAdapter(genericAdapter);
            genericAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this.getContext(), GroupDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, mGroupsList.get(clickedItemIndex).id());
        intent.putExtra(Constants.NAME_COMPETITION, mGroupsList.get(clickedItemIndex).nomGrupo());
        startActivity(intent);
    }
}

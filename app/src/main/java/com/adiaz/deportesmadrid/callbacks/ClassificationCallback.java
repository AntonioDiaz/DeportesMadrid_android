package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.retrofit.competitiondetails.ClassificationRetrofit;

import java.util.List;

public interface ClassificationCallback {
    List<ClassificationRetrofit> queryClassificationList();
    String underlineTeam();

}

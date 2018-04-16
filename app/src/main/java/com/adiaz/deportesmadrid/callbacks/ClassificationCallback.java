package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import java.util.List;

public interface ClassificationCallback {
    List<ClassificationRetrofitEntity> queryClassificationList();
    String underlineTeam();

}

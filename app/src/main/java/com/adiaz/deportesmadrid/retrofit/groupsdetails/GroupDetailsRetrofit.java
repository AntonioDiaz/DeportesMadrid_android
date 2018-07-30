
package com.adiaz.deportesmadrid.retrofit.groupsdetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDetailsRetrofit {

    @SerializedName("group")
    @Expose
    private GroupRetrofit groupRetrofit;
    @SerializedName("matches")
    @Expose
    private List<MatchRetrofit> matchRetrofits = null;
    @SerializedName("classification")
    @Expose
    private List<ClassificationRetrofit> classificationRetrofit = null;

    public GroupRetrofit getGroupRetrofit() {
        return groupRetrofit;
    }

    public void setGroupRetrofit(GroupRetrofit groupRetrofit) {
        this.groupRetrofit = groupRetrofit;
    }

    public List<MatchRetrofit> getMatchRetrofits() {
        return matchRetrofits;
    }

    public void setMatchRetrofits(List<MatchRetrofit> matchRetrofits) {
        this.matchRetrofits = matchRetrofits;
    }

    public List<ClassificationRetrofit> getClassificationRetrofit() {
        return classificationRetrofit;
    }

    public void setClassificationRetrofit(List<ClassificationRetrofit> classificationRetrofit) {
        this.classificationRetrofit = classificationRetrofit;
    }

}

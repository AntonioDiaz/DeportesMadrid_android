
package com.adiaz.deportesmadrid.retrofit.competitiondetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionDetailsRetrofit {

    @SerializedName("competition")
    @Expose
    private CompetitionRetrofit competitionRetrofit;
    @SerializedName("matches")
    @Expose
    private List<MatchRetrofit> matchRetrofits = null;
    @SerializedName("classification")
    @Expose
    private List<ClassificationRetrofit> classificationRetrofit = null;

    public CompetitionRetrofit getCompetitionRetrofit() {
        return competitionRetrofit;
    }

    public void setCompetitionRetrofit(CompetitionRetrofit competitionRetrofit) {
        this.competitionRetrofit = competitionRetrofit;
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

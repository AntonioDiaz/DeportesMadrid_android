package com.adiaz.deportesmadrid.db.entities;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Favorite implements Parcelable {

    @Nullable
    public abstract Long id();

    @Nullable
    public abstract String idCompetition();

    @Nullable
    public abstract String idTeam();

    public static Favorite.Builder builder() {
        return new AutoValue_Favorite.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);
        public abstract Builder idCompetition(String idCompetition);
        public abstract Builder idTeam(String idTeam);
        public abstract Favorite build();
    }
}

package com.adiaz.deportesmadrid.db.entities;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Favorite implements Parcelable {

    @Nullable
    public abstract Long id();

    @Nullable
    public abstract String idGroup();

    @Nullable
    public abstract Long idTeam();

    @Nullable
    public abstract String nameTeam();

    public static Favorite.Builder builder() {
        return new AutoValue_Favorite.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);
        public abstract Builder idGroup(String idGroup);
        public abstract Builder idTeam(Long idTeam);
        public abstract Builder nameTeam(String nameTeam);
        public abstract Favorite build();
    }
}

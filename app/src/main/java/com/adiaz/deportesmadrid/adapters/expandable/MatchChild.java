package com.adiaz.deportesmadrid.adapters.expandable;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MatchChild implements Parcelable {

    public abstract String teamLocal();
    public abstract String teamVisitor();
    public abstract String scoreLocal();
    public abstract String scoreVisitor();
    public abstract String state();
    public abstract String placeName();
    public abstract String dateStr();

    public static Builder builder() {
        return new AutoValue_MatchChild.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder teamLocal(String teamLocal);
        public abstract Builder teamVisitor(String teamVisitor);
        public abstract Builder scoreLocal(String scoreLocal);
        public abstract Builder scoreVisitor(String scoreVisitor);
        public abstract Builder state(String state);
        public abstract Builder placeName(String placeName);
        public abstract Builder dateStr(String dateStr);
        public abstract MatchChild build();
    }



}

package com.adiaz.deportesmadrid.db.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Match {

    public abstract String teamLocal();
    public abstract String teamVisitor();

    public static Match.Builder builder() {
        return new AutoValue_Match.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder teamLocal(String teamLocal);
        public abstract Builder teamVisitor(String teamVisitor);
        public abstract Match build();
    }
}

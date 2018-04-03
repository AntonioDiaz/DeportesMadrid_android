package com.adiaz.deportesmadrid.db.entities;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by adiaz on 22/3/18.
 */
@AutoValue
public abstract class Competition implements Parcelable {

    public abstract Integer id();
    public abstract Integer codTemporada();
    public abstract Integer codCompeticion();
    public abstract Integer codFase();
    public abstract Integer codGrupo();
    public abstract String nomTemporada();
    public abstract String nomCompeticion();
    public abstract String nomFase();
    public abstract String nomGrupo();
    public abstract String deporte();
    public abstract String distrito();

    public static Builder builder() {
        return new AutoValue_Competition.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);
        public abstract Builder codTemporada(Integer codTemporada);
        public abstract Builder codCompeticion(Integer codCompeticion);
        public abstract Builder codFase(Integer codFase);
        public abstract Builder codGrupo(Integer codGrupo);
        public abstract Builder nomTemporada(String nomTemporada);
        public abstract Builder nomCompeticion(String nomCompeticion);
        public abstract Builder nomFase(String nomFase);
        public abstract Builder nomGrupo(String nomGrupo);
        public abstract Builder deporte(String deporte);
        public abstract Builder distrito(String distrito);
        public abstract Competition build();
    }
}
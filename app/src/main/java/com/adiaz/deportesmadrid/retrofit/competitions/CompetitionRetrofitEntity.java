
package com.adiaz.deportesmadrid.retrofit.competitions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CompetitionRetrofitEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("codTemporada")
    @Expose
    private Integer codTemporada;
    @SerializedName("codCompeticion")
    @Expose
    private String codCompeticion;
    @SerializedName("codFase")
    @Expose
    private Integer codFase;
    @SerializedName("codGrupo")
    @Expose
    private Integer codGrupo;
    @SerializedName("nombreTemporada")
    @Expose
    private String nombreTemporada;
    @SerializedName("nombreCompeticion")
    @Expose
    private String nombreCompeticion;
    @SerializedName("nombreFase")
    @Expose
    private String nombreFase;
    @SerializedName("nombreGrupo")
    @Expose
    private String nombreGrupo;
    @SerializedName("deporte")
    @Expose
    private String deporte;
    @SerializedName("distrito")
    @Expose
    private String distrito;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCodTemporada() {
        return codTemporada;
    }

    public void setCodTemporada(Integer codTemporada) {
        this.codTemporada = codTemporada;
    }

    public String getCodCompeticion() {
        return codCompeticion;
    }

    public void setCodCompeticion(String codCompeticion) {
        this.codCompeticion = codCompeticion;
    }

    public Integer getCodFase() {
        return codFase;
    }

    public void setCodFase(Integer codFase) {
        this.codFase = codFase;
    }

    public Integer getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(Integer codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getNombreTemporada() {
        return nombreTemporada;
    }

    public void setNombreTemporada(String nombreTemporada) {
        this.nombreTemporada = nombreTemporada;
    }

    public String getNombreCompeticion() {
        return nombreCompeticion;
    }

    public void setNombreCompeticion(String nombreCompeticion) {
        this.nombreCompeticion = nombreCompeticion;
    }

    public String getNombreFase() {
        return nombreFase;
    }

    public void setNombreFase(String nombreFase) {
        this.nombreFase = nombreFase;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("codTemporada", codTemporada).append("codCompeticion", codCompeticion).append("codFase", codFase).append("codGrupo", codGrupo).append("nombreTemporada", nombreTemporada).append("nombreCompeticion", nombreCompeticion).append("nombreFase", nombreFase).append("nombreGrupo", nombreGrupo).append("deporte", deporte).append("distrito", distrito).toString();
    }

}

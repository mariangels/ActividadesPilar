package com.example.mariangeles.actividades.clases;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Actividad implements Parcelable{

    private int id;
    private int idProfesor;
    private String tipo;
    private String fechai;
    private String fechaf;
    private String lugari;
    private String lugarf;
    private String descripcion;
    private String alumno;

    public Actividad() {
    }

    public Actividad(int id, int idProfesor, String tipo, String fechai, String fechaf, String lugari, String lugarf, String descripcion, String alumno) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.tipo = tipo;
        this.fechai = fechai;
        this.fechaf = fechaf;
        this.lugari = lugari;
        this.lugarf = lugarf;
        this.descripcion = descripcion;
        this.alumno = alumno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechai() {
        return fechai;
    }

    public void setFechai(String fechai) {
        this.fechai = fechai;
    }

    public String getFechaf() {
        return fechaf;
    }

    public void setFechaf(String fechaf) {
        this.fechaf = fechaf;
    }

    public String getLugari() {
        return lugari;
    }

    public void setLugari(String lugari) {
        this.lugari = lugari;
    }

    public String getLugarf() {
        return lugarf;
    }

    public void setLugarf(String lugari) {
        this.lugarf = lugarf;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public JSONObject getJson(){
        //{"idprofesor":"2","tipo":"extraescolar","fechai":"aaaa-mm-dd hh:mm","fechaf":"aaaa-mm-dd hh:mm","lugari":"Granada","lugarf":"Granada","descripcion":"Nos vamos de parranda","alumno":"mariam"}

        JSONObject objetoJSON = new JSONObject();
        try {
            objetoJSON.put("idprofesor", idProfesor);
            objetoJSON.put("tipo", tipo);
            objetoJSON.put("fechai", fechai);
            objetoJSON.put("fechaf", fechaf);
            objetoJSON.put("lugari", lugari);
            objetoJSON.put("lugarf", lugarf);
            objetoJSON.put("descripcion", descripcion);
            objetoJSON.put("alumno", alumno);
        } catch (JSONException e) {
        }
        return objetoJSON;
    }

    /********************  PARCEL  ************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.getId());
        parcel.writeInt(this.getIdProfesor());
        parcel.writeString(this.tipo);
        parcel.writeString(this.fechai);
        parcel.writeString(this.fechaf);
        parcel.writeString(this.lugari);
        parcel.writeString(this.lugarf);
        parcel.writeString(this.descripcion);
        parcel.writeString(this.alumno);
    }

    public static final Parcelable.Creator<Actividad> CREATOR=
            new Parcelable.Creator<Actividad>(){

        @Override
        public Actividad createFromParcel(Parcel p) {
            int id = p.readInt();
            int idProfesor = p.readInt();
            String tipo = p.readString();
            String fechai = p.readString();
            String fechaf = p.readString();
            String lugari = p.readString();
            String lugarf = p.readString();
            String descripcion = p.readString();
            String alumno = p.readString();

            return new Actividad(id, idProfesor, tipo, fechai, fechaf, lugari, lugarf, descripcion, alumno);
        }

        @Override
        public Actividad[] newArray(int i) {
            return new Actividad[i];
        }
    };
}

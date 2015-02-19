package com.example.mariangeles.actividades.clases;

/**
 * Created by Mariangeles on 14/02/2015.
 */

public class Grupo {

    private int id;
    private String grupo;

    public Grupo() {
    }

    public Grupo(int id, String grupo) {
        this.id = id;
        this.grupo = grupo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
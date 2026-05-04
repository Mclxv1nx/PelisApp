package com.example.pelisapp;

import java.util.List;

public class Pelicula {
    public Integer id; // Cambiado a Integer (con mayúscula) para que pueda ser null
    public String titulo;
    public String director;
    public int anio;
    public String genero;
    public int duracion_minutos;
    public Double calificacion; // Cambiado a Double para que coincida con NUMERIC(3,1) de tu BD
    public String creada_en;
    public String actualizada_en;
    public List<Actor> actores;
}
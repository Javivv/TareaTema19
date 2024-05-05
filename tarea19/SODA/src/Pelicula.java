/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author javivv
 */
public class Pelicula {
    
    private String titulo, sinopsis;
    private int puntuacion, anyo;
    private FechaNacimiento fecha;

    
    public Pelicula (String titulo, int anyo, int puntuacion, String sinopsis, FechaNacimiento fecha){
        this.titulo = titulo;
        this.anyo = anyo;
        this.puntuacion = puntuacion;
        this.sinopsis = sinopsis;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return "Pelicula [titulo = " + titulo + ", sinopsis = " + sinopsis + ", año = " + anyo + ", puntuacion = " + puntuacion
                + "]";
    }
    
    
    
    
}

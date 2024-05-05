package com.mycompany.repaso;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MEDAC
 */
public class TableModelPeliculas extends AbstractTableModel {

    private static final String[] columnNames = {"Nombre", "Apellidos", "Edad", "Mail"};
    private HashMap<String, String> credenciales = new HashMap<>();
    private LinkedList<Usuario> datosUsuarios;


    public TableModelPeliculas(HashMap<String, String> credenciales, LinkedList<Usuario> datosUsuarios) {
        this.credenciales = credenciales;
        this.datosUsuarios = datosUsuarios;
        // Notifica a la vista que el contenido ha cambiado para que se refresque.
        fireTableDataChanged();
    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return datosUsuarios.size();
    }
    
    /** En este método agregamos la película nueva a la tabla y la actualizamos.
     * @param pelicula que queremos añadir a la tabla.
     */
    public void anyadirUsuario(String nombre, String apellidos, String edad, String mail, String contrasena){
        this.credenciales.put(mail, contrasena);
        Usuario nuevoUser = new Usuario(nombre, apellidos, edad, mail, contrasena);
        this.datosUsuarios.add(nuevoUser);
        System.out.println("Usuario Añadido");
        fireTableDataChanged(); // Refresca la tabla
    }
    
    /** En este método borramos todas las peliculas de la lista y las volvemos a añadir desde la base de datos para actualizar los datos.
     * @param conexion a la base de datos de donde obtendremos las peliculas.
     */
    public void borrarUsuario(SQLConnection conexion){
        
        for (int i = this.datosUsuarios.size() - 1; i >= 0; i--) { // Borro la tabla al reves para no tener problema con los indices al actualizarse.
            this.datosUsuarios.remove(i);
        }
        
        ResultSet rset = conexion.ejecutarSelect("SELECT `Nombre`, `Apellidos`, `Edad`, `Mail`, `contrasena` FROM `registrousuarios`;"); // Obtenemos todas las peliculas de la base de datos.

        try {
            while (rset.next()) { // El bucle se repite hasta que no haya más películas.

                String nombre = rset.getString(1);
                String apellidos = rset.getString(2);
                String edad = rset.getString(3);
                String mail = rset.getString(4);
                String contrasena = rset.getString(5);
                
                anyadirUsuario(nombre, apellidos, edad, mail, contrasena);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.getLocalizedMessage();
        }
        
        fireTableDataChanged(); // Actualizamos la tabla
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return datosUsuarios.get(rowIndex).getNombre();
            case 1:
                return datosUsuarios.get(rowIndex).getApellidos();
            case 2:
                return datosUsuarios.get(rowIndex).getEdad();
            case 3:
                return datosUsuarios.get(rowIndex).getMail();
            case 4:
                return datosUsuarios.get(rowIndex).getContrasena();
        }
        return null;
    }


}

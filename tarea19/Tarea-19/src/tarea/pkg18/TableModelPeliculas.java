/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea.pkg18;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

/**
 *
 * @author MEDAC
 */
public class TableModelPeliculas extends AbstractTableModel {

    private static final String[] columnNames = {"Titulo", "Año", "Puntuación", "Sinopsis"};
    private LinkedList<Pelicula> lista;

    public TableModelPeliculas(LinkedList<Pelicula> lista) {
        this.lista=lista;
        // Notifica a la vista que el contenido ha cambiado para que se refresque.
        fireTableDataChanged();
    }

    public Pelicula getValueAt(int rowIndex) {
        return lista.get(rowIndex);
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
        return lista.size();
    }
    
    /** En este método agregamos la película nueva a la tabla y la actualizamos.
     * @param pelicula que queremos añadir a la tabla.
     */
    public void anyadirPelicula(Pelicula pelicula){
        this.lista.add(pelicula);
        fireTableDataChanged(); // Refresca la tabla
    }
    
    /** En este método borramos todas las peliculas de la lista y las volvemos a añadir desde la base de datos para actualizar los datos.
     * @param conexion a la base de datos de donde obtendremos las peliculas.
     */
    public void borrarLista( ObjectContainer bd){
        
        for (int i = this.lista.size() - 1; i >= 0; i--) { // Borro la tabla al reves para no tener problema con los indices al actualizarse.
            this.lista.remove(i);
        }
        
        try {
            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class); // Con esto obtenemos todas las peliculas
            
            // ? Ejecutar y mostrar el resultado
            ObjectSet<Pelicula> res = consultaPelicula.execute();
            System.out.println("pasa execute");

            while (res.hasNext()){

                anyadirPelicula(res.next()); // Añadimos la pelicula a la lista.
            }
            
        } catch (Exception e) {
            System.out.println("Error en el método borrar lista: " + e.toString());
        }
        
        fireTableDataChanged(); // Actualizamos la tabla
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return lista.get(rowIndex).getTitulo();
            case 1:
                return lista.get(rowIndex).getAnyo();
            case 2:
                return lista.get(rowIndex).getPuntuacion();
            case 3:
                return lista.get(rowIndex).getSinopsis();
        }
        return null;
    }

}

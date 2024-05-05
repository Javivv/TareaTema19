package tarea.pkg18;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oException;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 *
 * @author javivv
 */
public class ConexionSoda {
    private ObjectContainer bd;
    private String rutabd;
    private static ConexionSoda instance; // Solo habra una conexion en toda la aplicación.
    
    private ConexionSoda(String rutabd) { // Al ser privado no podemos hacer un New desde fuera de esta clase.
        bd = null;
        this.rutabd = rutabd;
    }
    
    
    /** En este método crearemos la conexión con la base de datos.
     * @param ruta con el nombre de la base de datos que usaremos.
     */
    public static void crearConexion(String ruta){
        if (instance == null) {
            instance = new ConexionSoda(ruta);
            System.out.println("Conexión creada");
        }

    }
    
    public static ConexionSoda getInstance(){ // Cuando usamos un metodo estatico, podemos usarlo desde otra clase sin instanciarlo.
        return instance;
    }
    
    /** En este métdo nos desconectaremos de la base de datos, en caso de estar conectados enviaremos una excepción.
     * 
     */
    public void desconectar() throws Db4oException {
        try {
            
            if (bd != null) {
                bd.close(); // Cerramos la base de datos.
                System.out.println("Desconectado");
            }

        } catch (Exception e) {
            System.out.println("Error en el método desconectar: " + e.toString());
        }
    }
    
    /** En este método abriremos la conexión con nuestra base de datos.
     * 
     */
    public void conectar() throws Db4oException {
        try {
                if (bd == null) {
                    bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "peliculas.db4o"); // Instanciamos la base de datos
                    System.out.println("Conectado");
                }

        } catch (Exception e) {
            System.out.println("Error en el método conectar: " + e.toString());
        }
    }
    
    /** En este método ejecutaremos un select con los datos indicados por parámetros, devolviendo las similitudes encontradas
     * @param titulo con el título de la película
     * @param anyo con el año de la película
     * @param puntuacion con la puntuación de la película
     * @param sinopsis con la sinopsis de la película
     */
    public void ejecutarSelect() throws Db4oException {

        try {
            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class); // Con esto obtenemos todas las peliculas

            // ? Ejecutar y mostrar el resultado
            ObjectSet<Object> res = consultaPelicula.execute();

            while (res.hasNext()) {
                System.out.println(res.next());
            }

        } catch (Exception e) {
            System.out.println("Error en el método ejecutar select: " + e.toString());
        }
    }
    
    /** En este método ejecutaremos modificaciones sobre la película seleccionada.
     * @param peliculaAntigua con la película que queremos modificar
     * @param peliculaNueva con los parámetros actualizados de la película
     */
    public void ejecutarModificacion(Pelicula peliculaAntigua, Pelicula peliculaNueva){
        try {

            
            Query consultaPelicula = bd.query(); // Abrimos la consulta
            consultaPelicula.constrain(Pelicula.class); // Seleccionamos la clase película
            Constraint restriccionNombre = consultaPelicula.descend("titulo").constrain(peliculaAntigua.getTitulo()); // Aplicamos las restricciones del título y obtenemos el título de la pelicula a modificar
            consultaPelicula.descend("sinopsis").constrain(peliculaAntigua.getSinopsis()).and(restriccionNombre);  // Añadimos la restricción de la sinopsis junto con la creada anteriormente.
            
            ObjectSet<Pelicula> res1 = consultaPelicula.execute(); // Obtenemos el conjunto de resultados con las restricciones indicadas

            while(res1.hasNext()){ // Mientras encuentre resultados se seguirá ejecutando.
                Pelicula nuevaPelicula = res1.next();
                // Comprobamos que los valores no esten vacios para solo modificar los deseados
                if (nuevaPelicula.getAnyo() != 0) {
                    nuevaPelicula.setAnyo(peliculaNueva.getAnyo());
                }

                if (nuevaPelicula.getPuntuacion() != 0) {
                    nuevaPelicula.setPuntuacion(peliculaNueva.getPuntuacion());
                }

                if (!nuevaPelicula.getSinopsis().equals("")) {
                    nuevaPelicula.setSinopsis(peliculaNueva.getSinopsis());
                }

                if (!nuevaPelicula.getTitulo().equals("")) {
                    nuevaPelicula.setTitulo(peliculaNueva.getTitulo());
                }
                
                // Volvemos a guardar la pelicula en la BBDD para que se actualice
                bd.store(nuevaPelicula);
                }
            
            
        } catch (Exception e) {
            System.out.println("Error en el método ejecutar modificacion: " + e.toString());
            
        }
    }
    
    /** En este método buscaremos las coincidencias de la película que queremos eliminar y la borraremos del catálogo.
     * @param peliculaAEliminar con los datos de la película que queremos eliminar de la base de datos.
     */
    public void ejecutarDelete(Pelicula peliculaAEliminar) throws Db4oException{
        
        try {

            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class);
            Constraint restriccionNombre = consultaPelicula.descend("titulo").constrain(peliculaAEliminar.getTitulo());
            consultaPelicula.descend("sinopsis").constrain(peliculaAEliminar.getSinopsis()).and(restriccionNombre); 
            
            ObjectSet<Pelicula> resDelete = consultaPelicula.execute();

            bd.delete(resDelete.next()); // Eliminamos la película de la base de datos.

        } catch (Exception e) {
            System.out.println("Error al ejecutar Delete: " + e.toString());
        }
        
    }

    /** En este método ejecutaremos el borrado de las películas menores al año indicado con una puntuación específica
     * @param anyo con el año de referencia que tomaremos
     * @param puntuacion con el dato de puntuación exacto que queremos eliminar
     * @throws Db4oException
     */
    public void ejecutarDeleteMenor(int anyo, int puntuacion) throws Db4oException{
        
        try {

            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class);
            Constraint restriccion = consultaPelicula.descend("puntuacion").constrain(puntuacion).equal();
            consultaPelicula.descend("anyo").constrain(anyo).smaller().and(restriccion); // Con .smaller nos referimos a los años menores al indicado.
            
            ObjectSet<Pelicula> resDelete = consultaPelicula.execute();

            while (resDelete.hasNext()) {
                bd.delete(resDelete.next());
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar DeleteMenor: " + e.toString());
        }
        
    }
        /** En este método ejecutaremos el borrado de las películas iguales al año indicado con una puntuación específica
     * @param anyo con el año de referencia que tomaremos
     * @param puntuacion con el dato de puntuación exacto que queremos eliminar
     * @throws Db4oException
     */
    public void ejecutarDeleteIgual(int anyo, int puntuacion) throws Db4oException{
        
        try {

            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class);
            Constraint restriccionAnyo = consultaPelicula.descend("anyo").constrain(anyo).equal(); // con .equal nos referimos al año exacto indicado
            consultaPelicula.descend("puntuacion").constrain(puntuacion).and(restriccionAnyo); 
            
            ObjectSet<Pelicula> resDelete = consultaPelicula.execute();

            while (resDelete.hasNext()) {
                bd.delete(resDelete.next());
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar DeleteIgual: " + e.toString());
        }
        
    }

        /** En este método ejecutaremos el borrado de las películas mayores al año indicado con una puntuación específica
     * @param anyo con el año de referencia que tomaremos
     * @param puntuacion con el dato de puntuación exacto que queremos eliminar
     * @throws Db4oException
     */
    public void ejecutarDeleteMayor(int anyo, int puntuacion) throws Db4oException{
        
        try {

            Query consultaPelicula = bd.query();
            consultaPelicula.constrain(Pelicula.class);
            Constraint restriccionAnyo = consultaPelicula.descend("anyo").constrain(anyo).greater(); // Con .greater nos referimos a los años mayores al indicado.
            consultaPelicula.descend("puntuacion").constrain(puntuacion).and(restriccionAnyo); 
            
            ObjectSet<Pelicula> resDelete = consultaPelicula.execute();

            while (resDelete.hasNext()) {
                bd.delete(resDelete.next());
            }

        } catch (Exception e) {
            System.out.println("Error al ejecutar DeleteMayor: " + e.toString());
        }
        
    }


    public ObjectContainer getBd() {
        return bd;
    }


    public void setBd(ObjectContainer bd) {
        this.bd = bd;
    }


    public String getRutabd() {
        return rutabd;
    }


    public void setRutabd(String rutabd) {
        this.rutabd = rutabd;
    }


    public static void setInstance(ConexionSoda instance) {
        ConexionSoda.instance = instance;
    }
    
}

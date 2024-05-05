import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

public class App {
    public static void main(String[] args) throws Exception {

        FechaNacimiento fecha = new FechaNacimiento(23, 10, 2010);
        Pelicula peli = new Pelicula("Carlos", 2000, 6, "Carlos a secas", fecha);
        
        ObjectContainer bd = null;
        
        try {
            // ? Abrir el archivo de la BD
            bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "peliculas.db4o");
            // bd.store(peli);

        // ? Crear la consulta sobre la clase Pelicula
        Query consulta = bd.query();
        consulta.constrain(Pelicula.class);

        // * Crear la consulta sobre la clase Pelicula

        Constraint restriccionEdad = consultaPelicula.descend("puntuacion").constrain(9).greater();
        // * descend: selecciona el campo que queremos restringir
        // * constrain: asigna el valor del campo.
        // * greater / smaller / equal / not: asigna el comparador

        // ? Añadimos un OR a la consulta.
        consultaPelicula.descend("anyo").constrain(2023).greater().or(restriccionEdad);

        // ? Concatenación de consultas
        Query consultaPelicula = bd.query();
        consultaPelicula.constrain(Pelicula.class);
        Constraint restriccionNombre = consultaPelicula.descend("titulo").constrain("Carlos");
        consultaPelicula.descend("fecha").descend("mes").constrain(10).and(restriccionNombre); // Estamos diciendo que filtre por el parametro fecha, por el atributo mes (10) y use la restriccion de nombre
        consultaPelicula.descend("fecha").descend("dia").orderDescending(); // En este caso ordena por el atributo dia en orden descendente


        // ? ELIMINACION Y ACTUALIZACION EN TIPOS DE DATOS ESTRUCTURADOS

        // * Construir la consulta, ejecutar select
        // * Aplicar a los objetos del resultado store o delete segun el caso.

        ObjectSet result = consultaPelicula.execute();

        // * Ejemplo para eliminar las peliculas
        while (result.hasNext()) {
            bd.delete(result.next());
        }

        // * Ejemplo para guardar las peliculas
        while (result.hasNext()) {
            Pelicula p = (Pelicula)result.next();
            p.setPuntuacion(8);
            bd.store(p);
        }


        // ? Ejecutar y mostrar el resultado
        ObjectSet<Object> res = consultaPelicula.execute();
        while (res.hasNext()) {
            System.out.println(res.next());
        }

    } catch (Exception ex) {
        ex.printStackTrace();

    } finally {
        // Cerrar la base de datos
        bd.close();
    }


    }
}

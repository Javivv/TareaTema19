package com.mycompany.repaso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.TimeZone;


public class SQLConnection {

    private String host, port, user, pass, bd;
    private Connection conexion;

    public SQLConnection(String host, String port, String user, String pass, String bd) { // * Constructor por parámetros.
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.bd = bd;
        this.conexion = null;
    }

    public SQLConnection(String bd) { // * Creamos un constructor por "defecto" ya que normalmente solo deberemos cambiar de base de datos.
        host = "localhost";
        port = "3306";
        user = "root";
        pass = "";
        this.bd = bd;
        this.conectar();
    }

    
    /** Con este método nos conectamos a la base de datos deseada.
     * @param obtenemos los datos directamente del objeto SQLConnection 
     * @return void
     */
    public void conectar(){
        Calendar now = Calendar.getInstance(); // * Obtenemos el dia.
        TimeZone zonahoraria = now.getTimeZone(); // * Obtenemoos la zona horaria.

        try { // * Conectamos con nuestra base de datos.
            this.conexion = (Connection) DriverManager.getConnection(
            "jdbc:mysql://" + this.host + "/" + this.bd + "?user="
            + this.user + "&password" + this.pass
            + "&useLegacyDatetimeCode=flase&serverTimezone="
            + zonahoraria.getID());

            System.out.println("Conectado"); // * Imprimimos el resultado si no se produce ningun error.
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error al conectar!!");
        }
    }


    /** Con este método cerramos la conexión con la base de datos
     * @param obtenemos los datos directamente del objeto SQLConnection.
     * @return void
     */
    public void desconectar(){

        try {
            if (conexion != null && conexion.isClosed()) { // * Comprobamos que la conexión este iniciada, y en ese caso la cerramos.
                conexion.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    /** En este método ejecutamos consultas de tipo Select e imprimos el resultado.
     * @param String con la consulta deseada.
     * @return Resultset con la respuesta obtenida.
     */
    public ResultSet ejecutarSelect(String consulta){
        
        Statement stmt;
        ResultSet rset = null;

        try {
            stmt = conexion.createStatement(); // * Preparamos el objeto para ejecutar consultas.
            rset = stmt.executeQuery(consulta); // * Ejecutamos la consulta deseada y guardamos el resultado (array al tratarse de un select)
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return rset;
    }


    /** En este método ejecutamos consultas de tipo INSERT, UPDATE Y/O DELETE.
     * @param String con la consulta de modificación.
     * @return int con la líneas afectadas de la base de datos.
     */
    public int ejecutarModificacion(String modificacion){

        int respuesta = 0;

        try {
            Statement stmtInsert = conexion.createStatement();
            respuesta = stmtInsert.executeUpdate(modificacion); // * Respuesta devolverá el número de filas afectadas.
            if (respuesta > 0) { // * Si modifica alguna fila mostraremos que la modificación se ha realizado.
                System.out.println("Modificacion realizada");

            } else {
                System.out.println("Modificación fallida");
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return respuesta;
    }

}

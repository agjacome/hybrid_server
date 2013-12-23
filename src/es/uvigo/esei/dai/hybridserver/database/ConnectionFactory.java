package es.uvigo.esei.dai.hybridserver.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.Configuration;

/**
 * Clase estatica que sirve como factoria de Conexiones a la base de
 * datos configurada en el sistema.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class ConnectionFactory
{

    /**
     * Crea y devuelve una conexion a la base de datos segun los
     * parametros configurados en el sistema (via fichero de
     * configuracion).
     * 
     * @return {@link Connection} representando una nueva conexion a
     *         la base de datos.
     * 
     * @throws SQLException
     *         Si se produce algun error de acceso o conexion a la
     *         base de datos.
     */
    public static final Connection getConnection( ) throws SQLException
    {
        final String dbUrl  = Configuration.getInstance().getDatabaseURL();
        final String dbUser = Configuration.getInstance().getDatabaseUser();
        final String dbPass = Configuration.getInstance().getDatabasePassword();

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

}

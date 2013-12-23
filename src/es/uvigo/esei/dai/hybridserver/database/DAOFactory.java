package es.uvigo.esei.dai.hybridserver.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.dao.HTMLDocumentSQLDAO;
import es.uvigo.esei.dai.hybridserver.database.dao.XMLDocumentSQLDAO;
import es.uvigo.esei.dai.hybridserver.database.dao.XSDDocumentSQLDAO;
import es.uvigo.esei.dai.hybridserver.database.dao.XSLTDocumentSQLDAO;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Clase estatica que sirve como factoria de DAO segun la entidad
 * proporcionada.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class DAOFactory
{

    // map desde el que se obtendran las clases de los dao concretos
    // segun la entidad recibida
    private static final Map<String, Class<? extends DocumentDAO<?>>> daos;

    // inicializa el map y lo hace read-only
    static {
        final Map<String, Class<? extends DocumentDAO<?>>> dMap = new HashMap<>(4);
        dMap.put("html" , HTMLDocumentSQLDAO.class);
        dMap.put("xml"  , XMLDocumentSQLDAO.class);
        dMap.put("xsd"  , XSDDocumentSQLDAO.class);
        dMap.put("xslt" , XSLTDocumentSQLDAO.class);

        daos = Collections.unmodifiableMap(dMap);
    }

    /**
     * Devuelve una instancia de {@link DocumentDAO} asociada al tipo
     * de documento concreto segun la entidad recibida.
     * 
     * @param entity
     *        String representando la entidad de la que se desea
     *        obtener un DAO (eg: "HTML", "XML"...).
     * 
     * @return Instancia de DocumentDAO con un DAO capaz de trabajar
     *         con el tipo de entidad solicitada.
     * 
     * @throws ServerErrorException
     *         Si no se encuentra ningun tipo de DAO para la entidad
     *         solicitada.
     */
    public static DocumentDAO<?> getDAO(final String entity)
        throws ServerErrorException
    {
        // TODO: no es "limpio" lanzar una ServerError desde aqui, la
        // capa de acceso a datos no deberia saber que esta
        // ejecutandose bajo un servidor
        if (!daos.containsKey(entity))
            throw new ServerErrorException(entity + " DAO not found");

        try {

            return daos.get(entity).newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            System.err.print(entity + " DAO cannot be instantiated");
            System.err.println("Please, check your code.");
            throw new RuntimeException(e);
        }
    }

}

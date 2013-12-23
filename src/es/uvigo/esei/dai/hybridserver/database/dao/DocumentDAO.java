package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.database.entity.AbstractDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;

/**
 * Interfaz que todo DAO para los documentos del sistema debe cumplir.
 * 
 * @param <D>
 *        Documento al que estara asociado el DAO concreto. Debe,
 *        obligatoriamente, heredar de {@link AbstractDocument}.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public interface DocumentDAO<D extends AbstractDocument>
{

    // TODO: quizas seria mas adecuado no lanzar SQLExceptions, porque
    // asi estamos asociando los DAO con un tipo de BD concreto, si
    // mañana cambiamos por una No-SQL que? seria mejor crearse una
    // nueva excepcion tipo DataAccessException y asi se evita que la
    // capa superior (los controladores) sepan que debajo hay SQL
    // * usar ServerErrorException?
    // ** ServerError implica logica de servidor, la capa de datos no
    // debe saber que encima hay un servidor

    /**
     * Comprueba que un documento, dado su identificador UUID, exista
     * en el sistema.
     * 
     * @param uuid
     *        String representando el identificador UUID que se desea
     *        comprobar si existe o no en el sistema.
     * 
     * @return True si el identificador proporcionado existe en el
     *         sistema, False en caso contrario.
     * 
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public boolean exists(final String uuid) throws SQLException;

    /**
     * Devuelve un listado con todos los documentos del tipo del DAO
     * concreto existentes en el sistema.
     * 
     * @return List de documentos del tipo del DAO concreto.
     * 
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public List<D> list( ) throws SQLException;

    /**
     * Recupera de datos un documento del tipo del DAO concreto, dado
     * su identificador UUID.
     * 
     * @param uuid
     *        Identificador UUID del documento que se desea recuperar
     *        de los datos.
     * 
     * @return Documetno del tipo del DAO concreto con todos los datos
     *         recuperados.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento con el
     *         identificador proporcionado.
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public D get(final String uuid) throws DocumentNotFoundException, SQLException;

    /**
     * Inserta un nuevo documento del tipo del DAO concreto en los
     * datos del sistema.
     * 
     * @param document
     *        Documento a almacenar.
     * 
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public void create(final D document) throws SQLException;

    /**
     * Modifica el contenido de un documento del tipo del DAO concreto
     * en los datos.
     * 
     * @param document
     *        Documento a modificar, se utilizara el identificador
     *        UUID del mismo para identificarlo (debe existir
     *        previamente) y el resto de campos seran modificados en
     *        los datos segun el documento recibido.
     * 
     * @throws DocumentNotFoundException
     *         Si el identificador UUID del documento recibido no se
     *         corresponde con nignun documento existente. Es decir,
     *         el documento proporcionado no ha sido creado
     *         previamente a la solicitud de modificacion.
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public void update(final D document) throws DocumentNotFoundException, SQLException;

    /**
     * Elimina un documento, identificado por su UUID, de los datos
     * del sistema.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        a eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si el identificador UUID recibido no se corresponde con
     *         ningun documento existente.
     * @throws SQLException
     *         Si se produce algun error durante el acceso a datos.
     */
    public void delete(final String uuid) throws DocumentNotFoundException, SQLException;

}

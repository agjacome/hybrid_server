package es.uvigo.esei.dai.hybridserver.controller;

import java.util.List;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.exception.BadRequestException;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Interfaz a implementar por todos los controladores locales
 * existentes en el sistema.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public interface DocumentController
{

    /**
     * Devuelve el tipo MIME asociado a los documentos con los que
     * trabaja este controlador.
     * 
     * @return String representando el tipo MIME correcto.
     */
    public String getMIMEType( );

    /**
     * Devuelve un Map con un listado de todos los identificadores
     * UUID de los documentos existentes tanto localmente como en
     * servidores remotos. El servidor local estara en la clave
     * "Local Server", los servidores remotos estaran asociados cada
     * uno con el nombre con que hayan sido configurados en el
     * sistema.
     * 
     * @return Map con todos los listados de identificadores UUID
     *         existentes (locales y remotos) para el tipo de
     *         documento con el que el controlador trabaja.
     * 
     * @throws ServerErrorException
     *         Si se produce algun tipo de error en la recuperacion
     *         del listado (local o remoto).
     */
    public Map<String, List<String>> list( )
        throws ServerErrorException;

    /**
     * Obtiene el contenido de un documento concreto, identificado a
     * traves de un identificador UUID recibido como parametro. Es
     * posible realizar mas operaciones con el documento antes de
     * obtener su contenido, por ello se proporcionan las varargs
     * haciendo que cada controlador concreto trabaje con los
     * parametros que necesite sin afectar a todos los demas.
     * 
     * @param uuid
     *        Identificador UUID del documento del que se desea
     *        obtener el contenido.
     * @param extra
     *        Parametros a ser utilizados por los controladores
     *        concretos si asi lo desean o necesitan.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra el documento solicitado.
     * @throws BadRequestException
     *         Si la peticion es incorrecta (puede que se requieran
     *         mas parametros de los recibidos).
     * @throws ServerErrorException
     *         Si se produce algun tipo de error por parte del
     *         servidor (eg: acceso a datos) mientras se intenta
     *         recuperar el contenido del documento.
     */
    public String get(final String uuid, final String ... extra)
        throws DocumentNotFoundException, BadRequestException, ServerErrorException;

    /**
     * Crea un nuevo documento dado su contenido. Dependiendo del
     * controlador concreto, es posible que ademas del contenido
     * necesite mas parametros, por ello se proporcionan las varargs.
     * 
     * @param content
     *        String con el contenido del documento a crear.
     * @param extra
     *        Parametros extra a ser utilizados por los controladores
     *        concretos si asi lo desean o necesitan.
     * 
     * @return String con el identificador UUID del documento recien
     *         creado.
     * 
     * @throws DocumentNotFoundException
     *         Si la creacion del documento requiere de la existencia
     *         de otro previo (posiblemente recibido como parametro
     *         extra) y éste no existe.
     * @throws ServerErrorException
     *         Si se produce algun error por parte del servidor (eg:
     *         acceso a datos) durante la creacion del documento.
     */
    public String create(final String content, final String ... extra)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina un documento concreto identificado a traves del UUID
     * recibido como parametro.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        a eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si el documento que se desea eliminar no existe.
     * @throws ServerErrorException
     *         Si se produce algun tipo de error por parte del
     *         servidor (eg: acceso a datos) durante la eliminacion
     *         del documento.
     */
    public void delete(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

}

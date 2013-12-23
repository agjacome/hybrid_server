package es.uvigo.esei.dai.hybridserver.exception;

/**
 * Excepcion para el marcado de solicitudes de documentos no
 * existentes.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class DocumentNotFoundException extends Exception
{

    private static final long serialVersionUID = 1L;
    private final String      uuid;

    /**
     * Construye una DocumentNotFoundException con el identificador de
     * documento solicitado (y no encontrado).
     * 
     * @param uuid
     *        Identificador de documento solicitado.
     */
    public DocumentNotFoundException(final String uuid)
    {
        super();
        this.uuid = uuid;
    }

    /**
     * Construye una DocumentNotFoundException con un mensaje y el
     * identificador de documento solicitado (y no encontrado).
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param uuid
     *        Identificador del documento solicitado.
     */
    public DocumentNotFoundException(final String message, final String uuid)
    {
        super(message);
        this.uuid = uuid;
    }

    /**
     * Construye una DocumentNotFoundException con una causa y el
     * identificador de documento solicitado (y no encontrado).
     * 
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     * @param uuid
     *        Identificador del documento solicaitado.
     */
    public DocumentNotFoundException(final Throwable cause, final String uuid)
    {
        super(cause);
        this.uuid = uuid;
    }

    /**
     * Construye una DocumentNotFoundException con un mensaje, una
     * causa y el identificador de documento solicitado (y no
     * encontrado).
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     * @param uuid
     *        Identificador del documento solicaitado.
     */
    public DocumentNotFoundException(final String message, final Throwable cause, final String uuid)
    {
        super(message, cause);
        this.uuid = uuid;
    }

    /**
     * Devuelve el identificador de documento solicitado (y no
     * encontrado) asociado a esta instancia de
     * DocumentNotFoundException.
     * 
     * @return {@link String} con el identificador de documento
     *         proporcionado en el constructor.
     */
    public String getUUID( )
    {
        return uuid;
    }

}

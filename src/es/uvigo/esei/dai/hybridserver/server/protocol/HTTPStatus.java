package es.uvigo.esei.dai.hybridserver.server.protocol;

/**
 * Enumeracion representando todos los estados HTTP necesarios para la
 * implementacion del servidor, junto a sus mensajes asociados.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public enum HTTPStatus
{

    // satisfactorios
    OK(200, "OK"), CREATED(201, "Created"),

    // errores de cliente
    BAD_REQ(400, "Bad Request"), NOT_FOUND(404, "Not Found"),
    NOT_ALLOWED(405, "Method Not Allowed"),

    // errores de servidor
    INTERNAL_ERR(500, "Internal Server Error"),
    NOT_IMPL(501, "Not Implemented");

    private final int    code;
    private final String message;

    /**
     * Crea el estado de respuesta HTTP con un codigo y un mensaje
     * asociados.
     * 
     * @param code
     *        El codigo del estado.
     * @param message
     *        El mensaje del estado.
     */
    private HTTPStatus(final int code, final String message)
    {
        this.code    = code;
        this.message = message;
    }

    /**
     * Devuelve el codigo del estado de HTTP.
     * 
     * @return Un int representando el codigo HTTP correspondiente.
     */
    public int getCode( )
    {
        return code;
    }

    /**
     * Devuelve la cadena completa Codigo + Mensaje del estado HTTP.
     * 
     * @return {@link String} con el Codigo y Mensaje del estado HTTP.
     */
    @Override
    public String toString( )
    {
        return code + " " + message;
    }

}

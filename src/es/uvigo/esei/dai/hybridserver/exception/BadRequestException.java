package es.uvigo.esei.dai.hybridserver.exception;

/**
 * Excepcion para el marcado de peticiones incorrectas al servidor.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class BadRequestException extends Exception
{

    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva instancia de BadRequestException.
     */
    public BadRequestException( )
    {
        super();
    }

    /**
     * Crea una nueva instancia de BadRequestException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     */
    public BadRequestException(final String message)
    {
        super(message);
    }

    /**
     * Crea una nueva instancia de BadRequestException.
     * 
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     */
    public BadRequestException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Crea una nueva instancia de BadRequestException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     */
    public BadRequestException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}

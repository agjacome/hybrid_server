package es.uvigo.esei.dai.hybridserver.exception;

/**
 * Excepcion para el marcado de errores del servidor.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class ServerErrorException extends Exception
{

    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva instancia de ServerErrorException.
     */
    public ServerErrorException( )
    {
        super();
    }

    /**
     * Crea una nueva instancia de ServerErrorException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     */
    public ServerErrorException(final String message)
    {
        super(message);
    }

    /**
     * Crea una nueva instancia de ServerErrorException.
     * 
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     */
    public ServerErrorException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Crea una nueva instancia de ServerErrorException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     */
    public ServerErrorException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}

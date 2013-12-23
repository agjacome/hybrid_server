package es.uvigo.esei.dai.hybridserver.exception;

/**
 * Excepcion para el marcado de solicitudes de un controlador no
 * existente (rutas invalidas).
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class ControllerNotFoundException extends Exception
{

    private static final long serialVersionUID = 1L;

    // nombre del controlador solicitado y no encontrado
    private final String controller;

    /**
     * Crea una nueva instancia de ControllerNotFoundException.
     * 
     * @param controller
     *        Nombre del controlador (ruta) no encontrado.
     */
    public ControllerNotFoundException(final String controller)
    {
        super();
        this.controller = controller;
    }

    /**
     * Crea una nueva instancia de ControllerNotFoundException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param controller
     *        Nombre del controlador (ruta) no encontrado.
     */
    public ControllerNotFoundException(final String message, final String controller)
    {
        super(message);
        this.controller = controller;
    }

    /**
     * Crea una nueva instancia de ControllerNotFoundException.
     * 
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     * @param controller
     *        Nombre del controlador (ruta) no encontrado.
     */
    public ControllerNotFoundException(final Throwable cause, final String controller)
    {
        super(cause);
        this.controller = controller;
    }

    /**
     * Crea una nueva instancia de ControllerNotFoundException.
     * 
     * @param message
     *        Mensaje para la excepcion.
     * @param cause
     *        Objeto Throwable que encapsule la causa de la excepcion.
     * @param controller
     *        Nombre del controlador (ruta) no encontrado.
     */
    public ControllerNotFoundException(final String message, final Throwable cause, final String controller)
    {
        super(message, cause);
        this.controller = controller;
    }

    /**
     * Devuelve el nombre del controlador no encontrado asociado a
     * esta instancia de ControllerNotFoundException.
     * 
     * @return String con el nombre del controlador no encontrado.
     */
    public String getController( )
    {
        return controller;
    }

}

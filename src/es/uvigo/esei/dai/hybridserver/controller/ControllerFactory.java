package es.uvigo.esei.dai.hybridserver.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.exception.ControllerNotFoundException;

/**
 * Clase estatica que sirve como factoria (o enrutador) de
 * controladores concretos.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class ControllerFactory
{

    // map desde el que se obtendran las clases de los controladores
    // concretos segun la ruta recibida
    private static final Map<String, Class<? extends DocumentController>> routes;

    // inicializa el map y lo hace read-only
    static {
        final Map<String, Class<? extends DocumentController>> cMap = new HashMap<>(4);

        cMap.put("html" , HTMLDocumentController.class);
        cMap.put("xml"  , XMLDocumentController.class);
        cMap.put("xsd"  , XSDDocumentController.class);
        cMap.put("xslt" , XSLTDocumentController.class);

        routes = Collections.unmodifiableMap(cMap);
    }

    /**
     * Devuelve una instancia de {@link DocumentController} asociada
     * al tipo de documento que se solicite como parametro (puede ser
     * visto como un "enrutador" que devuelva el controlador correcto
     * para cada ruta).
     * 
     * @param route
     *        El tipo de controlador (o ruta) a obtener (eg: "HTML",
     *        "XML"...).
     * 
     * @return Instancia de DocumentController con un controlador
     *         capaz de trabajar con el tipo de documento solicitado.
     * 
     * @throws ControllerNotFoundException
     *         Si no se encuentra ningun tipo de controlador asociado
     *         al tipo de documento (ruta) solicitado.
     */
    public static DocumentController getController(final String route)
        throws ControllerNotFoundException
    {
        final String controller = route.toLowerCase();

        if (!routes.containsKey(controller))
            throw new ControllerNotFoundException(route);

        try {

            return routes.get(route.toLowerCase()).newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            System.err.print(route + " controller cannot be instantiated.");
            System.err.println(" Please, check your code.");
            throw new RuntimeException(e);
        }
    }

}

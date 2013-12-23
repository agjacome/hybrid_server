package es.uvigo.esei.dai.hybridserver.server;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.controller.service.DocumentServiceImpl;

/**
 * Clase estatica cuya unica responsabilidad es la incializacion del
 * WebService asociado a este servidor.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class WebServicesServer
{

    /**
     * Publica un nuevo Web Service {@link Endpoint} segun la
     * configuracion del sistema, utilizando un
     * {@link DocumentServiceImpl} como objeto implementador.
     */
    public static void init( )
    {
        Endpoint.publish(
            Configuration.getInstance().getWebServiceURL(),
            new DocumentServiceImpl()
        );
    }

}

package es.uvigo.esei.dai.hybridserver.controller.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.controller.service.DocumentService;

/**
 * Clase de utilidades para el trabajo con servicios web.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class WSUtils
{

    /**
     * Devuelve un Map con todos los {@link DocumentService} remotos
     * configurados en el sistema, siendo las claves cada uno de los
     * nombres presentes en el fichero de configuracion.
     * 
     * @return Map con todos los DocumentService remotos configurados.
     */
    public static Map<String, DocumentService> getDocumentServices( )
    {
        final Map<String, Service> services = getRemoteServices();

        final Map<String, DocumentService> documentServices = new HashMap<>();
        for (Entry<String, Service> service : services.entrySet()) {
            documentServices.put(
                service.getKey(),
                getDocumentService(service.getValue())
                );
        }

        return documentServices;
    }

    /**
     * Devuelve un Map con todos los {@link Service} remotos
     * configurados en el sistema. siendo las claves cada uno de los
     * nombres presentes en el fichero de configuracion.
     * 
     * @return Map con todos los servicios remotos configurados.
     */
    private static Map<String, Service> getRemoteServices( )
    {
        final Configuration config = Configuration.getInstance();

        final Map<String, Service> services = new HashMap<>();
        for (final String server : config.getRemoteServerNames()) {
            try {

                final URL wsdl = new URL(config.getRemoteWSDL(server));

                final QName sname = new QName(
                    config.getRemoteNamespace(server),
                    config.getRemoteService(server)
                    );

                final Service service = Service.create(wsdl, sname);

                services.put(server, service);

            } catch (final MalformedURLException mue) {
                System.err.println("Malformed Remote URL: " + mue.getMessage());
            } catch (final WebServiceException wse) {
                System.err.println("WebService error: " + wse.getMessage());
            }
        }

        return Collections.unmodifiableMap(services);
    }

    /**
     * Obtiene el {@link DocumentService} asociado a un
     * {@link Service} proporcionado.
     * 
     * @param service
     *        El servicio desde el que obtener el proxy a
     *        DocumentService.
     * 
     * @return DocumentService asociado al servicio recibido como
     *         parametro.
     */
    private static DocumentService getDocumentService(final Service service)
    {
        return service.getPort(DocumentService.class);
    }

}

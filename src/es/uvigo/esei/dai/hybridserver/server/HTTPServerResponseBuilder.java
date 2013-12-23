package es.uvigo.esei.dai.hybridserver.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.DocumentController;
import es.uvigo.esei.dai.hybridserver.exception.BadRequestException;
import es.uvigo.esei.dai.hybridserver.exception.ControllerNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;
import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPStatus;

/**
 * Clase para la construccion de respuestas HTTP en base a una
 * peticion recibida. Se encarga de realizar todo el procesamiento
 * asociado a la creacion de una respuesta.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
class HTTPServerResponseBuilder
{

    // la peticion con la que trabajara esta instancia para crear una
    // respuesta adecuada
    private final HTTPRequest request;

    /**
     * Construye una nueva instancia de HTTPServerResponseBuilder dada
     * una peticion {@link HTTPRequest} para procesar.
     * 
     * @param request
     *        Objeto HTTPRequest que encapsula una peticion HTTP desde
     *        la que generar una respuesta HTTP {@link HTTPResponse}
     *        adecuada.
     */
    public HTTPServerResponseBuilder(final HTTPRequest request)
    {
        this.request = request;
    }

    /**
     * Genera una respuesta HTTP, representada por un objeto
     * {@link HTTPResponse}, adecuada para la peticion recibida.
     * 
     * @return Objeto HTTPResponse que encapsula la respuesta adecuada
     *         a la peticion recibida en el constructor de esta
     *         instancia.
     */
    public HTTPResponse generateResponse( )
    {
        try {

            final DocumentController controller =
                ControllerFactory.getController(
                    request.getResource().substring(1)
                );

            switch (request.getMethod()) {
                case GET:
                    return handleGetRequest(controller);
                case POST:
                    return handlePostRequest(controller);
                case DELETE:
                    return handleDeleteRequest(controller);
                default:
                    return new HTTPResponse(
                        HTTPStatus.NOT_ALLOWED,
                        "Method not allowed: " + request.getMethod()
                    );
            }

        } catch (final BadRequestException bre) {
            return new HTTPResponse(
                HTTPStatus.BAD_REQ,
                "Invalid request: " + bre.getMessage()
            );
        } catch (final ControllerNotFoundException cnfe) {
            return new HTTPResponse(
                HTTPStatus.NOT_FOUND,
                "Path not found: /" + cnfe.getController()
            );
        } catch (final DocumentNotFoundException dnfe) {
            return new HTTPResponse(
                HTTPStatus.NOT_FOUND,
                "Document not found: " + dnfe.getUUID()
            );
        } catch (final ServerErrorException see) {
            return new HTTPResponse(
                HTTPStatus.INTERNAL_ERR,
                "Server error: " + see.getMessage()
            );
        }
    }

    /**
     * Metodo privado para el tratamiento de peticiones GET al
     * servidor.
     * 
     * @param controller
     *        Controlador asociado a la peticion segun la ruta que se
     *        haya solicitado.
     * 
     * @return Objeto HTTPResponse encapsulando la respuesta correcta
     *         a la peticion GET.
     * 
     * @throws DocumentNotFoundException
     *         Si se ha solicitado un documento que no existe.
     * @throws BadRequestException
     *         Si la peticion recibida no es correcta (eg: requiere
     *         mas parametros de los recibidos).
     * @throws ServerErrorException
     *         Si se produce un error durante el procesamiento de la
     *         peticion por parte del servidor (eg: error de la BD).
     */
    private HTTPResponse handleGetRequest(final DocumentController controller)
        throws DocumentNotFoundException, BadRequestException, ServerErrorException
    {
        HTTPResponse response;

        if (request.hasParam("uuid")) {

            final String content = controller.get(
                request.getParamValue("uuid"),
                request.getParamValue("xslt")
            );

            response = new HTTPResponse(HTTPStatus.OK, content);
            response.addHeader("Content-Type", controller.getMIMEType());

        } else {

            final String list = createListing(controller.list());
            response = new HTTPResponse(HTTPStatus.OK, list.toString());
            response.addHeader("Content-Type", "text/html;charset=UTF-8");

        }

        return response;
    }

    /**
     * Metodo privado para el tratamiento de peticiones POST al
     * servidor.
     * 
     * @param controller
     *        Controlador asociado a la peticion segun la ruta que se
     *        haya solicitado.
     * 
     * @return Objeto HTTPResponse encapsulando la respuesta correcta
     *         a la peticion POST.
     * 
     * @throws DocumentNotFoundException
     *         Si la peticion POST necesita de algun documento previo
     *         en el servidor que no ha sido encontrado.
     * @throws ServerErrorException
     *         Si se produce un error durante el procesamiento de la
     *         peticion por parte del servidor (eg: error de la BD).
     */
    private HTTPResponse handlePostRequest(final DocumentController controller)
        throws DocumentNotFoundException, ServerErrorException
    {
        final String resource = request.getResource().substring(1);
        String content        = request.getParamValue(resource);

        try {

            if ("application/x-www-form-urlencoded".equals(request.getHeaderValue("Content-Type")))
                content = URLDecoder.decode(content, "UTF-8");

        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final String uuid = controller.create(
            content,
            request.getParamValue("xsd")
        );

        final String message = String.format(
                "Document created successfully: <a href='?uuid=%s'>%s</a>",
                uuid, uuid
        );

        final HTTPResponse response = new HTTPResponse(
            HTTPStatus.CREATED,
            message
        );
        response.addHeader("Content-Type", "text/html;charset=UTF-8");

        return response;
    }

    /**
     * Metodo privado para el tratamiento de peticiones DELETE al
     * servidor.
     * 
     * @param controller
     *        Controlador asociado a la peticion segun la ruta que se
     *        haya solicitado.
     * 
     * @return Objeto HTTPResponse encapsulando la respuesta correcta
     *         a la peticion POST.
     * 
     * @throws DocumentNotFoundException
     *         Si el documento solicitado en la peiticion DELETE no
     *         existe en el sistema.
     * @throws ServerErrorException
     *         Si se produce un error durante el procesamiento de la
     *         peticion por parte del servidor (eg: error de la BD).
     */
    private HTTPResponse handleDeleteRequest(final DocumentController controller)
        throws DocumentNotFoundException, ServerErrorException
    {
        final String uuid = request.getParamValue("uuid");
        controller.delete(uuid);

        final HTTPResponse response = new HTTPResponse(
            HTTPStatus.OK,
            "Document deleted successfully\n"
        );
        response.addHeader("Content-Type", "text/plain;charset=UTF-8");

        return response;
    }

    /**
     * Metodo privado para la creacion de un listado en HTML con todos
     * los UUID recibidos como parametro, a traves de un Map. Genera
     * un listado "ul" por cada uno de los List<String> de UUID que
     * contiene el Map, poniendo a cada uno un titulo "h2" con la
     * clave asociada del Map.
     * 
     * @param uuids
     *        Map de identificadores UUID para la generacion del
     *        listado.
     * 
     * @return String con todo el contenido HTML de respuesta.
     */
    private String createListing(final Map<String, List<String>> uuids)
    {
        final StringBuilder sb = new StringBuilder();

        sb.append("<html><head><title>File Listing</title></head><body>");

        for (final Entry<String, List<String>> entry : uuids.entrySet()) {
            sb.append("<h2>").append(entry.getKey()).append("</h2>");
            sb.append("<ul>");
            for (final String uuid : entry.getValue()) {
                sb.append("<li><a href='?uuid=").append(uuid).append("'>");
                sb.append(uuid).append("</a></li>");
            }
            sb.append("</ul>");
        }

        sb.append("</body></html>");

        return sb.toString();
    }

}

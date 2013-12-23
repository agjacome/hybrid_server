package es.uvigo.esei.dai.hybridserver.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ProtocolException;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.server.protocol.HTTPStatus;

/**
 * Clase para el procesamiento de cada uno de los sockets cliente que
 * reciba {@link HTTPServer}. Implementa Runnable para permitir que el
 * procesamiento de cada Socket se realice en un hilo a parte.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
class HTTPServerService implements Runnable
{

    private HTTPRequest  request;      // peticion asociada al socket
    private final Socket clientSocket; // socket cliente

    /**
     * Crea un nuevo HTTPServerService con un socket cliente recibido
     * como parametro.
     * 
     * @param clientSocket
     *        Socket cliente asociado a este hilo de servicio.
     */
    public HTTPServerService(final Socket clientSocket)
    {
        request = null;
        this.clientSocket = clientSocket;
    }

    /**
     * Implementa el procesamiento de cada peticion recibida a traves
     * del socket. Se encarga de crear el objeto {@link HTTPRequest}
     * asociado a los datos recibidos, generar a traves de
     * {@link HTTPServerResponseBuilder} una respuesta
     * {@link HTTPResponse} adecuada para dicha peticion y enviar la
     * misma a traves del socket cliente desde el que se recibe.
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run( )
    {
        try (final Socket clientSocket = this.clientSocket) {

            final Reader reader = new InputStreamReader(
                clientSocket.getInputStream()
            );
            final Writer writer = new OutputStreamWriter(
                clientSocket.getOutputStream()
            );

            HTTPResponse response = null;

            try {

                request  = new HTTPRequest(reader);
                response = new HTTPServerResponseBuilder(
                    request
                ).generateResponse();

            } catch (final ProtocolException pe) {
                response = new HTTPResponse(
                    HTTPStatus.BAD_REQ,
                    "Malformed HTTP Request: " + pe.getMessage()
                );
            }

            response.print(writer);

        } catch (final IOException ioe) {
            System.err.println("Server Service error: " + ioe.getMessage());
        }
    }

}

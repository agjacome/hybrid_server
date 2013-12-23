package es.uvigo.esei.dai.hybridserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.uvigo.esei.dai.hybridserver.Configuration;

/**
 * Clase principal para el servidor HTTP. Proporciona un metodo
 * estatico para el inicio de la ejecucion del servidor.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class HTTPServer
{

    /**
     * Inicia la ejecucion del servidor HTTP de forma bloqueante (el
     * metodo no termina mientras el servidor no sea finalizado). Crea
     * un socket de servidor capaz de recibir peticiones y reenvia el
     * procesamiento de las mismas a sus propios hilos de
     * {@link HTTPServerService}.
     * 
     * @throws IOException
     *         Si se produce algun error de Entrada/Salida asociado al
     *         Socket del servidor.
     */
    public static void init( ) throws IOException
    {
        final int numClients = Configuration.getInstance().getNumClients();
        final int serverPort = Configuration.getInstance().getServerPort();

        final ExecutorService threadPool =
            Executors.newFixedThreadPool(numClients);

        try (final ServerSocket serverSocket = new ServerSocket(serverPort)) {

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                threadPool.execute(new HTTPServerService(clientSocket));
            }

        }
    }

}

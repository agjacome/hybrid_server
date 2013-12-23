package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.hybridserver.server.HTTPServer;
import es.uvigo.esei.dai.hybridserver.server.WebServicesServer;

/**
 * Clase principal. Proporciona el metodo main() desde donde se
 * cargara la configuracion e iniciaran los servidores.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class Launcher
{

    /**
     * Punto de entrada al programa.
     * 
     * @param args
     *        El programa debe recibir como argumento unico el fichero
     *        XML de configuracion del sistema.
     */
    public static void main(final String[ ] args)
    {
        // comprueba parametros
        if (args.length != 1) {
            System.err.println("USAGE: java es.uvigo.esei.dai.hybridserver.Launcher configuration.xml");
            System.exit(-1);
        }

        // parsea configuracion
        try {

            Configuration.getInstance().readFromFile(args[0]);

        } catch (final IOException ioe) {
            System.err.println("Supplied path is not a valid file.");
            System.err.println(ioe.getMessage());
            System.err.println("Please, provide an actual configuration file.");
            System.exit(-2);
        } catch (final ParserConfigurationException | SAXException e) {
            System.err.println("Invalid XML configuration file.");
            System.err.println(e.getMessage());
            System.err.println("Please, provide a valid configuration file.");
            System.exit(-3);
        }

        // inicia servidores
        try {

            WebServicesServer.init();
            HTTPServer.init();

        } catch (final IOException ioe) {
            System.err.println("Server error: " + ioe.getMessage());
            System.exit(-4);
        }
    }

}

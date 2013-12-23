package es.uvigo.esei.dai.hybridserver.server.protocol;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.server.HTTPServer;

/**
 * HTTPResponse implementa una clase que almacena una respuesta HTTP a
 * ser enviada por el servidor {@link HTTPServer}. Utiliza el
 * protocolo HTTP/1.1 y se encarga de formatear correctamente una
 * respuesta HTTP dados el codigo de estado {@link HTTPStatus} y un
 * contenido de respuesta. Permite la insercion de nuevas cabeceras
 * despues de haber sido creada la instancia y el envio de la misma a
 * traves de un {@link Writer}.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class HTTPResponse
{

    private static final String       VERSION = "HTTP/1.1";

    private final String              content;
    private final HTTPStatus          status;
    private final Map<String, String> headers = new HashMap<>();

    /**
     * Crea una instancia de HTTPResponse dados un estado HTTP para la
     * respuesta y el contenido para la misma. Incluye automaticamente
     * la cabecera "Content-Length", calculada a partir de la longitud
     * del String de cuerpo de la respuesta.
     * 
     * @param status
     *        El codigo de estado de la respuesta, como un
     *        {@link HTTPStatus}.
     * 
     * @param content
     *        El contenido del cuerpo de la respuesta, dado como un
     *        {@link String}.
     */
    public HTTPResponse(final HTTPStatus status, final String content)
    {
        this.status  = status;
        this.content = content;

        headers.put("Content-Length", Integer.toString(content.length()));
    }

    /**
     * Inserta una nueva cabecera HTTP en la respuesta, dadas el
     * nombre de la cabecera y su valor.
     * 
     * @param key
     *        El nombre de la cabecera HTTP a insertar, como
     *        {@link String}.
     * 
     * @param value
     *        El valor de la cabecera a insertar, asociada al nombre
     *        anterior,
     *        como {@link String}.
     */
    public void addHeader(final String key, final String value)
    {
        headers.put(key, value);
    }

    /**
     * Envia a traves de un {@link Writer} proporcionado todo el
     * contenido de la respuesta HTTP de la instancia que lo invoca,
     * formateada acordemente con el estandar.
     * 
     * @param writer
     *        El {@link Writer} por el que escribir la respuesta.
     * 
     * @throws IOException
     *         Si se produce algun error de entrada/salida durante el
     *         envio de
     *         la respuesta.
     */
    public void print(final Writer writer) throws IOException
    {
        final BufferedWriter output = new BufferedWriter(writer);

        output.write(VERSION + " " + status);
        output.newLine();

        for (final String header : headers.keySet()) {
            output.write(header + ": " + headers.get(header));
            output.newLine();
        }

        output.newLine();
        output.write(content);

        output.flush();
    }

}

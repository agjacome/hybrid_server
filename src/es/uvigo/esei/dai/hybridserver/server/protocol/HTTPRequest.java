package es.uvigo.esei.dai.hybridserver.server.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPRequest implementa una clase inmutable para almacenar todos los
 * datos relevantes de una peticion HTTP. Se encarga de parsear todos
 * los campos de la peticion que reciba a traves de un {@link Reader}
 * proporcionado en el constructor.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class HTTPRequest
{

    private final HTTPMethod          method;
    private final String              resource;
    private final int                 contentLen;
    private final Map<String, String> headers    = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();

    /**
     * Construye una instancia de HTTPRequest, parseando todos los
     * campos que lleguen a traves de un stream de caracteres recibido
     * como parametro.
     * 
     * @param reader
     *        Un stream de caracteres desde el cual se recibiran y
     *        parsearan todos los campos de la peticion HTTP.
     * 
     * @throws IOException
     *         Si se produce un error de entrada/salida asociado al
     *         {@link Reader}.
     * 
     * @throws ProtocolException
     *         Si alguno de los campos parseados no cumple los
     *         requisitos del estandar HTTP.
     */
    public HTTPRequest(final Reader reader)
        throws IOException, ProtocolException
    {
        final BufferedReader input = new BufferedReader(reader);

        // obtiene primera linea, y la separa en partes utilizando
        // como separador la repeticion de 1..* espacios
        final String firstLine = input.readLine();
        if (firstLine == null)
            throw new ProtocolException("Empty request.");

        final String[ ] firstLineParts = firstLine.split("\\s+");
        if (firstLineParts.length != 3)
            throw new ProtocolException("First line requires three fields.");

        // extrae el metodo de la primera parte de la primera linea
        try {
            method = HTTPMethod.valueOf(firstLineParts[0]);
        } catch (final IllegalArgumentException _) {
            throw new ProtocolException("Unrecognized method " + firstLineParts[0]);
        }

        // extrae recurso solicitado, separa la segunda parte de la
        // primera linea en partes, utlizando "?" como separador y
        // guarda la primera parte de esta nueva separacion
        final String[ ] resourceParts = firstLineParts[1].split("\\?");
        resource = resourceParts[0];

        // extrae parametros del recurso siempre que la peticion
        // contenga parametros
        if (resourceParts.length > 1)
            parseParams(resourceParts[1]);

        // extrae cabeceras de las siguientes lineas de la peticion,
        // hasta encontrar una linea en blanco, o hasta llegar a fin
        // del stream (en cuyo caso se lanzara una ProtocolException)
        String headerLine;
        while ((headerLine = input.readLine()) != null) {
            if (headerLine.isEmpty()) break;
            parseHeader(headerLine);
        }
        if (headerLine == null)
            throw new ProtocolException("Unexpected end of stream");

        // si la cabecera "Content-Length" existe, guarda su valor,
        // parseando el entero representado por la misma
        try {
            contentLen = headers.containsKey("Content-Length")
                       ? Integer.parseInt(headers.get("Content-Length"))
                       : 0;
        } catch (final NumberFormatException _) {
            throw new ProtocolException("Content-Length header does not hold a valid number");
        }

        // extrae parametros del cuerpo de la peticion si el metodo es
        // POST, lee como maximo la cantidad de caracteres
        // especificada por la cabecera Content-Length
        if (method == HTTPMethod.POST) {
            // no se puede utilizar readLine() porque el cuerpo de la
            // peticion no contiene un salto de linea al terminar, por
            // lo cual readLine() se quedaria bloqueado
            // indefinidamente
            final char[ ] charLineParams = new char[contentLen];
            final int readed = input.read(charLineParams);

            if (readed < contentLen)
                throw new ProtocolException("Incorrect value in Content-Length");

            parseParams(new String(charLineParams, 0, readed));
        }
    }

    /**
     * Devuelve el metodo de la peticion HTTP parseada en el
     * constructor.
     * 
     * @return {@link HTTPMethod} el metodo HTTP utilizado en esta
     *         peticion.
     */
    public HTTPMethod getMethod( )
    {
        return method;
    }

    /**
     * Devuelve el recurso solicitado por la peticion HTTP parseada en
     * el constructor.
     * 
     * @return {@link String} con el parametro solicitado.
     */
    public String getResource( )
    {
        return resource;
    }

    /**
     * Devuelve el valor de una cabecera HTTP.
     * 
     * @param key
     *        La cabecera de la que se desea obtener el valor.
     * 
     * @return {@link String} con el valor asociado a la clave
     *         proporcionada, <code>null</code> si la clave no existe.
     */
    public String getHeaderValue(final String key)
    {
        return headers.get(key);
    }

    /**
     * Comprueba si un parametro HTTP existe en la peticion HTTP
     * parseada.
     * 
     * @param key
     *        El parametro que se desea comprobar si existe.
     * 
     * @return True si el parametro existe en la peticion HTTP, False
     *         en caso contrario.
     */
    public boolean hasParam(final String key)
    {
        return parameters.containsKey(key);
    }

    /**
     * Devuelve el valor de un parametro HTTP.
     * 
     * @param key
     *        La clave del valor (el parametro) que se desea obtener.
     * 
     * @return {@link String} con el valor asociado a la clave
     *         proporcionada, <code>null</code> si la clave no existe.
     */
    public String getParamValue(final String key)
    {
        return parameters.get(key);
    }

    /**
     * Parsea una cabecera HTTP recibiendo para ello un {@link String}
     * que almacene la linea completa de la cabecera. Separa la misma
     * en clave y valor y los almacena dentro de la instancia de
     * HTTPRequest.
     * 
     * @param headerLine
     *        La linea que contiene una cabecera HTTP.
     * 
     * @throws ProtocolException
     *         Si la linea recibida no contiene una cabecera valida
     *         segun el estandar HTTP.
     */
    private void parseHeader(final String headerLine) throws ProtocolException
    {
        // separacion "Clave: Valor", con los espacios alrededor de
        // ":" repetidos 0..* veces.
        final String[ ] header = headerLine.split("\\s*:\\s*");

        if (header.length < 2)
            throw new ProtocolException("Malformed header: " + headerLine);

        // es posible que el valor de la cabecera tenga algun ":" en
        // su contenido, y split lo haya separado en multiples
        // valores, por tanto se recorren todos los valores separados
        // (salvo el primero, la clave) y se reconstruye el string con
        // todos
        final StringBuilder valueSB = new StringBuilder();
        for (int i = 1; i < header.length; ++i) {
            valueSB.append(header[i]);
            if (i < header.length - 1)
                valueSB.append(":");
        }

        headers.put(header[0], valueSB.toString());
    }

    /**
     * Parsea los parametros HTTP de una linea recibida como un
     * {@link String}. Se encarga de separar pares
     * "clave1=valor1&clave2=valor2..." y almacenar las claves y
     * valores correspondientes dentro del map de parametros.
     * 
     * @param paramsLine
     *        Linea conteniendo una serie de parametros.
     * 
     * @throws ProtocolException
     *         Si los parametros no cumplen el formato del estandar
     *         HTTP.
     */
    private void parseParams(final String paramsLine) throws ProtocolException
    {
        String[ ] param;
        for (final String keyValue : paramsLine.split("&")) {
            param = keyValue.split("=");

            if (param.length != 2)
                throw new ProtocolException("Malformed parameter: " + keyValue);

            parameters.put(param[0].trim(), param[1].trim());
        }
    }

}

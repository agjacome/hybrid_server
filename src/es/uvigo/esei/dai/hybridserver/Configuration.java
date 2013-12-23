package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Clase (Singleton) para la obtencion, almacenamiento y consulta de
 * todos los parametros de configuracion del sistema.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class Configuration
{

    // instacina del singleton
    private static final Configuration INSTANCE = new Configuration();

    // map que almacena los parametros de configuracion del servidor
    // local
    private final Map<String, String> localConfig;

    // map que almacena los parametros de configuracion de conexiona
    // todos los servidores remotos
    private final Map<String, Map<String, String>> remoteConfig;

    /**
     * Crea una instancia de Configuration, inicializando los dos Maps
     * de configuracion. Privado, para evitar que los clientes puedan
     * crear nuevas instancias, y obligarles a utilizar getInstance()
     * para obtener la unica instancia del singleton.
     */
    private Configuration( )
    {
        localConfig  = new HashMap<>();
        remoteConfig = new HashMap<>();
    }

    /**
     * Devuelve la unica instancia existente de Configuration.
     * 
     * @return Instancia de Configuration.
     */
    public static Configuration getInstance( )
    {
        return INSTANCE;
    }

    /**
     * Obtiene todos los parametros de configuracion del sistema desde
     * un fichero, dado el path hacia el mismo a traves de un String.
     * Asume que el fichero recibido es un fichero XML.
     * 
     * @param fileName
     *        Path hacia el fichero de configuracion a parsear.
     * 
     * @throws IOException
     *         Si durante la obtencion de los dato se produce algun
     *         error de entrada/salida.
     * @throws ParserConfigurationException
     *         Si se produce algun error durante el parseo del
     *         fichero XML.
     * @throws SAXException
     *         Si se produce algun error durante el parseo del
     *         fichero XML.
     */
    public void readFromFile(final String fileName)
        throws IOException, ParserConfigurationException, SAXException
    {
        final Document document = loadAndValidate(fileName);

        parseLocalConfig(document);
        parseRemoteConfig(document);
    }

    /**
     * Devuelve el puerto para el servidor HTTP.
     * 
     * @return Un int representando el puerto para la escucha de
     *         peticiones HTTP.
     */
    public int getServerPort( )
    {
        return Integer.parseInt(localConfig.get("http"));
    }

    /**
     * Devuelve el numero de clientes simultaneos que el servidor HTTP
     * debe ser capaz de soportar.
     * 
     * @return Un int con el numero de clientes concurrentes que el
     *         servidor HTTP es capaz de trabajar.
     */
    public int getNumClients( )
    {
        return Integer.parseInt(localConfig.get("numclients"));
    }

    /**
     * Devuelve una URL donde colocar el WebService de este servidor.
     * 
     * @return String representando la URL para e WebService.
     */
    public String getWebServiceURL( )
    {
        return localConfig.get("webservice");
    }

    /**
     * Devuelve una URL para la conexion a la base de datos a traves
     * de JDBC.
     * 
     * @return String con la URL de conexion a la BD.
     */
    public String getDatabaseURL( )
    {
        return localConfig.get("db_url");
    }

    /**
     * Devuelve el usuario a utilizar para conectarse a la base de
     * datos.
     * 
     * @return String con el nombre de usuario a utilizar para la
     *         conexion a la BD.
     */
    public String getDatabaseUser( )
    {
        return localConfig.get("db_user");
    }

    /**
     * Devuelve la contraseña del usuario para la conexion a la base
     * de datos.
     * 
     * @return String con la contraseña del usuario para la base de
     *         datos.
     */
    public String getDatabasePassword( )
    {
        return localConfig.get("db_pass");
    }

    /**
     * Devuelve un conjunto con todos los nombres de los servidores
     * remotos configurados.
     * 
     * @return Set de String representando todos los nombres de los
     *         servidores remotos configurados.
     */
    public Set<String> getRemoteServerNames( )
    {
        return remoteConfig.keySet();
    }

    /**
     * Devuelve la direccion HTTP de un servidor remoto dado el nombre
     * del mismo.
     * 
     * @param serverName
     *        String con el nombre del servidor del que se desea
     *        obtener la direccion HTTP.
     * 
     * @return String con la direccion HTTP del servidor remoto.
     */
    public String getRemoteAddress(final String serverName)
    {
        return remoteConfig.get(serverName).get("address");
    }

    /**
     * Devuelve el Namespace del WebService de un servidor remoto dado
     * el nombre del mismo.
     * 
     * @param serverName
     *        String con el nombre del servidor del que se desea
     *        obtener el Namespace.
     * 
     * @return String con el Namespace del servidor remoto.
     */
    public String getRemoteNamespace(final String serverName)
    {
        return remoteConfig.get(serverName).get("namespace");
    }

    /**
     * Devuelve el Service del WebService de un servidor remoto dado
     * el nombre del mismo.
     * 
     * @param serverName
     *        String con el nombre del servidor del que se desea
     *        obtener el Service.
     * 
     * @return String con el Service del servidor remoto.
     */
    public String getRemoteService(final String serverName)
    {
        return remoteConfig.get(serverName).get("service");
    }

    /**
     * Devuelve el WSDL de un servidor remoto dado el nombre del
     * mismo.
     * 
     * @param serverName
     *        String con el nombre del servidor del que se desea
     *        obtener el Service.
     * 
     * @return String con el WSDL del servidor remoto.
     */
    public String getRemoteWSDL(final String serverName)
    {
        return remoteConfig.get(serverName).get("wsdl");
    }

    /**
     * Carga y valida el documento XML de configuracion dado su path
     * como parametro.
     * 
     * @param documentPath
     *        String con el path hacia el fichero XML a cargar y
     *        validar.
     * 
     * @return {@link Document} representando el documento XML.
     * 
     * @throws ParserConfigurationException
     *         Si se produce un error durante la configuracion del
     *         parseador del documento XML.
     * @throws SAXException
     *         Si el documento XML no es valido o no ha podido
     *         parsearse correctamente.
     * @throws IOException
     *         Si se produce algun error de Entrada/Salida durante el
     *         proceso.
     */
    private Document loadAndValidate(final String documentPath)
        throws ParserConfigurationException, SAXException, IOException
    {
        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

        factory.setValidating(true);
        factory.setNamespaceAware(true);

        factory.setAttribute(
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
            XMLConstants.W3C_XML_SCHEMA_NS_URI
        );

        final DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new ErrorHandler()
        {
            @Override
            public void error(final SAXParseException exception)
                throws SAXException
            {
                throw exception;
            }

            @Override
            public void fatalError(final SAXParseException exception)
                throws SAXException
            {
                throw exception;
            }

            @Override
            public void warning(final SAXParseException exception)
                throws SAXException
            {
                throw exception;
            }
        });

        return builder.parse(new File(documentPath));
    }

    /**
     * Obtiene todos los parametros de configuracion relativos al
     * servidor local del documento XML previamente cargado.
     * 
     * @param document
     *        Documento XML desde el que obtener los parametros.
     */
    private void parseLocalConfig(final Document document)
    {
        final Element serverPort   = (Element) document.getElementsByTagName("http").item(0);
        final Element webService   = (Element) document.getElementsByTagName("webservice").item(0);
        final Element numClients   = (Element) document.getElementsByTagName("numClients").item(0);
        final Element databaseURL  = (Element) document.getElementsByTagName("url").item(0);
        final Element databaseUser = (Element) document.getElementsByTagName("user").item(0);
        final Element databasePass = (Element) document.getElementsByTagName("password").item(0);

        localConfig.put("http", serverPort.getTextContent().trim());
        localConfig.put("webservice", webService.getTextContent().trim());
        localConfig.put("numclients", numClients.getTextContent().trim());
        localConfig.put("db_url", databaseURL.getTextContent().trim());
        localConfig.put("db_user", databaseUser.getTextContent().trim());
        localConfig.put("db_pass", databasePass.getTextContent().trim());
    }

    /**
     * Obtiene todos los parametros de configuracion relativos a la
     * conexion a servidores remotos del documento XML previamente
     * cargado.
     * 
     * @param document
     *        Documento XML desde el que obtener los parametros.
     */
    private void parseRemoteConfig(final Document document)
    {
        final NodeList serverList = document.getElementsByTagName("server");

        for (int i = 0; i < serverList.getLength(); ++i) {
            final Element server = (Element) serverList.item(i);

            final Map<String, String> serverConfig = new HashMap<>();
            serverConfig.put("wsdl", server.getAttribute("wsdl"));
            serverConfig.put("namespace", server.getAttribute("namespace"));
            serverConfig.put("service", server.getAttribute("service"));
            serverConfig.put("address", server.getAttribute("httpAddress"));

            remoteConfig.put(server.getAttribute("name"), serverConfig);
        }
    }

}

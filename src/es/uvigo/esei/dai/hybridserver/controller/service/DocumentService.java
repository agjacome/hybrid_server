package es.uvigo.esei.dai.hybridserver.controller.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Interfaz para el servicio web. Proporciona métodos para listar
 * todos los identificadores de los documentos existentes en el
 * servidor asi como para recuperar su contenido y solicitar la
 * eliminacion de los mismos.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface DocumentService
{

    /**
     * Devuelve un listado con todos los identificadores de documentos
     * HTML disponibles en el servidor donde se invoca.
     * 
     * @return Array de String con todos los identificadores de
     *         documentos HTML contenidos en el servidor.
     * 
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         lsitado
     */
    @WebMethod
    public String[ ] getAllHTMLDocumentUUID( ) throws ServerErrorException;

    /**
     * Devuelve un listado con todos los identificadores de documentos
     * XML disponibles en el servidor donde se invoca.
     * 
     * @return Array de String con todos los identificadores de
     *         documentos XML contenidos en el servidor.
     * 
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         lsitado
     */
    @WebMethod
    public String[ ] getAllXMLDocumentUUID( ) throws ServerErrorException;

    /**
     * Devuelve un listado con todos los identificadores de documentos
     * XSD disponibles en el servidor donde se invoca.
     * 
     * @return Array de String con todos los identificadores de
     *         documentos XSD contenidos en el servidor.
     * 
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         lsitado
     */
    @WebMethod
    public String[ ] getAllXSDDocumentUUID( ) throws ServerErrorException;

    /**
     * Devuelve un listado con todos los identificadores de documentos
     * XSLT disponibles en el servidor donde se invoca.
     * 
     * @return Array de String con todos los identificadores de
     *         documentos XSLT contenidos en el servidor.
     * 
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         lsitado
     */
    @WebMethod
    public String[ ] getAllXSLTDocumentUUID( ) throws ServerErrorException;

    /**
     * Devuelve el contenido del documento HTML identificado por el
     * UUID recibido. Si dicho documento no existe en el servidor
     * donde se invoca, lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el UUID del documento HTML del que
     *        se quiere obtener el contenido.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento HTML para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         contenido.
     */
    @WebMethod
    public String getHTMLDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Devuelve el contenido del documento XML identificado por el
     * UUID recibido. Si dicho documento no existe en el servidor
     * donde se invoca, lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el UUID del documento XML del que
     *        se quiere obtener el contenido.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XML para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         contenido.
     */
    @WebMethod
    public String getXMLDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Devuelve el contenido del documento XSD identificado por el
     * UUID recibido. Si dicho documento no existe en el servidor
     * donde se invoca, lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el UUID del documento XSD del que
     *        se quiere obtener el contenido.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSD para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         contenido.
     */
    @WebMethod
    public String getXSDDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Devuelve el contenido del documento XSLT identificado por el
     * UUID recibido. Si dicho documento no existe en el servidor
     * donde se invoca, lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el UUID del documento XSLT del que
     *        se quiere obtener el contenido.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSLT para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         contenido.
     */
    @WebMethod
    public String getXSLTDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Devuelve el identificador UUID del documento XSD asociado al
     * XSLT identificado por el UUID recibido. Si dicho documento XSLT
     * no existe en el servidor donde se invoca, se lanzara un
     * DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el UUID del documento XSLT del que
     *        se quiere obtener el identificador del XSD al que esta
     *        asociado.
     * 
     * @return String representando el identificador UUID del
     *         documento XSD al que esta asociado el documento XSLT.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSLT para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la recuperacion del
     *         documento XSLT o del identificador del XSD.
     */
    @WebMethod
    public String getXSLTDocumentReferencedXSD(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina el documento HTML identificado por el UUID recibido. Si
     * el documento no existe en el servidor donde este metodo es
     * invocado, se lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        HTML que se desea eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento HTML para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la eliminacion del
     *         documento.
     */
    @WebMethod
    public void deleteHTMLDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina el documento XML identificado por el UUID recibido. Si
     * el documento no existe en el servidor donde este metodo es
     * invocado, se lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        XML que se desea eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XML para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la eliminacion del
     *         documento.
     */
    @WebMethod
    public void deleteXMLDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina el documento XSD identificado por el UUID recibido y
     * todos los documentos XSLT que esten asociados al mismo. Si
     * el documento no existe en el servidor donde este metodo es
     * invocado, se lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        XSD que se desea eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSD para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la eliminacion del
     *         documento.
     */
    @WebMethod
    public void deleteXSDDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina el documento XSLT identificado por el UUID recibido. Si
     * el documento no existe en el servidor donde este metodo es
     * invocado, se lanzara un DocumentNotFoundException.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        XSLT que se desea eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSLT para el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce un error durante la eliminacion del
     *         documento.
     */
    @WebMethod
    public void deleteXSLTDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

}

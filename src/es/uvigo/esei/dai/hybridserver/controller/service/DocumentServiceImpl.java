package es.uvigo.esei.dai.hybridserver.controller.service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.database.DAOFactory;
import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.entity.AbstractDocument;
import es.uvigo.esei.dai.hybridserver.database.entity.XSLTDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Clase que implementa la interfaz para los servicios web.
 * Proporciona métodos para listar todos los identificadores de los
 * documentos existentes en el servidor asi como para recuperar su
 * contenido y solicitar la eliminacion de los mismos.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.controller.service.DocumentService")
public class DocumentServiceImpl implements DocumentService
{

    /**
     * @see DocumentService#getAllHTMLDocumentUUID()
     */
    @Override
    public String[ ] getAllHTMLDocumentUUID( ) throws ServerErrorException
    {
        final List<String> list = list(DAOFactory.getDAO("html"));
        return list.toArray(new String[list.size()]);
    }

    /**
     * @see DocumentService#getAllXMLDocumentUUID()
     */
    @Override
    public String[ ] getAllXMLDocumentUUID( ) throws ServerErrorException
    {
        final List<String> list = list(DAOFactory.getDAO("xml"));
        return list.toArray(new String[list.size()]);
    }

    /**
     * @see DocumentService#getAllXSDDocumentUUID()
     */
    @Override
    public String[ ] getAllXSDDocumentUUID( ) throws ServerErrorException
    {
        final List<String> list = list(DAOFactory.getDAO("xsd"));
        return list.toArray(new String[list.size()]);
    }

    /**
     * @see DocumentService#getAllXSLTDocumentUUID()
     */
    @Override
    public String[ ] getAllXSLTDocumentUUID( ) throws ServerErrorException
    {
        final List<String> list = list(DAOFactory.getDAO("xslt"));
        return list.toArray(new String[list.size()]);
    }

    /**
     * @see DocumentService#getHTMLDocumentContent(String)
     */
    @Override
    public String getHTMLDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return getContent(uuid, DAOFactory.getDAO("html"));
    }

    /**
     * @see DocumentService#getXMLDocumentContent(String)
     */
    @Override
    public String getXMLDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return getContent(uuid, DAOFactory.getDAO("xml"));
    }

    /**
     * @see DocumentService#getXSDDocumentContent(String)
     */
    @Override
    public String getXSDDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return getContent(uuid, DAOFactory.getDAO("xsd"));
    }

    /**
     * @see DocumentService#getXSLTDocumentContent(String)
     */
    @Override
    public String getXSLTDocumentContent(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return getContent(uuid, DAOFactory.getDAO("xslt"));
    }

    /**
     * @see DocumentService#getXSLTDocumentReferencedXSD(String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public String getXSLTDocumentReferencedXSD(String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return getReferencedXSD(
            uuid,
            (DocumentDAO<XSLTDocument>) DAOFactory.getDAO("xslt")
        );
    }

    /**
     * @see DocumentService#deleteHTMLDocument(String)
     */
    @Override
    public void deleteHTMLDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        deleteDocument(uuid, DAOFactory.getDAO("html"));
    }

    /**
     * @see DocumentService#deleteXMLDocument(String)
     */
    @Override
    public void deleteXMLDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        deleteDocument(uuid, DAOFactory.getDAO("xml"));
    }

    /**
     * @see DocumentService#deleteXSDDocument(String)
     */
    @Override
    public void deleteXSDDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        deleteDocument(uuid, DAOFactory.getDAO("xsd"));
    }

    /**
     * @see DocumentService#deleteXSLTDocument(String)
     */
    @Override
    public void deleteXSLTDocument(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        deleteDocument(uuid, DAOFactory.getDAO("xslt"));
    }


    /**
     * Obtiene todo el listado de identificadores UUID de un tipo de
     * documento, dado el DAO asociado a dicho documento.
     * 
     * @param dao
     *        DocumentDAO que accede a los datos del tipo de documento
     *        del que se desea obtener el listado de UUID.
     * 
     * @return List de String conteniendo todos los UUID del servidor
     *         para el tipo de documento solicitado a traves de su
     *         DAO.
     * 
     * @throws ServerErrorException
     *         Si se produce algun error durante el acceso a datos.
     */
    private List<String> list(final DocumentDAO<?> dao)
        throws ServerErrorException
    {
        try {

            final List<String> uuidList = new LinkedList<>();
            for (final AbstractDocument doc : dao.list())
                uuidList.add(doc.getUUID());

            return uuidList;

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database error", sqe);
        }
    }

    /**
     * Obtiene el contenido de un documento dado su identificador UUID
     * y el DAO de acceso a datos para dicho tipo de documento.
     * 
     * @param uuid
     *        El identificador UUID del documento del que se desea
     *        obtener el contenido.
     * @param dao
     *        DocumentDAO que accede a los datos del tipo de documento
     *        del que se desea obtener el contenido.
     * 
     * @return String con el contenido del documento solicitado.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento correspondiente al
     *         identificador UUID proporcionado.
     * @throws ServerErrorException
     *         Si se produce algun error durante el acceso a datos.
     */
    private String getContent(final String uuid, final DocumentDAO<?> dao)
        throws DocumentNotFoundException, ServerErrorException
    {
        try {

            return dao.get(uuid).getContent();

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database error", sqe);
        }
    }

    /**
     * Obtiene el identificador del XSD referenciado por un documento
     * XSLT del que se conoce el UUID.
     * 
     * @param uuid
     *        El identificador UUID del documento XSLT del que se
     *        desea conocer el XSD al que esta asociado.
     * @param dao
     *        DocumentDAO para el acceso a datos de documentos XSLT.
     * 
     * @return String representando el identificador UUID del
     *         documento XSD al que esta asociado el XSLT identificado
     *         por el UUID recibido.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento XSLT con el
     *         identificador proporcionado.
     * @throws ServerErrorException
     *         Si se produce algun error durante el acceso a datos.
     */
    private String getReferencedXSD(String uuid, DocumentDAO<XSLTDocument> dao)
        throws DocumentNotFoundException, ServerErrorException
    {
        try {

            return dao.get(uuid).getXSD();

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database error", sqe);
        }
    }

    /**
     * Elimina del sistema el documento identificado por el UUID
     * recibido como parametro, apoyandose para ello en el DocumentDAO
     * tambien recibido como parametro.
     * 
     * @param uuid
     *        El identificador UUID del documento que se desea
     *        eliminar.
     * @param dao
     *        DocumentDAO para el tipo de documento que se desea
     *        eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento con el
     *         identificador recibido.
     * @throws ServerErrorException
     *         Si se produce algun error durante el acceso a datos.
     */
    private void deleteDocument(final String uuid, final DocumentDAO<?> dao)
        throws DocumentNotFoundException, ServerErrorException
    {
        try {

            dao.delete(uuid);

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database error", sqe);
        }
    }

}

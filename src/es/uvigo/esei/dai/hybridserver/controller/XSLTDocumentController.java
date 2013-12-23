package es.uvigo.esei.dai.hybridserver.controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.controller.service.DocumentService;
import es.uvigo.esei.dai.hybridserver.controller.utils.WSUtils;
import es.uvigo.esei.dai.hybridserver.database.DAOFactory;
import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.entity.XSDDocument;
import es.uvigo.esei.dai.hybridserver.database.entity.XSLTDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Controlador concreto para documentos XSLT.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
class XSLTDocumentController extends AbstractController<XSLTDocument> implements DocumentController
{

    private final DocumentDAO<XSDDocument> xsdDAO;

    /**
     * Construye una nueva instacia del controlador concreto y del
     * controlador abstracto padre asociado.
     * 
     * @throws ServerErrorException
     *         Si por algun motivo es imposible obtener el DAO para
     *         acceder a los datos de documentos XSLT o XSD.
     */
    @SuppressWarnings("unchecked")
    public XSLTDocumentController( ) throws ServerErrorException
    {
        super();
        xsdDAO = (DocumentDAO<XSDDocument>) DAOFactory.getDAO("xsd");
    }

    /**
     * Ademas de construir el documento XSLT, valida que se reciba un
     * identificador de documento XSD valido (existente) para el
     * mismo en extra[0].
     * 
     * @see AbstractController#documentFactory(String, String[ ])
     */
    @Override
    protected XSLTDocument documentFactory(final String content, final String ... extra)
        throws DocumentNotFoundException, SQLException, ServerErrorException
    {
        if (extra.length != 1)
            throw new RuntimeException("XSLT Document creation requires an XSD reference, check your code.");

        final String xsd = extra[0];
        if (xsd == null)
            throw new DocumentNotFoundException(xsd, "XSLT Document creation requires an XSD reference.");
        if (!xsdDAO.exists(xsd) && !copyRemoteXSD(xsd))
            throw new DocumentNotFoundException(xsd, "Referenced XSD Document not found.");

        return new XSLTDocument(xsd, content);
    }

    /**
     * @see AbstractController#getDAO()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected DocumentDAO<XSLTDocument> getDAO( ) throws ServerErrorException
    {
        return (DocumentDAO<XSLTDocument>) DAOFactory.getDAO("xslt");
    }

    /**
     * @see DocumentController#getMIMEType()
     */
    @Override
    public String getMIMEType( )
    {
        return "text/xml;charset=UTF-8";
    }

    /**
     * @see AbstractController#listRemote(DocumentService)
     */
    @Override
    protected List<String> listRemote(final DocumentService service)
        throws ServerErrorException
    {
        return Arrays.asList(service.getAllXSLTDocumentUUID());
    }

    /**
     * @see AbstractController#getRemote(DocumentService, String)
     */
    @Override
    protected XSLTDocument getRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return new XSLTDocument(
            uuid,
            service.getXSLTDocumentReferencedXSD(uuid),
            service.getXSLTDocumentContent(uuid)
        );
    }

    /**
     * @see AbstractController#deleteRemote(DocumentService, String)
     */
    @Override
    protected void deleteRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        service.deleteXSLTDocument(uuid);
    }

    /**
     * Busca en los servidores remotos un documento XSD con el
     * identificador UUID recibido como parametro. Si lo localiza, lo
     * copia en el DAO local y devuelve true. Si no lo encuentra
     * devuelve false.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        XSD que se desea recuperar.
     * 
     * @return True si se ha encontrado (y copiado a local) el
     *         documento XSD solicitado. False en caso contrario.
     * 
     * @throws SQLException
     *         Si se produce un error de acceso a datos durante la
     *         copia del documento al DAO local.
     */
    private boolean copyRemoteXSD(final String uuid) throws SQLException
    {
        final Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        for (final DocumentService service : services.values()) {
            try {

                final List<String> uuids =
                    Arrays.asList(service.getAllXSDDocumentUUID());

                if (uuids.contains(uuid)) {
                    xsdDAO.create(new XSDDocument(
                        uuid,
                        service.getXSDDocumentContent(uuid)
                    ));
                    return true;
                }

            } catch (final DocumentNotFoundException _) {
                // no se hace nada, puesto que se ha comprobado que no
                // se lance con el "uuids.contains(uuid)", y si por
                // algun motivo se lanza, se deja que continue con el
                // siguiente servidor del listado
            } catch (final ServerErrorException see) {
                System.err.println("Remote server error: " + see.getMessage());
            }
        }

        return false;
    }

}

package es.uvigo.esei.dai.hybridserver.controller;

import java.util.Arrays;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.controller.service.DocumentService;
import es.uvigo.esei.dai.hybridserver.database.DAOFactory;
import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.entity.XSDDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Controlador concreto para documentos XSD.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
class XSDDocumentController extends AbstractController<XSDDocument> implements DocumentController
{

    /**
     * Construye una nueva instacia del controlador concreto y del
     * controlador abstracto padre asociado.
     * 
     * @throws ServerErrorException
     *         Si por algun motivo es imposible obtener el DAO para
     *         acceder a los datos de documentos XSD.
     */
    public XSDDocumentController( ) throws ServerErrorException
    {
        super();
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
     * @see AbstractController#documentFactory(String, String[ ])
     */
    @Override
    protected XSDDocument documentFactory(final String content, final String... extra)
    {
        return new XSDDocument(content);
    }

    /**
     * @see AbstractController#getDAO()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected DocumentDAO<XSDDocument> getDAO( ) throws ServerErrorException
    {
        return (DocumentDAO<XSDDocument>) DAOFactory.getDAO("xsd");
    }

    /**
     * @see AbstractController#listRemote(DocumentService)
     */
    @Override
    protected List<String> listRemote(final DocumentService service)
        throws ServerErrorException
    {
        return Arrays.asList(service.getAllXSDDocumentUUID());
    }

    /**
     * @see AbstractController#getRemote(DocumentService, String)
     */
    @Override
    protected XSDDocument getRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return new XSDDocument(uuid, service.getXSDDocumentContent(uuid));
    }

    /**
     * @see AbstractController#deleteRemote(DocumentService, String)
     */
    @Override
    protected void deleteRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        service.deleteXSDDocument(uuid);
    }

}

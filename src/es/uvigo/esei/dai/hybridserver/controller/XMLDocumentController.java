package es.uvigo.esei.dai.hybridserver.controller;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.hybridserver.controller.service.DocumentService;
import es.uvigo.esei.dai.hybridserver.controller.utils.WSUtils;
import es.uvigo.esei.dai.hybridserver.controller.utils.XMLUtils;
import es.uvigo.esei.dai.hybridserver.database.DAOFactory;
import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.entity.XMLDocument;
import es.uvigo.esei.dai.hybridserver.database.entity.XSDDocument;
import es.uvigo.esei.dai.hybridserver.database.entity.XSLTDocument;
import es.uvigo.esei.dai.hybridserver.exception.BadRequestException;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Controlador concreto para documentos XML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
class XMLDocumentController extends AbstractController<XMLDocument> implements DocumentController
{

    private final DocumentDAO<XSDDocument>  xsdDAO;
    private final DocumentDAO<XSLTDocument> xsltDAO;

    /**
     * Construye una nueva instacia del controlador concreto y del
     * controlador abstracto padre asociado.
     * 
     * @throws ServerErrorException
     *         Si por algun motivo es imposible obtener el DAO para
     *         acceder a los datos de documentos XML, XSD o XSLT.
     */
    @SuppressWarnings("unchecked")
    public XMLDocumentController( ) throws ServerErrorException
    {
        super();
        xsdDAO  = (DocumentDAO<XSDDocument>)  DAOFactory.getDAO("xsd");
        xsltDAO = (DocumentDAO<XSLTDocument>) DAOFactory.getDAO("xslt");
    }

    /**
     * Sobreescribe la implementacion por defecto del controlador
     * abstracto, proporcionando la posibilidad de realizar una
     * transformacion a traves de un documento XSLT, recibiendo como
     * parametro opcional en extra[0] el identificador UUID del mismo.
     * Si esa transformacion se solicita, se realiza la validacion del
     * documento XML solicitado con el XSD al que el XSLT esta
     * asociado y devuelve el resultado de la transformacion
     * realizada. Si no se solicita una transformacion, simplemente
     * devuelve la implementacion por defecto del controlador
     * abstracto.
     * 
     * @see AbstractController#get(String, String[ ])
     */
    @Override
    public String get(final String uuid, final String... extra)
        throws BadRequestException, DocumentNotFoundException, ServerErrorException
    {
        final String xslt = extra[0];
        if (extra.length == 0 || xslt == null)
            return super.get(uuid, extra);

        final XMLDocument  document;
        final XSLTDocument transformer;
        final XSDDocument  schema;

        try {

            if (!dao.exists(uuid)) {
                document = getRemote(uuid);
                dao.create(document);
            } else
                document = dao.get(uuid);


            if (!xsltDAO.exists(xslt)) {
                transformer = getRemoteXSLT(xslt);
                xsltDAO.create(transformer);
            } else
                transformer = xsltDAO.get(xslt);

            try {
                final String xsd = transformer.getXSD();

                if (!xsdDAO.exists(xsd)) {
                    schema = getRemoteXSD(xsd);
                    xsdDAO.create(schema);
                } else
                    schema = xsdDAO.get(xsd);

            } catch (final DocumentNotFoundException dnfe) {
                throw new BadRequestException("XSD not found", dnfe);
            }

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database Error", sqe);
        }

        validate(document, schema);
        return transform(document, transformer);
    }

    /**
     * @see DocumentController#getMIMEType()
     */
    @Override
    public String getMIMEType( )
    {
        // TODO: y si se ha solicitado la transformacion con un XSLT?
        // deberia seguir enviandose un "text/xml"?
        return "text/xml;charset=UTF-8";
    }

    /**
     * @see AbstractController#documentFactory(String, String[ ])
     */
    @Override
    protected XMLDocument documentFactory(final String content, final String ... extra)
    {
        return new XMLDocument(content);
    }

    /**
     * @see AbstractController#getDAO()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected DocumentDAO<XMLDocument> getDAO( ) throws ServerErrorException
    {
        return (DocumentDAO<XMLDocument>) DAOFactory.getDAO("xml");
    }

    /**
     * @see AbstractController#listRemote(DocumentService)
     */
    @Override
    protected List<String> listRemote(final DocumentService service)
        throws ServerErrorException
    {
        return Arrays.asList(service.getAllXMLDocumentUUID());
    }

    /**
     * @see AbstractController#getRemote(DocumentService, String)
     */
    @Override
    protected XMLDocument getRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        return new XMLDocument(uuid, service.getXMLDocumentContent(uuid));
    }

    /**
     * @see AbstractController#deleteRemote(DocumentService, String)
     */
    @Override
    protected void deleteRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        service.deleteXMLDocument(uuid);
    }

    /**
     * Obtiene un XSD remoto a traves del UUID que lo identifica.
     * Busca en todos los servidores remotos configurados en el
     * sistema hasta encontrarlo.
     * 
     * @param uuid
     *        Identificador UUID del documento XSD que se desea
     *        recuperar de los servidores remotos.
     * 
     * @return Documento XSD asociado al UUID recibido.
     * 
     * @throws DocumentNotFoundException
     *         Si ninguno de los servidores remotos contiene al
     *         documento XSD identificado por el UUID recibido.
     */
    private XSDDocument getRemoteXSD(final String uuid)
        throws DocumentNotFoundException
    {
        final Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        for (final DocumentService service : services.values()) {
            try {

                final List<String> uuids =
                    Arrays.asList(service.getAllXSDDocumentUUID());

                if (uuids.contains(uuid)) {
                    return new XSDDocument(
                        uuid,
                        service.getXSDDocumentContent(uuid)
                    );
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

        throw new DocumentNotFoundException(uuid);
    }

    /**
     * Obtiene un XSLT remoto a traves del UUID que lo identifica.
     * Busca en todos los servidores remotos configurados en el
     * sistema hasta encontrarlo.
     * 
     * @param uuid
     *        Identificador UUID del documento XSLT que se desea
     *        recuperar de los servidores remotos.
     * 
     * @return Documento XSLT asociado al UUID recibido.
     * 
     * @throws DocumentNotFoundException
     *         Si ninguno de los servidores remotos contiene al
     *         documento XSLT identificado por el UUID recibido.
     */
    private XSLTDocument getRemoteXSLT(final String uuid)
        throws DocumentNotFoundException
    {
        final Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        for (final DocumentService service : services.values()) {
            try {

                final List<String> uuids =
                    Arrays.asList(service.getAllXSLTDocumentUUID());

                if (uuids.contains(uuid)) {
                    return new XSLTDocument(
                        uuid,
                        service.getXSLTDocumentReferencedXSD(uuid),
                        service.getXSLTDocumentContent(uuid)
                    );
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

        throw new DocumentNotFoundException(uuid);
    }

    /**
     * Valida un documento XML con un documento XSD. Si la validacion
     * es correcta no devolvera nada, si es incorrecta se lanzara una
     * excepcion BadRequestException.
     * 
     * @param xml
     *        Documento XML a valida.
     * @param xsd
     *        Documento XSD que servira como validador.
     * 
     * @throws BadRequestException
     *         Si la validacion ha resultado incorrecta.
     * @throws ServerErrorException
     *         Si se produce algun error por parte del servidor
     *         durante la validacion.
     */
    private void validate(final XMLDocument xml, final XSDDocument xsd)
        throws BadRequestException, ServerErrorException
    {
        try {

            XMLUtils.validate(
                new StreamSource(new StringReader(xml.getContent())),
                new StreamSource(new StringReader(xsd.getContent()))
            );

        } catch (final SAXException e) {
            throw new BadRequestException("Invalid XML");
        } catch (final IOException ioe) {
            throw new ServerErrorException(ioe);
        }
    }

    /**
     * Transforma un documento XML con un documento XSLT devolviendo
     * el resultado de la transformacion como un String.
     * 
     * @param xml
     *        Documento XML a transformar.
     * @param xslt
     *        Documento XSLT que servira como transformador.
     * 
     * @return String conteniendo el resultado de la transformacion.
     * 
     * @throws ServerErrorException
     *         Si se produce algun error durante la transformacion del
     *         documento XML.
     */
    private String transform(final XMLDocument xml, final XSLTDocument xslt)
        throws ServerErrorException
    {
        try {

            final Writer outputWriter = new StringWriter();

            XMLUtils.transform(
                new StreamSource(new StringReader(xml.getContent())),
                new StreamSource(new StringReader(xslt.getContent())),
                new StreamResult(outputWriter)
            );

            return outputWriter.toString();

        } catch (final TransformerException te) {
            throw new ServerErrorException("Transformation Error", te);
        }
    }

}

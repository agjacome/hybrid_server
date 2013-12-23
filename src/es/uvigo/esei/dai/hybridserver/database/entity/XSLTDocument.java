package es.uvigo.esei.dai.hybridserver.database.entity;

import java.util.UUID;

/**
 * Clase concreta para documentos XSLT.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XSLTDocument extends AbstractDocument
{

    // referencia al identificador UUID del XSD asociado
    private final UUID xsd;

    /**
     * Crea un nuevo documento XSLT dado su contenido y la referencia
     * al documento XSD asociado.
     * 
     * @param xsd
     *        String representando el identificador UUID del documento
     *        XSD al que este XSLT esta asociado.
     * @param content
     *        String con el contenido del documento XSLT a crear.
     */
    public XSLTDocument(final String xsd, final String content)
    {
        super(content);
        this.xsd = UUID.fromString(xsd);
    }

    /**
     * Crea un nuevo documento XSLT dado su identificador UUID, su
     * contenido y la referencia al documento XSD asociado.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        XSLT a crear.
     * @param xsd
     *        String representando el identificador UUID del documento
     *        XSD al que este documento XSLT esta asociado.
     * @param content
     *        String con el contenido del documento XSLT a crear.
     */
    public XSLTDocument(final String uuid, final String xsd, final String content)
    {
        super(uuid, content);
        this.xsd = UUID.fromString(xsd);
    }

    /**
     * Devuelve el identificador UUID del documento XSD al que este
     * documento XSLT esta asociado.
     * 
     * @return String representando el identificador UUID del
     *         documento XSD al que este XSLT esta asociado.
     */
    public String getXSD( )
    {
        return xsd.toString();
    }

}

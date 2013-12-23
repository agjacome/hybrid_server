package es.uvigo.esei.dai.hybridserver.database.entity;

/**
 * Clase concreta para documentos XSD.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XSDDocument extends AbstractDocument
{

    /**
     * Crea un nuevo documento XSD dado su contenido. El
     * identificador UUID se generara automaticamente.
     * 
     * @param content
     *        String con el contenido del documento XSD a crear.
     */
    public XSDDocument(final String content)
    {
        super(content);
    }

    /**
     * Crea un nuevo documento XSD dado su identificador UUID y su
     * contenido.
     * 
     * @param uuid
     *        String representando el identificador UUID del
     *        documento.
     * @param content
     *        String con el contenido del documento XSD a crear.
     */
    public XSDDocument(final String uuid, final String content)
    {
        super(uuid, content);
    }

}

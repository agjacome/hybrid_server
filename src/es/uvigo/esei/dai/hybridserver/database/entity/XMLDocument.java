package es.uvigo.esei.dai.hybridserver.database.entity;

/**
 * Clase concreta para documentos XML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XMLDocument extends AbstractDocument
{

    /**
     * Crea un nuevo documento XML dado su contenido. El
     * identificador UUID se generara automaticamente.
     * 
     * @param content
     *        String con el contenido del documento XML a crear.
     */
    public XMLDocument(final String content)
    {
        super(content);
    }

    /**
     * Crea un nuevo documento XML dado su identificador UUID y su
     * contenido.
     * 
     * @param uuid
     *        String representando el identificador UUID del
     *        documento.
     * @param content
     *        String con el contenido del documento XML a crear.
     */
    public XMLDocument(final String uuid, final String content)
    {
        super(uuid, content);
    }

}

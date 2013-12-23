package es.uvigo.esei.dai.hybridserver.database.entity;

/**
 * Clase concreta para documentos HTML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class HTMLDocument extends AbstractDocument
{

    /**
     * Crea un nuevo documento HTML dado su contenido. El
     * identificador UUID se generara automaticamente.
     * 
     * @param content
     *        String con el contenido del documento HTML a crear.
     */
    public HTMLDocument(final String content)
    {
        super(content);
    }

    /**
     * Crea un nuevo documento HTML dado su identificador UUID y su
     * contenido.
     * 
     * @param uuid
     *        String representando el identificador UUID del
     *        documento.
     * @param content
     *        String con el contenido del documento HTML a crear.
     */
    public HTMLDocument(final String uuid, final String content)
    {
        super(uuid, content);
    }

}

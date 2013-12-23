package es.uvigo.esei.dai.hybridserver.database.entity;

import java.util.UUID;

/**
 * Clase abstracta con operaciones genericas para todos los documentos
 * del sistema. Todo documento del sistema debe estar representado en
 * una clase concreta que herede obligatoriamente de ésta.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public abstract class AbstractDocument
{

    protected final UUID   uuid;    // identificador del documento
    protected final String content; // contenido del documento

    /**
     * Construye una nueva instancia de esta clase abstracta (un nuevo
     * documento) dado un contenido para la misma. El UUID se generara
     * aleatoriamente.
     * 
     * @param content
     *        String con el contenido para el documento.
     */
    public AbstractDocument(final String content)
    {
        uuid = UUID.randomUUID();
        this.content = content;
    }

    /**
     * Construye una nueva instancia de esta clase abstracta (un nuevo
     * documento) dado un identificador UUID y el contenido del
     * documento.
     * 
     * @param uuid
     *        String representando el identificador UUID del documento
     *        a crear.
     * @param content
     *        String representando el contenido del documento a crear.
     */
    public AbstractDocument(final String uuid, final String content)
    {
        this.uuid    = UUID.fromString(uuid);
        this.content = content;
    }

    /**
     * Devuelve el identificador UUID del documento.
     * 
     * @return String representando el identificador UUID del
     *         documento.
     */
    public String getUUID( )
    {
        return uuid.toString();
    }

    /**
     * Devuelve el contenido del documento.
     * 
     * @return String representando el contenido del documento.
     */
    public String getContent( )
    {
        return content;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode( )
    {
        return uuid.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj)
    {
        if (obj instanceof AbstractDocument) {
            final AbstractDocument that = (AbstractDocument) obj;
            return getUUID().equals(that.getUUID());
        }

        return false;
    }

}

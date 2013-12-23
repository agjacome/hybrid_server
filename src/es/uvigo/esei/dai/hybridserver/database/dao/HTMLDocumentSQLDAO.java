package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.database.entity.HTMLDocument;

/**
 * DAO de SQL concreto para documentos HTML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class HTMLDocumentSQLDAO extends AbstractDocumentSQLDAO<HTMLDocument> implements DocumentDAO<HTMLDocument>
{

    /**
     * @see AbstractDocumentSQLDAO#documentFactory(ResultSet)
     */
    @Override
    protected HTMLDocument documentFactory(final ResultSet resultSet)
        throws SQLException
    {
        final String uuid    = resultSet.getString(UUID_NAME);
        final String content = resultSet.getString(CONTENT_NAME);

        return new HTMLDocument(uuid, content);
    }

    /**
     * @see AbstractDocumentSQLDAO#getContentName()
     */
    @Override
    protected String getContentName( )
    {
        return "content";
    }

    /**
     * @see AbstractDocumentSQLDAO#getTableName()
     */
    @Override
    protected String getTableName( )
    {
        return "HTML";
    }

    /**
     * @see AbstractDocumentSQLDAO#getUUIDName()
     */
    @Override
    protected String getUUIDName( )
    {
        return "uuid";
    }

}

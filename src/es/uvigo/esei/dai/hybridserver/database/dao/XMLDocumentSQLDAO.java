package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.database.entity.XMLDocument;

/**
 * DAO de SQL concreto para documentos XML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XMLDocumentSQLDAO extends AbstractDocumentSQLDAO<XMLDocument> implements DocumentDAO<XMLDocument>
{

    /**
     * @see AbstractDocumentSQLDAO#documentFactory(ResultSet)
     */
    @Override
    protected XMLDocument documentFactory(final ResultSet resultSet)
        throws SQLException
    {
        final String uuid    = resultSet.getString(UUID_NAME);
        final String content = resultSet.getString(CONTENT_NAME);

        return new XMLDocument(uuid, content);
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
        return "XMLT";
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

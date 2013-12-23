package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uvigo.esei.dai.hybridserver.database.ConnectionFactory;
import es.uvigo.esei.dai.hybridserver.database.entity.XSLTDocument;

/**
 * DAO de SQL concreto para documentos XSLT.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XSLTDocumentSQLDAO extends AbstractDocumentSQLDAO<XSLTDocument> implements DocumentDAO<XSLTDocument>
{

    // columna que almacena la referencia al documento XSD
    private final String XSD_NAME = "xsd";

    /**
     * Sobreescribe la implementacion base del dao abstracto para
     * insertar correctamente los documentos XSLT, puesto que ademas
     * de UUID y contenido cuentan tambien con una referencia a un
     * documento XSD.
     * 
     * @see AbstractDocumentSQLDAO#create(AbstractDocument)
     */
    @Override
    public void create(final XSLTDocument document) throws SQLException
    {
        final String insert = "INSERT INTO " + TABLE_NAME + " "
                            + "(" + UUID_NAME + ", " + CONTENT_NAME + ", " + XSD_NAME + ") "
                            + "VALUES(?, ?, ?)";

        try (
            final Connection database = ConnectionFactory.getConnection();
            final PreparedStatement statement =
                database.prepareStatement(insert)
        ) {

            statement.setString(1, document.getUUID());
            statement.setString(2, document.getContent());
            statement.setString(3, document.getXSD());

            if (statement.executeUpdate() != 1)
                throw new SQLException("Error while inserting into database");

        }
    }

    /**
     * @see AbstractDocumentSQLDAO#documentFactory(ResultSet)
     */
    @Override
    protected XSLTDocument documentFactory(final ResultSet resultSet)
        throws SQLException
    {
        final String uuid    = resultSet.getString(UUID_NAME);
        final String xsd     = resultSet.getString(XSD_NAME);
        final String content = resultSet.getString(CONTENT_NAME);

        return new XSLTDocument(uuid, xsd, content);
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
        return "XSLT";
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

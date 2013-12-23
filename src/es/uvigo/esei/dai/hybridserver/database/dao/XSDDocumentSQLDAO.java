package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.database.entity.XSDDocument;
import es.uvigo.esei.dai.hybridserver.database.entity.XSLTDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;

/**
 * DAO de SQL concreto para documentos XSD.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XSDDocumentSQLDAO extends AbstractDocumentSQLDAO<XSDDocument> implements DocumentDAO<XSDDocument>
{

    /**
     * Sobreescribe la implementacion base del DAO abstracto para
     * realizar el borrado en cascada de todos los XSLT asociados al
     * XSD que se esta eliminando.
     * 
     * @see AbstractDocumentSQLDAO#delete(String)
     */
    @Override
    public void delete(final String uuid)
        throws DocumentNotFoundException, SQLException
    {
        // primero elimina el XSD
        super.delete(uuid);

        // y despues elimina todos los XSLT asociados a dicho XSD
        // TODO: discutir esta solucion de invocar al DAO de XSLT,
        // porque se esta creando un acoplamiento entre ellos y quizas
        // no sea la forma adecuada
        final DocumentDAO<XSLTDocument> xsltDAO = new XSLTDocumentSQLDAO();
        final List<XSLTDocument> list = xsltDAO.list();
        for (XSLTDocument xslt : list) {
            if (xslt.getXSD().equals(uuid))
                xsltDAO.delete(xslt.getUUID());
        }
    }

    /**
     * @see AbstractDocumentSQLDAO#documentFactory(ResultSet)
     */
    @Override
    protected XSDDocument documentFactory(final ResultSet resultSet)
        throws SQLException
    {
        final String uuid = resultSet.getString(UUID_NAME);
        final String content = resultSet.getString(CONTENT_NAME);

        return new XSDDocument(uuid, content);
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
        return "XSD";
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

package es.uvigo.esei.dai.hybridserver.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.dai.hybridserver.database.ConnectionFactory;
import es.uvigo.esei.dai.hybridserver.database.entity.AbstractDocument;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;

/**
 * Clase abstracta con metodos basicos para todos los DAO de SQL. Debe
 * ser implementada por cada DAO concreto proporcionando el tipo de
 * documento al que se asocia, sobreescribiendo los distintos metodos
 * segun sea necesario y proporcionando una implementacion a todos los
 * metodos abstractos aqui definidos.
 * 
 * @param <D>
 *        Documento al que estara asociado el DAO de SQL concreto.
 *        Debe, obligatoriamente, heredar de {@link AbstractDocument}.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
abstract class AbstractDocumentSQLDAO<D extends AbstractDocument> implements DocumentDAO<D>
{

    protected final String TABLE_NAME;   // nombre de la tabla
    protected final String UUID_NAME;    // columna para UUID
    protected final String CONTENT_NAME; // columna para contenido

    /**
     * Construye una nueva instancia del DAO de SQL abstracto,
     * obteniendo los nombres de las tablas y columnas necesarias para
     * todo tipo de documento.
     */
    public AbstractDocumentSQLDAO( )
    {
        TABLE_NAME   = getTableName();
        UUID_NAME    = getUUIDName();
        CONTENT_NAME = getContentName();
    }

    /**
     * @see DocumentDAO#exists(String)
     */
    @Override
    public boolean exists(final String uuid) throws SQLException
    {
        final String select = "SELECT COUNT(*) "
                            + "FROM " + TABLE_NAME + " "
                            + "WHERE " + UUID_NAME + " = ?";

        try (
          final Connection database = ConnectionFactory.getConnection();
          final PreparedStatement statement = database.prepareStatement(select)
        ) {

            statement.setString(1, uuid);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next())
                    throw new SQLException("Error while querying database");

                return resultSet.getInt(1) != 0;
            }

        }
    }

    /**
     * @see DocumentDAO#list()
     */
    @Override
    public List<D> list( ) throws SQLException
    {
        final String select = "SELECT * FROM " + TABLE_NAME;

        try (
          final Connection database = ConnectionFactory.getConnection();
          final PreparedStatement statement = database.prepareStatement(select)
        ) {

            final List<D> list = new LinkedList<>();

            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    list.add(documentFactory(resultSet));

                return list;
            }

        }
    }

    /**
     * @see DocumentDAO#get(String)
     */
    @Override
    public D get(final String uuid) throws DocumentNotFoundException,
        SQLException
    {
        final String select = "SELECT * "
                            + "FROM " + TABLE_NAME + " "
                            + "WHERE " + UUID_NAME + " = ?";

        try (
            final Connection database = ConnectionFactory.getConnection();
            final PreparedStatement statement =
                database.prepareStatement(select)) {

            statement.setString(1, uuid);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next())
                    throw new DocumentNotFoundException(uuid);

                return documentFactory(resultSet);
            }
        }
    }

    /**
     * @see DocumentDAO#create(AbstractDocument)
     */
    @Override
    public void create(final D document) throws SQLException
    {
        final String insert = "INSERT INTO " + TABLE_NAME
                            + " (" + UUID_NAME + ", " + CONTENT_NAME + ")"
                            + "VALUES(?, ?)";

        try (
          final Connection database  = ConnectionFactory.getConnection();
          final PreparedStatement statement = database.prepareStatement(insert)
        ) {

            statement.setString(1, document.getUUID());
            statement.setString(2, document.getContent());

            if (statement.executeUpdate() != 1)
                throw new SQLException("Error while inserting into database");

        }
    }

    /**
     * @see DocumentDAO#update(AbstractDocument)
     */
    @Override
    public void update(final D document)
        throws DocumentNotFoundException, SQLException
    {
        final String update = "UPDATE " + TABLE_NAME + " "
                            + "SET " + CONTENT_NAME + " = ? "
                            + "WHERE " + UUID_NAME + " = ?";

        try (
          final Connection database = ConnectionFactory.getConnection();
          final PreparedStatement statement = database.prepareStatement(update)
        ) {

            statement.setString(1, document.getContent());
            statement.setString(2, document.getUUID());

            if (statement.executeUpdate() != 1)
                throw new DocumentNotFoundException(document.getUUID());

        }
    }

    /**
     * @see DocumentDAO#delete(String)
     */
    @Override
    public void delete(final String uuid)
        throws DocumentNotFoundException, SQLException
    {
        final String delete =
                "DELETE FROM " + TABLE_NAME + " WHERE " + UUID_NAME + " = ?";

        try (
          final Connection database = ConnectionFactory.getConnection();
          final PreparedStatement statement = database.prepareStatement(delete)
        ) {

            statement.setString(1, uuid);

            if (statement.executeUpdate() != 1)
                throw new DocumentNotFoundException(uuid);

        }
    }

    /**
     * Devuelve el nombre de la tabla con la que trabajara el DAO
     * concreto.
     * 
     * @return String con el nombre de la tabla en la que se
     *         realizaran las operaciones SQL.
     */
    protected abstract String getTableName( );

    /**
     * Devuelve el nombre de la columna en la que se encuentran los
     * identificadores UUID del documento.
     * 
     * @return String con el nombre de la columna en la que se
     *         encuentran los UUID.
     */
    protected abstract String getUUIDName( );

    /**
     * Devuelve el nombre de la columna en la que se encuentran los
     * contenidos de cada documento.
     * 
     * @return String con el nombre de la columna en la que se
     *         encuentran los contenidos.
     */
    protected abstract String getContentName( );

    /**
     * Construye una nueva instancia del tipo de documento concreto,
     * dado un {@link ResultSet} desde el que obtener los datos
     * necesarios.
     * 
     * @param resultSet
     *        ResultSet con los resultados de una consulta SQL desde
     *        donde obtener todos los campos necesarios para la
     *        creacion de un documento concreto.
     * 
     * @return Documento del tipo correcto segun cada DAO de SQL
     *         concreto y que albergue los datos recuperados del
     *         ResultSet.
     * 
     * @throws SQLException
     *         Si se produce algun error SQL durante la recuperacion
     *         de los datos del ResultSet.
     */
    protected abstract D documentFactory(final ResultSet resultSet)
        throws SQLException;

}

package es.uvigo.esei.dai.hybridserver.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.uvigo.esei.dai.hybridserver.controller.service.DocumentService;
import es.uvigo.esei.dai.hybridserver.controller.utils.WSUtils;
import es.uvigo.esei.dai.hybridserver.database.dao.DocumentDAO;
import es.uvigo.esei.dai.hybridserver.database.entity.AbstractDocument;
import es.uvigo.esei.dai.hybridserver.exception.BadRequestException;
import es.uvigo.esei.dai.hybridserver.exception.DocumentNotFoundException;
import es.uvigo.esei.dai.hybridserver.exception.ServerErrorException;

/**
 * Clase abstracta con metodos basicos para todos los controladores.
 * Debe ser implementada por cada controlador concreto proporcionando
 * el tipo de documento al que se asocia, sobreescribiendo los
 * distintos metodos segun sea necesario y proporcionando una
 * implementacion a todos los metodos abstractos aqui definidos.
 * 
 * @param <D>
 *        Documento al que estara asociado el DAO concreto. Debe,
 *        obligatoriamente, heredar de {@link AbstractDocument}.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
abstract class AbstractController<D extends AbstractDocument> implements DocumentController
{

    // DAO con el que se comunicara este controlador
    protected DocumentDAO<D> dao;

    /**
     * Construye una nueva instancia de la clase abstracta, que se
     * encargara de obtener el DAO correcto llamando para ello al
     * metodo abstracto (a implementar por las clases concretas)
     * getDAO().
     * 
     * @throws ServerErrorException
     *         Si se ha producido algun tipo de error durante la
     *         obtencion del DAO.
     */
    public AbstractController( ) throws ServerErrorException
    {
        this.dao = getDAO();
    }

    /**
     * @see DocumentController#list()
     */
    @Override
    public Map<String, List<String>> list( ) throws ServerErrorException
    {
        final Map<String, List<String>> listing = new LinkedHashMap<>();

        fillLocalListing(listing);
        fillRemoteListing(listing);

        return listing;
    }

    /**
     * @see DocumentController#get(String, String[ ])
     */
    @Override
    public String get(final String uuid, final String... extra)
        throws DocumentNotFoundException, BadRequestException, ServerErrorException
    {
        try {

            final D document;

            if (!dao.exists(uuid)) {
                document = getRemote(uuid);
                dao.create(document);
            } else {
                document = dao.get(uuid);
            }

            return document.getContent();

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database Error", sqe);
        }
    }

    /**
     * @see DocumentController#create(String, String[ ])
     */
    @Override
    public String create(final String content, final String... extra)
        throws DocumentNotFoundException, ServerErrorException
    {
        try {

            final D document = documentFactory(content, extra);
            dao.create(document);

            return document.getUUID();

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database Error", sqe);
        }
    }

    /**
     * @see DocumentController#delete(String)
     */
    @Override
    public void delete(final String uuid)
        throws DocumentNotFoundException, ServerErrorException
    {
        try {

            dao.delete(uuid);
            deleteRemote(uuid);

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database Error", sqe);
        }
    }


    /**
     * Obtiene el DAO asociado al tipo de documento con el que el
     * controlador concreto trabajara.
     * 
     * @return DocumentDAO asociado al tipo de documento correcto.
     * 
     * @throws ServerErrorException
     *         Si se produce algun tipo de error intentando obtener
     *         una referencia al DAO.
     */
    protected abstract DocumentDAO<D> getDAO( ) throws ServerErrorException;

    /**
     * Construye una nueva instancia del tipo de documento concreto,
     * puesto que es imposible construir una instancia a traves de un
     * generic (ie: no se puede hacer "new D()").
     * 
     * @param content
     *        El contenido que el documento a devolver debe tener.
     * @param extra
     *        Parametros extra que seran necesarios o no segun cada
     *        tipo de documento.
     * 
     * @return Un instancia de una clase que herede de
     *         {@link AbstractDocument}.
     * 
     * @throws DocumentNotFoundException
     *         Si es necesaria la existencia de un documento previo
     *         para la creacion de este (posible uso de los varargs
     *         para recuperarlo), y dicho documento no se encuentra.
     * @throws ServerErrorException
     *         Si se produce algun tipo de error durante la creacion
     *         del documento.
     * @throws SQLException
     *         Si se produce algun tipo de error asociado al acceso a
     *         datos.
     */
    protected abstract D documentFactory(final String content, final String ... extra)
        throws DocumentNotFoundException, ServerErrorException, SQLException;

    /**
     * Obtiene un listado de todos los identificadores UUID de un
     * servidor remoto (accesible a traves del {@link DocumentService}
     * recibido como parametro). Debera devolverse el listdo asociado
     * al tipo de documento concreto con el que el controlador
     * trabaja.
     * 
     * @param service
     *        DocumentService desde el que recuperar el listado de
     *        identificadores.
     * 
     * @return List de String con todos los identificadores existentes
     *         para el tipo de documento concreto.
     * 
     * @throws ServerErrorException
     *         Si se produce algun tipo de error durante la
     *         recuperacion del listado.
     */
    protected abstract List<String> listRemote(final DocumentService service)
        throws ServerErrorException;

    /**
     * Crea un nuevo objeto del tipo de documento concreto de este
     * controlador, obteniendo el contenido del mismo remotamente,
     * apoyandose para ello en el {@link DocumentService} y el
     * identificador UUID que recibe como parametros.
     * 
     * @param service
     *        DocumentService desde el que recuperar el contenido del
     *        documento.
     * @param uuid
     *        Identificador del documento que se desea recuperar.
     * 
     * @return Documento con el identificador recibido y el contenido
     *         recuperado del servicio remoto.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento con el
     *         identificador recibido en el servicio remoto.
     * @throws ServerErrorException
     *         Si se produce algun tipo de error durante la
     *         recuperacion del contenido del documento.
     */
    protected abstract D getRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException;

    /**
     * Elimina un documento remoto del tipo al que el controlador
     * concreto este asociado, dados su idenfificador UUID y un
     * servicio donde se invocara la eliminacion.
     * 
     * @param service
     *        DocumentService desde el que eliminar el documento.
     * @param uuid
     *        Identificador del documento que se desea eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si no se encuentra ningun documento con el
     *         identificador recibido en el servicio remoto.
     * @throws ServerErrorException
     *         Si se produce algun tipo de error durante la
     *         eliminacion del documento remoto.
     */
    protected abstract void deleteRemote(final DocumentService service, final String uuid)
        throws DocumentNotFoundException, ServerErrorException;


    /**
     * Añade el listado local de documentos del tippo concreto al map
     * recibido como parametro, dentro de la clave "Local Server".
     * 
     * @param listing
     *        Map donde añadir el listado de identificadores UUID
     *        locales (para el tipo de documento del controlador
     *        concreto).
     * 
     * @throws ServerErrorException
     *         Si se produce algun tipo de error durante el acceso a
     *         datos.
     */
    private void fillLocalListing(Map<String, List<String>> listing)
        throws ServerErrorException
    {
        try {

            final List<String> list = new LinkedList<>();

            for (final D document : dao.list())
                list.add(document.getUUID());

            listing.put("Local Server", list);

        } catch (final SQLException sqe) {
            throw new ServerErrorException("Database Error", sqe);
        }
    }

    /**
     * Añade cada uno de los distintos listados remotos (uno para cada
     * servicio existente configurado) al map recibido como parametro,
     * cada uno de ellos teniendo como clave el nombre del servidor
     * remoto (segun fichero de configuracion).
     * 
     * @param listing
     *        Map donde añadir el listado de identificadores UUID
     *        remotos (para el tipo de documento del controlador
     *        concreto).
     */
    private void fillRemoteListing(Map<String, List<String>> listing)
    {
        Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        for (final Entry<String, DocumentService> server : services.entrySet()) {
            try {
                listing.put(server.getKey(), listRemote(server.getValue()));
            } catch (final ServerErrorException _) {
                listing.put(server.getKey(), Collections.<String>emptyList());
            }
        }
    }

    /**
     * Obtiene un documento remoto a través de un identificador
     * proporcionado. Recorre todos los servidores remotos
     * configurados buscando dicho documento.
     * 
     * @param uuid
     *        El identificador UUID del documento a recuperar.
     * 
     * @return Un documento del tipo con el que trabaja el controlador
     *         conreto, recuperando todos sus datos a traves de un
     *         servidor remoto.
     * 
     * @throws DocumentNotFoundException
     *         Si ningun servidor remoto contiene el documento que se
     *         desea recuperar.
     */
    protected D getRemote(final String uuid) throws DocumentNotFoundException
    {
        final Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        for (final DocumentService service : services.values()) {
            try {

                final List<String> uuids = listRemote(service);

                if (uuids.contains(uuid))
                        return getRemote(service, uuid);

            } catch (final DocumentNotFoundException _) {
                // no se hace nada, puesto que se ha comprobado que no
                // se lance con el "uuids.contains(uuid)", y si por
                // algun motivo se lanza, se deja que continue con el
                // siguiente servidor del listado
            } catch (final ServerErrorException see) {
                System.err.println("Remote server error: " + see.getMessage());
            }
        }

        throw new DocumentNotFoundException(uuid);
    }

    /**
     * Elimina un documento, dado su identificador UUID, de todos los
     * servidores remotos que lo contengan.
     * 
     * @param uuid
     *        El identificador UUID del documento a eliminar.
     * 
     * @throws DocumentNotFoundException
     *         Si ningun servidor remoto contiene el documento que se
     *         desea eliminar.
     */
    private void deleteRemote(final String uuid)
        throws DocumentNotFoundException
    {
        final Map<String, DocumentService> services =
            WSUtils.getDocumentServices();

        boolean atLeastOnce = false;
        for (final DocumentService service : services.values()) {
            try {

                final List<String> uuids = listRemote(service);

                if (uuids.contains(uuid)) {
                    atLeastOnce = true;
                    deleteRemote(service, uuid);
                }

            } catch (final DocumentNotFoundException _) {
                // no se hace nada, puesto que se ha comprobado que no
                // se lance con el "uuids.contains(uuid)", y si por
                // algun motivo se lanza, se deja que continue con el
                // siguiente servidor del listado
            } catch (final ServerErrorException see) {
                System.err.println("Remote server error: " + see.getMessage());
            }
        }

        if (!atLeastOnce) throw new DocumentNotFoundException(uuid);
    }

}

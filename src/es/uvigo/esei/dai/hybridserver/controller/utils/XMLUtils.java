package es.uvigo.esei.dai.hybridserver.controller.utils;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Clase de utilidades para el trabajo con documentos XML.
 * 
 * @author Jesus Garcia Limon (jglimon@esei.uvigo.es)
 * @author Alberto Gutierrez Jacome (agjacome@esei.uvigo.es)
 * @author Pablo Vazquez Fernandez (pvfernandez@esei.uvigo.es)
 */
public class XMLUtils
{

    /**
     * Valida que un documento XML sea correcto con respecto a un XSD,
     * ambos dos recibidos como objetos {@link Source}. Si es invalido
     * se lanzara una excepcion, sino no devolvera nada.
     * 
     * @param xml
     *        Source asociado al documento XML a validar.
     * @param xsd
     *        Source asociado al documento XSD que servira para
     *        validar al XML.
     * 
     * @throws IOException
     *         Si se produce algun error de Entrada/Salida durante la
     *         validacion.
     * @throws SAXException
     *         Si el documento no se ha validado correctamente.
     */
    public static void validate(final Source xml, final Source xsd)
        throws IOException, SAXException
    {
        final SchemaFactory schemaFactory =
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final Schema schema = schemaFactory.newSchema(xsd);

        schema.newValidator().validate(xml);
    }

    /**
     * Transforma un documento XML a traves de otro documento XSLT
     * ambos recibidos como objetos {@link Source}. La transformacion
     * se devolvera en un objeto {@link Result} recibido como
     * parametro.
     * 
     * @param xml
     *        Source asociado al documento XML a transformar.
     * @param xslt
     *        Soruce asociado al documento XSLT que servira para
     *        transformar al XML.
     * @param res
     *        Result donde almacenar el resultado de la
     *        transformacion.
     * 
     * @throws TransformerException
     *         Si se produce algun tipo de error durante la
     *         transformacion del documento XML.
     */
    public static void transform(final Source xml, final Source xslt, Result res)
        throws TransformerException
    {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer    = factory.newTransformer(xslt);

        transformer.transform(xml, res);
    }

}

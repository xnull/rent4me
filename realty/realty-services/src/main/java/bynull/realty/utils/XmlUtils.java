package bynull.realty.utils;

import bynull.realty.services.metro.SvgMetro;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Класс для работы с xml.
 */
public final class XmlUtils {

    private XmlUtils(){
        throw new IllegalStateException("Can't instantiate an instance");
    }

    /**
     * Отобразить xml документ на класс
     *
     * @param <T>
     *            класс на который будет отображен xml документ
     * @param xmlDoc
     *            документ источник, который будет отображен на класс
     * @param unmarshallClass
     *            класс на который будет отображен xml документ
     * @return <T> новый класс на который отобразился xml документ
     * @throws XmlParseException
     */
    public static <T> T xmlDocumentToClass(Document xmlDoc, Class<T> unmarshallClass)
            throws XmlParseException {
        if (xmlDoc == null) {
            return null;
        }

        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(unmarshallClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<T> result = unmarshaller.unmarshal(xmlDoc, unmarshallClass);

            return result.getValue();
        } catch (JAXBException e) {
            throw new XmlParseException(e);
        }
    }
    /**
     * Отображает класс на xml документ.
     * @param jaxbClass объект.
     * @return документ.
     * @throws Throwable
     */
    public static Document classToXmlDocument(Object jaxbClass) throws XmlParseException {
        XmlUtils xmlUtils = new XmlUtils();
        Document resultXmlDoc = null;
        try {
            resultXmlDoc = xmlUtils.createDocument();
            JAXBContext context = JAXBContext.newInstance(jaxbClass.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jaxbClass, resultXmlDoc);
            return resultXmlDoc;
        } catch (Exception e) {
            throw new XmlParseException(e);
        }
    }

    /**
     * Получение из JAXB класса, строкового представления в виде xml строки
     * @param jaxbClass
     * @return xml-строка.
     */
    public static String classToXmlString(Object jaxbClass) {
        StringWriter xmlString = new StringWriter();
        JAXB.marshal(jaxbClass, xmlString);
        return xmlString.toString();
    }

    /**
     * Преобразование строки в xml документ.
     *
     * @param xmlDoc строковое представление xml документа.
     * @return
     */
    public static Document stringToDocument(String xmlDoc) throws XmlParseException {
        if (xmlDoc == null) {
            return null;
        }
        try {
            return createDocumentBuilder().parse(
                    new InputSource(new StringReader(xmlDoc)));
        } catch (Exception e) {
            throw new XmlParseException(e);
        }
    }

    /**
     * Преобразование xml документа в строковое представление
     *
     * @param xmlDoc xml документ, который необходимо преобразовать в строку.
     * @return
     */
    public static String documentToString(Document xmlDoc) throws XmlParseException {
        if (xmlDoc == null) {
            return null;
        }

        Transformer transformer = null;
        try {
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(new DOMSource(xmlDoc), result);
            transformer = TransformerFactory.newInstance().newTransformer();
            return ((StringWriter) result.getWriter()).toString();
        } catch (Exception e) {
            throw new XmlParseException(e);
        }
    }

    /**
     * Создание пустого xml документа.
     *
     * @return пустой документ.
     * @throws ParserConfigurationException
     */
    public static Document createDocument() throws XmlParseException {
        return createDocumentBuilder().newDocument();
    }

    /**
     * Создание билдера документа.
     *
     * @return DocumentBuilder.
     * @throws ParserConfigurationException
     */
    private static DocumentBuilder createDocumentBuilder() throws XmlParseException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlParseException(e);
        }
        return builder;
    }

    public static Document load(String url) throws XmlParseException {
        try {
            return createDocumentBuilder().parse(url);
        } catch (Exception e) {
            throw new XmlParseException(e);
        }
    }

    public static <T> T stringToObject(String xmlDoc, Class<T> clazz) throws XmlParseException {
        return xmlDocumentToClass(stringToDocument(xmlDoc), clazz);
    }
}

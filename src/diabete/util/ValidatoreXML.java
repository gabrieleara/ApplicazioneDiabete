/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class ValidatoreXML {
    /* TODO: va bene che non ritorna mai false? */
    public static boolean validaXML(String fileXML, String schemaXML) throws SAXException, IOException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Document d = db.parse(new File(fileXML));
            Schema s = sf.newSchema(new StreamSource(new File(schemaXML)));
            s.newValidator().validate(new DOMSource(d));
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex.getMessage());
        }
        return true;
    }
}

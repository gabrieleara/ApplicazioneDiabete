/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import diabete.configurazione.Configurazione;
import com.thoughtworks.xstream.XStream;
import diabete.dati.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class LettoreFileXML {
    private LettoreFileXML() {
    }
	
	private static void validaXML(String fileXML, String schemaXML) throws SAXException, IOException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Document d = db.parse(new File(fileXML));
            Schema s = sf.newSchema(new StreamSource(new File(schemaXML)));
            s.newValidator().validate(new DOMSource(d));
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    
    public static RaccoltaDatiDiabetici leggiFileDatiGlicemici(String nomefile) throws SAXException, IOException {
        validaXML(nomefile, "rilevazioni.xsd");
		
		File xmlFile = new File(nomefile);
		return (RaccoltaDatiDiabetici) new XStream().fromXML(xmlFile);
    }
	
	public static Configurazione leggiFileConfigurazione(String nomefile) throws SAXException, IOException {
		validaXML(nomefile, "configurazione.xsd");
		
		File xmlFile = new File(nomefile);
		return (Configurazione) new XStream().fromXML(xmlFile);
	}
}

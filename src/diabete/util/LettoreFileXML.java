package diabete.util;

import diabete.configurazione.*;
import com.thoughtworks.xstream.XStream;
import diabete.dati.*;
import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class LettoreFileXML {

    static void validaStringaXML(String stringaXML, String schemaXML) throws SAXException, IOException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            InputSource inputXML = new InputSource(new StringReader(stringaXML));
            Document d = db.parse(inputXML);
            Schema s = sf.newSchema(new StreamSource(new File(schemaXML)));
            s.newValidator().validate(new DOMSource(d));
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    private LettoreFileXML() {
    }
    
    public static void validaXML(String fileXML, String schemaXML) throws SAXException, IOException {
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
        
        XStream stream = new XStream();
        
        stream.alias("raccolta-dati-diabetici", RaccoltaDatiDiabetici.class);
        stream.alias("glicemia-rilevata", GlicemiaRilevata.class);
        stream.alias("iniezione-insulina", IniezioneInsulina.class);
        
        return (RaccoltaDatiDiabetici) stream.fromXML(xmlFile);
    }
    
    public static Configurazione leggiFileConfigurazione(String nomefile) throws SAXException, IOException {
        validaXML(nomefile, "configurazione.xsd");
        
        File xmlFile = new File(nomefile);
        XStream stream = new XStream();
        
        stream.useAttributeFor(ParametriStilistici.class, "percorsoCss");
        stream.alias("configurazione-diabete", Configurazione.class);
        return (Configurazione) stream.fromXML(xmlFile);
    }
}

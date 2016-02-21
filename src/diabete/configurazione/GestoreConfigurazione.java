package diabete.configurazione;

import java.io.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class GestoreConfigurazione {
    
    private static Configurazione configurazione;
    private static final String percorso = "configurazione.xml";
    
    private GestoreConfigurazione() {
    }
    
    public static void init() throws SAXException, IOException {
        try {
            configurazione = diabete.util.LettoreFileXML.leggiFileConfigurazione(percorso);
        } catch (SAXException | IOException ex) {
            configurazione = new Configurazione();
            throw ex;
        }
    }
    
    public static Object ottieniParametro(TipoParametro tipo) {
        return configurazione.ottieniParametro(tipo);
    }
    
}

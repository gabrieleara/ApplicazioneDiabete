/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.configurazione;

import com.thoughtworks.xstream.XStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class GestoreConfigurazione {
    
    private static Configurazione istanza;
    private static final String percorso = "configurazione.xml";
    
    private GestoreConfigurazione() {
    }
    
    public static void init() throws SAXException, IOException {
        try {
            istanza = diabete.util.LettoreFileXML.leggiFileConfigurazione(percorso);
        } catch (SAXException | IOException ex) {
            istanza = new Configurazione();
            throw ex;
        }
    }
    
    public static Object ottieniParametro(TipoParametro tipo) {
        return istanza.ottieniParametro(tipo);
    }
    
    public static void main(String[] args) {
        /* Testing della struttura del file di Configurazione */
        
        Configurazione c = new Configurazione();
        
        try (
                FileOutputStream fos = new FileOutputStream("configurazione.xml");
                DataOutputStream dos = new DataOutputStream(fos);
        ) {
            dos.writeUTF(new XStream().toXML(c));
        } catch (IOException ex) {
            Logger.getLogger(GestoreConfigurazione.class.getName()).log(Level.SEVERE, null, ex);
            // return;
        }
    }
    
}

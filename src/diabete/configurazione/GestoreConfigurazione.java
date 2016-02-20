/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.configurazione;

import com.thoughtworks.xstream.XStream;
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
    
    public static void main(String[] args) {
        /* Testing della struttura del file di Configurazione */
        
        Configurazione c = new Configurazione();
        
        try (
                FileOutputStream fos = new FileOutputStream("configurazione.xml");
                DataOutputStream dos = new DataOutputStream(fos);
        ) {
            dos.writeUTF(new XStream().toXML(c));
        } catch (IOException ex) {
            
        }
    }
    
}

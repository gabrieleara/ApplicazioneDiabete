package diabete.util;

import diabete.configurazione.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.*;

/**
 *
 * @author Gabriele Ara
 */
public class ServerLoggingDiabete {
    
    public static void appendiSuFile(String nomeFile, String azione) {
        try(
            FileOutputStream fos = new FileOutputStream(nomeFile, true);
            PrintWriter pw = new PrintWriter(fos);
        ) {
            pw.write(azione);
            pw.write('\n');
        } catch (IOException ex) {
            System.out.println("Errore in scrittura su file in append.");
        }
    }
    
    public static void main(String[] args) {
        try {
            GestoreConfigurazione.init();
        } catch (SAXException | IOException ex) {
            System.out.println("Errore nella lettura del file di configurazione!");
            return;
        }
        
        int porta = (int) GestoreConfigurazione.ottieniParametro(TipoParametro.PORTA_SERVER);
        
        System.out.println("Dalla configurazione il numero di porta risulta: " + porta);
        
        try( ServerSocket servs = new ServerSocket(porta); ) {
            System.out.println("Socket di ascolto aperto con successo!");
            
            while(true) {
                try(
                    Socket s = servs.accept();
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                ) {
                    String xml = dis.readUTF();
                    
                    LettoreFileXML.validaStringaXML(xml, "log.xsd");
                    
                    System.out.println("Scrittura di:");
                    System.out.println(xml);
                    
                    appendiSuFile("diabetelog.xml", xml);
                    
                } catch (IOException ex) {
                    System.out.println("Errore in ricezione dei dati!");
                } catch (SAXException ex) {
                    System.out.println("Errore in parsing dei dati!");
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore in apertura del socket di ascolto!");
        }
    }
    
}

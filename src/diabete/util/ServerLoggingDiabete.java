package diabete.util;

import com.thoughtworks.xstream.XStream;
import diabete.configurazione.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.xml.sax.*;

/**
 *
 * @author Gabriele Ara
 */
public class ServerLoggingDiabete {
    
    public static class AzioneUtente {
        public final String ipclient;
        public final Date timestamp;
        public final String azione;

        public AzioneUtente(String ipclient, Date timestamp, String azione) {
            this.ipclient = ipclient;
            this.timestamp = timestamp;
            this.azione = azione;
        }
        
        @Override
        public String toString() {
            return new XStream().toXML(this);
        }
    }
    
    public static void appendiSuFile(String nomeFile, AzioneUtente azione) {
        try(
            FileOutputStream fos = new FileOutputStream(nomeFile, true);
            PrintWriter pw = new PrintWriter(fos);
        ) {
            pw.write(azione.toString());
            pw.write('\n');
        } catch (IOException ex) {
            System.out.println("Errore in scrittura su file in append.");
        }
    }
    
    public static void main(String[] args) {
        AzioneUtente azione;
        
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
                    azione =
                            new AzioneUtente(
                                    s.getInetAddress().getHostAddress(),
                                    new Date(),
                                    dis.readUTF());
                    System.out.println("Scrittura di " + azione);
                    
                    appendiSuFile("diabetelog.xml", azione);
                    
                } catch (IOException ex) {
                    System.out.println("Errore in ricezione dei dati!");
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore in apertura del socket di ascolto!");
        }
    }
    
}

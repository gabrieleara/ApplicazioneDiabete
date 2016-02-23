package diabete.util;

import diabete.configurazione.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Gabriele Ara
 */
public class LoggerAzioniUtente {
    
    public static void log(String azione) throws IOException {
        String ipServer =
                (String) GestoreConfigurazione.ottieniParametro(
                        TipoParametro.IP_SERVER);
        int portaServer =
                (int) GestoreConfigurazione.ottieniParametro(
                        TipoParametro.PORTA_SERVER);
        
        try(
            Socket s = new Socket(ipServer, portaServer);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ) {
            AzioneUtente dati = new AzioneUtente(
                    s.getLocalAddress().getHostAddress(),
                    new Date(),
                    azione);
            dos.writeUTF(dati.toString());
        }
        
    }
    
}

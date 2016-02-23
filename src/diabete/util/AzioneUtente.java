/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import com.thoughtworks.xstream.XStream;
import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class AzioneUtente {
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
        XStream stream = new XStream();
        stream.alias("azione-utente", AzioneUtente.class);
        return stream.toXML(this);
    }
}
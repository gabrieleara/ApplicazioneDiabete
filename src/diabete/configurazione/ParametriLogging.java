/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.configurazione;

/**
 *
 * @author Gabriele Ara
 */
public class ParametriLogging {
    public final String ipServer;
    public final String ipLocale;
    public final String portaServer;

    public ParametriLogging(String ipServer, String ipLocale, String portaServer) {
        this.ipServer = ipServer;
        this.ipLocale = ipLocale;
        this.portaServer = portaServer;
    }

    public ParametriLogging() {
        this.ipServer = "127.0.0.1";
        this.ipLocale = "127.0.0.1";
        this.portaServer = "13994";
    }
    
    
}

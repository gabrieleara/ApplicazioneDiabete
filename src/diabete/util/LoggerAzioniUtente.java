/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import diabete.configurazione.*;
import java.io.*;
import java.net.*;

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
			dos.writeUTF(azione);
		}
		
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.dati;

import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class GlicemiaRilevata implements java.io.Serializable {
	
	public final Date timestamp;
	public final int valore;

	public GlicemiaRilevata(Date timestamp, int valore) {
            this.timestamp = timestamp;
            this.valore = valore;
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class DBDiabete {
	
	private DBDiabete() {
	}

	/* @TODO */
	static Date getUltimaData(String utente) {
		return Calendar.getInstance().getTime();
	}
	
}

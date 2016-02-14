/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.GlicemiaRilevata;
import diabete.dati.TipoStatistica;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class Cache implements java.io.Serializable {
	private final ArrayList<GlicemiaRilevata> datiDelGrafo;
	private final int[] statistiche;
	private final ArrayList<String> utenti;
	private final String utenteAttuale;
	private final Date dataAttuale;
	
	private static Cache valoreIniziale = null;

	public Cache(ArrayList<GlicemiaRilevata> datiDelGrafo,
			ArrayList<String> utenti, String utenteAttuale, Date dataAttuale,
			int[] statistiche) {
		
		this.datiDelGrafo = new ArrayList(datiDelGrafo);
		this.statistiche = new int[TipoStatistica.NUMERO_TIPI_STATISTICHE];
		
		this.utenti = new ArrayList(utenti);
		this.utenteAttuale = utenteAttuale;
		this.dataAttuale = dataAttuale;
		
		System.arraycopy(statistiche, 0, this.statistiche, 0, statistiche.length);
	}
	
	public Cache(ArrayList<GlicemiaRilevata> datiDelGrafo,
			ArrayList<String> utenti, String utenteAttuale, Date dataAttuale) {
		
		this.datiDelGrafo = new ArrayList(datiDelGrafo);
		this.statistiche = new int[TipoStatistica.NUMERO_TIPI_STATISTICHE];
		
		this.utenti = new ArrayList(utenti);
		this.utenteAttuale = utenteAttuale;
		this.dataAttuale = dataAttuale;
	}
	
	public static void setValoreIniziale(Cache valoreIniziale) {
		assert valoreIniziale != null : "Cache.setValoreIniziale: era nulla!";
		
		Cache.valoreIniziale = valoreIniziale;
	}

	public static Cache getValoreIniziale() {
		assert valoreIniziale != null : "Cache.getValoreInstance: era nulla!";
		
		return valoreIniziale;
	}

	public ArrayList<GlicemiaRilevata> getDatiDelGrafo() {
		return datiDelGrafo;
	}

	public ArrayList<String> getUtenti() {
		return utenti;
	}

	public String getUtenteAttuale() {
		return utenteAttuale;
	}

	public Date getDataAttuale() {
		return dataAttuale;
	}
	
	public int getStatistica(TipoStatistica tipo) {
		switch(tipo) {
		case GLUCOSIO_MEDIO:
			return statistiche[0];
		case GLUCOSIO_SOPRA_INTERVALLO:
			return statistiche[1];
		case GLUCOSIO_SOTTO_INTERVALLO:
			return statistiche[2];
		case INSULINA_LENTA:
			return statistiche[3];
		case INSULINA_RAPIDA:
			return statistiche[4];
		case EVENTI_GLUCOSIO_BASSO:
			return statistiche[5];
		case DURATA_EVENTI_GLUCOSIO_BASSO:
			return statistiche[6];
		}
		
		assert false : "Cache.getStatistica: non deve mai arrivare qui!";
		return 0;
	}
	
	public void setStatistica(int valore, TipoStatistica tipo) {
		switch(tipo) {
		case GLUCOSIO_MEDIO:
			statistiche[0] = valore;
			break;
		case GLUCOSIO_SOPRA_INTERVALLO:
			statistiche[1] = valore;
			break;
		case GLUCOSIO_SOTTO_INTERVALLO:
			statistiche[2] = valore;
			break;
		case INSULINA_LENTA:
			statistiche[3] = valore;
			break;
		case INSULINA_RAPIDA:
			statistiche[4] = valore;
			break;
		case EVENTI_GLUCOSIO_BASSO:
			statistiche[5] = valore;
			break;
		case DURATA_EVENTI_GLUCOSIO_BASSO:
			statistiche[6] = valore;
			break;
		}
	}
	
	public static void main(String args[]) {
		/* Prova della serializzazione / recuper dati */
		ArrayList<GlicemiaRilevata> datiDelGrafo = new ArrayList<>();
		
		Calendar c = Calendar.getInstance();
		
		c.set(2016, 01, 05, 00, 00, 00);
		int numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 01, 20, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 10, 40, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 15, 50, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 19, 15, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 22, 00, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		c.set(2016, 01, 05, 23, 59, 00);
		numero = (int) Math.floor( Math.random() * 400);
		datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));
		
		ArrayList<String> utenti = new ArrayList();
		utenti.add("Annalisa Gioli");
		utenti.add("Eva Lupo");
		
		
		Cache cache = new Cache(datiDelGrafo, utenti, "Annalisa Gioli",
				c.getTime());
		
		cache.setStatistica(132, TipoStatistica.GLUCOSIO_MEDIO);
		cache.setStatistica(26, TipoStatistica.GLUCOSIO_SOPRA_INTERVALLO);
		cache.setStatistica(15, TipoStatistica.GLUCOSIO_SOTTO_INTERVALLO);
		cache.setStatistica(1, TipoStatistica.INSULINA_LENTA);
		cache.setStatistica(11, TipoStatistica.INSULINA_RAPIDA);
		cache.setStatistica(11, TipoStatistica.EVENTI_GLUCOSIO_BASSO);
		cache.setStatistica(124, TipoStatistica.DURATA_EVENTI_GLUCOSIO_BASSO);
		
		try(
				java.io.FileOutputStream fos = new java.io.FileOutputStream("cache.bin");
				java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
			) {
			oos.writeObject(cache);
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Scritto tutto!");
	}
}

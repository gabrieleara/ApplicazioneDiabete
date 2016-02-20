/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.GlicemiaRilevata;
import diabete.dati.TipoStatistica;
import java.io.*;
import java.util.*;

/**
 *
 * @author Gabriele Ara
 */

public class Cache implements java.io.Serializable {
    private final ArrayList<GlicemiaRilevata> datiPerGrafico;
    private final int[] statistiche;
    private final ArrayList<String> pazienti;
    private final String pazienteAttuale;
    private final Date dataAttuale;
	
	private static final String cacheFileName = "cache.bin";

    public static Cache leggiCache() throws IOException, ClassNotFoundException {
		Cache c;
        try(
            FileInputStream fis = new FileInputStream(cacheFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            c = (Cache) ois.readObject();
        }
		return c;
    }

    public static void scriviCache() throws IOException {
		Cache c = new Cache(StatoApplicazione.getInstance());
        try (
            FileOutputStream fos = new FileOutputStream(cacheFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
           oos.writeObject(c);
		}
    }
	
	private Cache(StatoApplicazione stato) {
		this.datiPerGrafico = new ArrayList<>(stato.getDatiPerGrafico());
		
		this.pazienti = new ArrayList<>(stato.getPazienti());
		
		this.pazienteAttuale = stato.getPazienteAttuale().get();
		
		this.dataAttuale = stato.getDataAttuale().get();
		
		this.statistiche = new int[TipoStatistica.NUMERO_TIPI_STATISTICHE];
		
		for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
			this.statistiche[i] = stato.getStatistiche()[i].get();
	}

    public ArrayList<GlicemiaRilevata> getDatiPerGrafico() {
        return datiPerGrafico;
    }

    public ArrayList<String> getPazienti() {
        return pazienti;
    }

    public String getPazienteAttuale() {
        return pazienteAttuale;
    }

    public Date getDataAttuale() {
        return dataAttuale;
    }
	
	public int[] getStatistiche() {
		return statistiche;
	}

    public int getStatistica(TipoStatistica tipo) {
        return statistiche[tipo.valore];
    }
	
	public static void main(String args[]) {
        // Prova della serializzazione / recupero dati 
        
        ArrayList<GlicemiaRilevata> datiDelGrafo = new ArrayList<>();

        Calendar c = Calendar.getInstance();

        c.set(2016, 01, 16, 00, 00, 00);
        int numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 01, 20, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 10, 40, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 15, 50, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 19, 15, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 22, 00, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

        c.set(2016, 01, 16, 23, 59, 00);
        numero = (int) Math.floor( Math.random() * 400);
        datiDelGrafo.add(new GlicemiaRilevata(c.getTime(), numero));

		StatoApplicazione.init();
		StatoApplicazione stato = StatoApplicazione.getInstance();
		
		String paziente = new String("Eva Lupo");
		stato.addPaziente(paziente);
		paziente = new String("Annalisa Gioli");
		stato.addPaziente(paziente);
		
		stato.setDataAttuale(new Date());
		
		stato.setDatiPerGrafico(datiDelGrafo);
		
		try {
			scriviCache();
			
			System.out.println("Scritto tutto!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    
    }
    
}

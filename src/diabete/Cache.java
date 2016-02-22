/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.*;

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
        // 01
        Cache c = new Cache(StatoApplicazione.getInstance());
        try (
            FileOutputStream fos = new FileOutputStream(cacheFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
           oos.writeObject(c);
        }
    }
    
    private Cache(StatoApplicazione stato) {
        this.datiPerGrafico = new ArrayList<>(stato.glicemieRilevateProperty());
        
        this.pazienti = new ArrayList<>(stato.pazientiProperty());
        
        this.pazienteAttuale = stato.pazienteAttualeProperty().get();
        
        this.dataAttuale = stato.dataAttualeProperty().get();
        
        this.statistiche = new int[TipoStatistica.NUMERO_TIPI_STATISTICHE];
        
        for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
            this.statistiche[i] = stato.statisticheProperty()[i].get();
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
    
}

/*
    COMMENTI AL CODICE
    
    01) Costruisce un oggetto cache a partire dallo stato attuale e lo scrive
        su file.
*/

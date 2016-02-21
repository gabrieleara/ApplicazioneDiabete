package diabete;

import diabete.configurazione.*;
import diabete.dati.*;
import diabete.util.CalendarioSettimanale;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriele Ara
 */
public class AnalizzatoreDatiDiabetici {
    private static int SOGLIA_GLICEMIA_BASSA = 0;
	private static int SOGLIA_GLICEMIA_ALTA = 0;
	
	public static final int INDEX_GLUCOSIO_MEDIO = 0;
	public static final int INDEX_GLUCOSIO_SOPRA = 1;
	public static final int INDEX_GLUCOSIO_SOTTO = 2;
	
	public static final int INDEX_NUMERO_EVENTI = 0;
	public static final int INDEX_DURATA_EVENTI = 1;
    
    private AnalizzatoreDatiDiabetici() {
		
	}
    
	public static int[] produciStatisticheGlicemiche(ArrayList<GlicemiaRilevata> gc) {
        // 01
		SOGLIA_GLICEMIA_BASSA = (int) GestoreConfigurazione
                .ottieniParametro(TipoParametro.SOGLIA_GLICEMIA_ALTA);
		SOGLIA_GLICEMIA_ALTA = (int) GestoreConfigurazione
                .ottieniParametro(TipoParametro.SOGLIA_GLICEMIA_BASSA);
        
		int[] glucosioM = analizzaGlucosioMedio(gc);
		int[] glucosioB = analizzaEventiGlucosioBasso(gc);
		
		int[] statistiche = new int[TipoStatistica.NUMERO_TIPI_STATISTICHE];
		
		statistiche[TipoStatistica.GLUCOSIO_MEDIO.valore] = glucosioM[INDEX_GLUCOSIO_MEDIO];
		statistiche[TipoStatistica.GLUCOSIO_SOPRA_INTERVALLO.valore] = glucosioM[INDEX_GLUCOSIO_SOPRA];
		statistiche[TipoStatistica.GLUCOSIO_SOTTO_INTERVALLO.valore] = glucosioM[INDEX_GLUCOSIO_SOTTO];
		statistiche[TipoStatistica.EVENTI_GLUCOSIO_BASSO.valore] = glucosioB[INDEX_NUMERO_EVENTI];
		statistiche[TipoStatistica.DURATA_EVENTI_GLUCOSIO_BASSO.valore] = glucosioB[INDEX_DURATA_EVENTI];
		
		return statistiche;
	}
    
	private static int[] analizzaGlucosioMedio(Collection<GlicemiaRilevata> glicemia) {
		int media = 0, sopra = 0, sotto = 0, quanti = 0;
		
		for(GlicemiaRilevata gr : glicemia) {
			quanti++;
			media += gr.valore;
			
			if(gr.valore > SOGLIA_GLICEMIA_BASSA)
				sopra++;
			if(gr.valore < SOGLIA_GLICEMIA_ALTA)
				sotto++;
		}
		
		if(quanti != 0){
			media = media / quanti;
			sopra = (sopra * 100) / quanti;
			sotto = (sotto * 100) / quanti;
		}
		
		int[] statistiche = new int[3];
		
		statistiche[INDEX_GLUCOSIO_MEDIO] = media;
		statistiche[INDEX_GLUCOSIO_SOPRA] = sopra;
		statistiche[INDEX_GLUCOSIO_SOTTO] = sotto;
		
		return statistiche;
	}
	
    // 02
	private static class ComparatoreTemporale implements Comparator<GlicemiaRilevata> {
		@Override
		public int compare(GlicemiaRilevata o1, GlicemiaRilevata o2) {
			if(o1.timestamp.equals(o2.timestamp))
				return 0;
			return o1.timestamp.before(o2.timestamp) ? -1 : 1;
		}
		
	}
	
	private static int[] analizzaEventiGlucosioBasso(Collection<GlicemiaRilevata> glicemia) {
		List<GlicemiaRilevata> lista = new ArrayList<>(glicemia);
		int[] statistiche = new int[2];
		
		if(lista.size() < 1)
			return statistiche;
		
        // 03
		lista.sort(new ComparatoreTemporale());
		
		int eventi = 0;
		long durata = 0;
		
		boolean bassa = false;
		
		Date inizio = lista.get(0).timestamp;
		Date fine = inizio;
		
		for(int i = 0; i < lista.size(); ++i) {
            // 04
			if(bassa) {
				if(lista.get(i).valore < SOGLIA_GLICEMIA_ALTA) {
					fine = lista.get(i).timestamp;
					continue;
				}
				
                // 05
				durata += fine.getTime() - inizio.getTime();
				bassa = false;
			}
			
			inizio = lista.get(i).timestamp;
			fine = inizio;
			
			if(lista.get(i).valore < SOGLIA_GLICEMIA_ALTA) {
				bassa = true;
				eventi++;
			}
		}
		
        // 05.1
		if(bassa)
			durata += fine.getTime() - inizio.getTime();
		
		int media = 0;
		if(eventi != 0)
			media = (int) ((durata / (long) eventi) / 60000);
		
		statistiche[INDEX_NUMERO_EVENTI] = eventi;
		statistiche[INDEX_DURATA_EVENTI] = media;
		
		return statistiche;
	}
	
	public static Collection<GlicemiaRilevata> analizzaGlicemiaMediaOraria(Collection<GlicemiaRilevata> glicemia) {
		List<GlicemiaRilevata> lista = new ArrayList<>(glicemia);
		
		if(lista.size() < 1)
			return lista;
		
		CalendarioSettimanale cs = new CalendarioSettimanale();
		cs.setTime(lista.get(0).timestamp);
		
		cs.resetTempoDelGiorno();
		CalendarioSettimanale attuale = new CalendarioSettimanale();
		
		ArrayList<GlicemiaRilevata> media = new ArrayList<>();
		
        // 06
		do {
            // 07
			List<GlicemiaRilevata> buoni = lista.stream()
					.filter((GlicemiaRilevata gc) -> {
						attuale.setTime(gc.timestamp);
                        
                        // 08
						return
                                attuale.get(Calendar.HOUR_OF_DAY) == cs.get(Calendar.HOUR_OF_DAY)
								&& attuale.get(Calendar.MINUTE) >= cs.get(Calendar.MINUTE)
                                && attuale.get(Calendar.MINUTE) <= cs.get(Calendar.MINUTE) + 14;
					}).collect(Collectors.toList());
			
			int sum = 0;
			
            // 09
			for(GlicemiaRilevata gr : buoni) {
				sum+=gr.valore;
			}
			
            // 10
			if(!buoni.isEmpty()) {
				media.add(new GlicemiaRilevata(cs.getTime(), sum/buoni.size()));
			}
			
			cs.add(Calendar.MINUTE, 15);
		} while(cs.get(Calendar.HOUR_OF_DAY) != 0 || cs.get(Calendar.MINUTE) !=0);
		
		return media;
	}
	
}

/*
    COMMENTI AL CODICE

    01) Recupero dati da configurazione.

    02) Necessario per eseguire l'ordinamento di Glicemie Rilevate.

    03) Ordinare le rilevazioni permette di individuare gli intervalli nei
        quali il glucosio si è mantenuto sotto la soglia indicata con facilità,
        scorrendo semplicemente avanti la lista.

    04) Uno o più valori precedenti erano inferiori alla soglia.

    05) Di nuovo sopra la soglia, l'intervallo è concluso all'iterazione
        precedente e i valori salvati sono quelli giusti.

        05.1)   L'ultimo evento ha superato la mezzanotte della domenica: per
                semplicità assumo che sia terminato contestualmente.

    06) Esegue una scansione di tutte le ore del giorno per produrre una media
        dei valori di glicemia registrati, a intervalli di 15 min.

    07) Filtra i valori contenuti nella lista secondo il criterio fornito
        dalla lambda expression sottostante.

    08) Il criterio è temporale: rispettano il criterio tutte le rilevazioni
        compiute in un dati intervallo di 15 min all'interno del giorno.

    09) Calcola la media dei valori ottenuti dal filtraggio precedente. O
        meglio somma tutti i valori per poi aggiungere in seguito la media.

    10) Aggiunge il valore calcolato se l'insieme ottenuto è non vuoto.
*/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.configurazione.GestoreConfigurazione;
import diabete.configurazione.TipoParametro;
import diabete.dati.GlicemiaRilevata;
import diabete.dati.TipoStatistica;
import diabete.util.CalendarioSettimanale;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriele Ara
 */
public class AnalizzatoreDiabetico {

	public static int[] analizza(ArrayList<GlicemiaRilevata> gc) {
		SOGLIA_GLICEMIA_BASSA = (int) GestoreConfigurazione.ottieniParametro(TipoParametro.SOGLIA_GLICEMIA_ALTA);
		SOGLIA_GLICEMIA_ALTA = (int) GestoreConfigurazione.ottieniParametro(TipoParametro.SOGLIA_GLICEMIA_BASSA);
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
	
	private AnalizzatoreDiabetico() {
		
	}
	
	private static int SOGLIA_GLICEMIA_BASSA = 0;
	private static int SOGLIA_GLICEMIA_ALTA = 0;
	
	public static final int INDEX_GLUCOSIO_MEDIO = 0;
	public static final int INDEX_GLUCOSIO_SOPRA = 1;
	public static final int INDEX_GLUCOSIO_SOTTO = 2;
	
	public static final int INDEX_NUMERO_EVENTI = 0;
	public static final int INDEX_DURATA_EVENTI = 1;
	
	
	
	private static final int[] analizzaGlucosioMedio(Collection<GlicemiaRilevata> glicemia) {
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
	
	private static class ComparatoreTemporale implements Comparator<GlicemiaRilevata> {
		@Override
		public int compare(GlicemiaRilevata o1, GlicemiaRilevata o2) {
			if(o1.timestamp.equals(o2.timestamp))
				return 0;
			return o1.timestamp.before(o2.timestamp) ? -1 : 1;
		}
		
	}
	
	private static final int[] analizzaEventiGlucosioBasso(Collection<GlicemiaRilevata> glicemia) {
		List<GlicemiaRilevata> lista = new ArrayList<>(glicemia);
		int[] statistiche = new int[2];
		
		if(lista.size() < 1)
			return statistiche;
		
		lista.sort(new ComparatoreTemporale());
		
		int eventi = 0;
		long durata = 0;
		
		boolean bassa = false;
		
		Date start = lista.get(0).timestamp;
		Date end = start;
		
		for(int i = 0; i < lista.size(); ++i) {
			if(bassa) {
				if(lista.get(i).valore < SOGLIA_GLICEMIA_ALTA) {
					end = lista.get(i).timestamp;
					continue;
				}
				/* evento finito */
				durata += end.getTime() - start.getTime();
				bassa = false;
			}
			
			start = lista.get(i).timestamp;
			end = start;
			
			if(lista.get(i).valore < SOGLIA_GLICEMIA_ALTA) {
				bassa = true;
				eventi++;
			}
		}
		
		if(bassa) /* evento finito */
			durata += end.getTime() - start.getTime();
		
		int media = 0;
		if(eventi != 0)
			media = (int) ((durata / (long) eventi) / 60000);
		
		statistiche[INDEX_NUMERO_EVENTI] = eventi;
		statistiche[INDEX_DURATA_EVENTI] = media;
		
		return statistiche;
	}
	
	public static final Collection<GlicemiaRilevata> glicemiaMediaOraria(Collection<GlicemiaRilevata> glicemia) {
		List<GlicemiaRilevata> lista = new ArrayList<>(glicemia);
		
		if(lista.size() < 1)
			return lista;
		
		CalendarioSettimanale cs = new CalendarioSettimanale();
		cs.setTime(lista.get(0).timestamp);
		
		cs.resetTempoDelGiorno();
		CalendarioSettimanale attuale = new CalendarioSettimanale();
		
		ArrayList<GlicemiaRilevata> media = new ArrayList<>();
		
		do {
			List<GlicemiaRilevata> buoni = lista.stream()
					.filter((GlicemiaRilevata gc) -> {
						attuale.setTime(gc.timestamp);
						return
								attuale.get(Calendar.HOUR_OF_DAY) == cs.get(Calendar.HOUR_OF_DAY)
								&& attuale.get(Calendar.MINUTE) == cs.get(Calendar.MINUTE);
					}).collect(Collectors.toList());
			
			// System.out.println("Guardo:\t" + cs.getTime());
			
			int sum = 0, num = 0;
			
			for(GlicemiaRilevata gr : buoni) {
				// System.out.println("\t\t" + gr.timestamp + "\t" + gr.valore);
				++num;
				sum+=gr.valore;
			}
			
			if(num != 0) {
				media.add(new GlicemiaRilevata(cs.getTime(), sum/num));
			}
			
			cs.add(Calendar.MINUTE, 15);
		} while(cs.get(Calendar.HOUR_OF_DAY) != 0 || cs.get(Calendar.MINUTE) !=0);
		
		return media;
	}
	
}

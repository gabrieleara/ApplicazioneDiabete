/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.GlicemiaRilevata;
import diabete.dati.TipoStatistica;
import java.util.*;

/**
 *
 * @author Gabriele Ara
 */
public class AnalizzatoreDiabetico {

	static int[] analizza(ArrayList<GlicemiaRilevata> gc) {
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
	
	private static final int SOGLIA_MAX_MEDIO = 200;
	private static final int SOGLIA_MIN_MEDIO = 70;
	
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
			
			if(gr.valore > SOGLIA_MAX_MEDIO)
				sopra += gr.valore;
			if(gr.valore < SOGLIA_MIN_MEDIO)
				sotto += gr.valore;
		}
		
		if(quanti != 0){
			media = media / quanti;
			sopra = sopra / quanti;
			sotto = sotto / quanti;
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
				if(lista.get(i).valore < SOGLIA_MIN_MEDIO) {
					end = lista.get(i).timestamp;
					continue;
				}
				/* evento finito */
				durata += end.getTime() - start.getTime();
				bassa = false;
			}
			
			start = lista.get(i).timestamp;
			end = start;
			
			if(lista.get(i).valore < SOGLIA_MIN_MEDIO) {
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
	
}

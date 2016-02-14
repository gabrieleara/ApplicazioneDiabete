/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.dati;

/**
 *
 * @author Gabriele Ara
 */
public enum TipoStatistica {
	GLUCOSIO_MEDIO,
	GLUCOSIO_SOPRA_INTERVALLO,
	GLUCOSIO_SOTTO_INTERVALLO,
	INSULINA_LENTA,
	INSULINA_RAPIDA,
	EVENTI_GLUCOSIO_BASSO,
	DURATA_EVENTI_GLUCOSIO_BASSO;
	
	public static int NUMERO_TIPI_STATISTICHE = 8;
}

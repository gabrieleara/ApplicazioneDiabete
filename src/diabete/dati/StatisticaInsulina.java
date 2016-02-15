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
public class StatisticaInsulina {
	public final int unita;
	public final TipoInsulina tipo;

	public StatisticaInsulina(TipoInsulina tipo, int unita) {
		this.tipo = tipo;
		this.unita = unita;
	}
	
}

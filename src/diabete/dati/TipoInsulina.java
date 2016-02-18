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
public enum TipoInsulina {
	INSULINA_RAPIDA(0),
	INSULINA_LENTA(1);

	public int valore;
	
	TipoInsulina(int valore) {
		this.valore = valore;
	}
}
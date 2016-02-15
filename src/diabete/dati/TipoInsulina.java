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
	INSULINA_RAPIDA,
	INSULINA_LENTA;

	public static TipoInsulina fromInt(int tipo) {
		return TipoInsulina.values()[tipo];
	}
}
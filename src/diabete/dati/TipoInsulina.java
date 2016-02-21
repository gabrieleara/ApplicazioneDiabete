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
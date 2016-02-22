package diabete.dati;

/**
 *
 * @author Gabriele Ara
 */
public enum TipoStatistica {
    GLUCOSIO_MEDIO(0),
    GLUCOSIO_SOPRA_INTERVALLO(1),
    GLUCOSIO_SOTTO_INTERVALLO(2),
    INSULINA_LENTA(3),
    INSULINA_RAPIDA(4),
    EVENTI_GLUCOSIO_BASSO(5),
    DURATA_EVENTI_GLUCOSIO_BASSO(6);
    
    public static int NUMERO_TIPI_STATISTICHE = 7;
    
    public final int valore;
    
    TipoStatistica(int v) {
        valore = v;
    }
}

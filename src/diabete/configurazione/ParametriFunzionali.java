package diabete.configurazione;

/**
 *
 * @author Gabriele Ara
 */
public class ParametriFunzionali {
    public final int sogliaGlicemiaAlta;
    public final int sogliaGlicemiaBassa;

    public ParametriFunzionali(int sogliaGlicemiaAlta, int sogliaGlicemiaBassa) {
        this.sogliaGlicemiaAlta = sogliaGlicemiaAlta;
        this.sogliaGlicemiaBassa = sogliaGlicemiaBassa;
    }
    
    public ParametriFunzionali() {
        this.sogliaGlicemiaAlta = 170;
        this.sogliaGlicemiaBassa = 70;
    }
}

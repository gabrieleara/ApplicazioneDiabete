package diabete.configurazione;

/**
 *
 * @author Gabriele Ara
 */
public class ParametriStilistici {
    public final String percorsoCss;
    public final String nomeFont;
    public final String coloreIntestazione;
    public final String colorePannelloUtenti;
    public final String coloreUtenteSelezionato;
    public final String coloreLineaGrafico;
    public final String coloreSfondoGrafico;

    public ParametriStilistici(String percorsoCss, String nomeFont, String coloreIntestazione, String colorePannelloUtenti, String coloreUtenteSelezionato, String coloreLineaGrafico, String coloreSfondoGrafico) {
        this.percorsoCss = percorsoCss;
        this.nomeFont = nomeFont;
        this.coloreIntestazione = coloreIntestazione;
        this.colorePannelloUtenti = colorePannelloUtenti;
        this.coloreUtenteSelezionato = coloreUtenteSelezionato;
        this.coloreLineaGrafico = coloreLineaGrafico;
        this.coloreSfondoGrafico = coloreSfondoGrafico;
    }

    public ParametriStilistici() {
        this.percorsoCss = "res/styles/style.css";
        this.nomeFont = "sans-serif";
        this.coloreIntestazione = "#00BCD4";
        this.colorePannelloUtenti = "#0097A7";
        this.coloreUtenteSelezionato = "#FAFAFA";
        this.coloreLineaGrafico = "#607D8B";
        this.coloreSfondoGrafico = "#F6F6F6";
    }
}

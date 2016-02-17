/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        this.percorsoCss = "stile.css";
        this.nomeFont = "sans-serif";
        this.coloreIntestazione = "#00BCD4";
        this.colorePannelloUtenti = "#0097A7";
        this.coloreUtenteSelezionato = "#727272";
        this.coloreLineaGrafico = "#607D8B";
        this.coloreSfondoGrafico = "#B6B6B6";
    }
}

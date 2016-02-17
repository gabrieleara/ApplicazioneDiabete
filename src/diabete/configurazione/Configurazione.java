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
public class Configurazione {
    
    public final ParametriStilistici stile;
    public final ParametriLogging logging;
    public final ParametriFunzionali soglie;

    public Configurazione(ParametriStilistici stile, ParametriLogging logging, ParametriFunzionali soglie) {
        this.stile = stile;
        this.logging = logging;
        this.soglie = soglie;
    }
    
    public Configurazione() {
        this.stile = new ParametriStilistici();
        this.logging = new ParametriLogging();
        this.soglie = new ParametriFunzionali();
    }
    
    Object ottieniParametro(TipoParametro tipo) {
        switch(tipo) {
            case PERCORSO_CSS:
                    return stile.percorsoCss;
            case NOME_FONT:
                    return stile.nomeFont;
            case COLORE_INTESTAZIONE:
                    return stile.coloreIntestazione;
            case COLORE_PANNELLO_UTENTI:
                    return stile.colorePannelloUtenti;
            case COLORE_UTENTE_SELEZIONATO:
                    return stile.coloreUtenteSelezionato;
            case COLORE_LINEA_GRAFICO:
                    return stile.coloreLineaGrafico;
            case COLORE_SFONDO_GRAFICO:
                    return stile.coloreSfondoGrafico;
            case IP_SERVER:
                    return logging.ipServer;
            case IP_LOCALE:
                    return logging.ipLocale;
            case PORTA_SERVER:
                    return logging.portaServer;
            case SOGLIA_GLICEMIA_BASSA:
                    return soglie.sogliaGlicemiaBassa;
            case SOGLIA_GLICEMIA_ALTA:
                    return soglie.sogliaGlicemiaAlta;
        }
        assert false : "Configurazione: parametro non valido!";
        return 0;
    }
    
}

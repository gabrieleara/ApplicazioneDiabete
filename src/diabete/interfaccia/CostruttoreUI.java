package diabete.interfaccia;

import diabete.configurazione.GestoreConfigurazione;
import diabete.configurazione.TipoParametro;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;

/**
 *
 * @author Gabriele Ara
 */
public class CostruttoreUI {
    private static Pane pannelloPazienti;
    
    private CostruttoreUI() {
    }
    
    public static Label creaEtichetta(String id, String classe) {
        Label label = new Label();
        label.getStyleClass().add(classe);
        
        label.setId(id);
        return label;
    }
    
    public static TextField creaCampoTesto(String id, String classe) {
        TextField tf = new TextField();
        tf.getStyleClass().add(classe);
        tf.setId(id);
        return tf;
    }
    
    public static final Button creaBottoneQuadrato(String id, String testo,
            String classe) {
        Button b = new Button(testo);
        b.getStyleClass().add(classe);
        b.setMinHeight(60.);
        b.setMaxHeight(60.);
        b.setMinWidth(60.);
        b.setMaxWidth(60.);
        b.setId(id);
        return b;
    }
    
    public static RadioButton creaBottonePaziente(String nome, String classe,
            ToggleGroup gruppo) {
        RadioButton rb = new RadioButton(nome);
        rb.getStyleClass().add(classe);
        rb.setWrapText(true);
        rb.setToggleGroup(gruppo);
        
        return rb;
    }
    
    public static void aggiungiPaziente(ToggleButton paziente) {
        paziente.setMaxWidth(180);
        paziente.setMinWidth(180);
        paziente.setMinHeight(40);
        pannelloPazienti.getChildren().add(paziente);
    }
    
    private static Pane costruisciIntestazione(Button pulsante) {
        AnchorPane intestazione = new AnchorPane();
        intestazione.getStyleClass().add("intestazione");
        
        String color = (String) GestoreConfigurazione
                .ottieniParametro(TipoParametro.COLORE_INTESTAZIONE);
        intestazione.setStyle("-fx-background-color: " + color);
        
        intestazione.setMinHeight(50.);
        intestazione.setMaxHeight(50.);
        intestazione.setMinWidth(930);
        
        Label titolo = new Label("Controllo del Glucosio");
        AnchorPane.setTopAnchor(titolo, 0.0);
        AnchorPane.setBottomAnchor(titolo, 0.0);
        AnchorPane.setLeftAnchor(titolo, 10.0);
        AnchorPane.setRightAnchor(titolo, 80.0);
        intestazione.getChildren().add(titolo);
        
        AnchorPane.setTopAnchor(pulsante, 0.0);
        AnchorPane.setBottomAnchor(pulsante, 0.0);
        AnchorPane.setRightAnchor(pulsante, 0.0);
        intestazione.getChildren().add(pulsante);
        
        return intestazione;
    }
    
    private static Pane costruisciPannelloPazienti(ToggleGroup gruppo) {
        VBox pannello = new VBox();
        
        pannello.setMinWidth(180);
        pannello.setMaxWidth(180);
        
        return pannello;
    }
    
    private static Pane costruisciPannelliContenuto(Button indietro,
            Button avanti,
            TextField data,
            Label errore) {
        PannelloGlucosio pg = new PannelloGlucosio();
        pg.setMinHeight(320 - 32);
        pg.setMinWidth(320 - 66);
        pg.getStyleClass().add("pannello-statistica");
        pg.setId("pannello-glucosio");
        
        PannelloInsulina pi = new PannelloInsulina();
        pi.setMinHeight(240 - 32);
        pi.setMinWidth(320 - 66);
        pi.getStyleClass().add("pannello-statistica");
        pi.setId("pannello-insulina");
        
        PannelloGlucosioBasso pgb = new PannelloGlucosioBasso();
        pgb.setMinHeight(120 - 32);
        pgb.setMinWidth(430 - 66);
        pgb.getStyleClass().add("pannello-statistica");
        pgb.setId("pannello-glucosio-basso");
        
        PannelloGraficoGlicemico pgg = new PannelloGraficoGlicemico();
        pgg.setMinHeight(320 - 26);
        pgg.setMaxHeight(320 - 26);
        pgg.setMinWidth(530 - 32);
        pgg.setMaxWidth(530 - 32);
        pgg.setId("pannello-grafico");
        
        HBox comandi = new HBox();
        comandi.setMinHeight(60);
        comandi.setMaxHeight(70);
        comandi.setMinWidth(430);
        comandi.setId("pannello-comandi");

        Label l = new Label("Settimana dal ");
        comandi.getChildren().addAll(indietro, l, data, avanti);
        
        GridPane contenuto = new GridPane();
        
        contenuto.setMinWidth(320 + 430);
        contenuto.setMinHeight(240 + 220);
        
        errore.setMinWidth(480);
        errore.setMaxWidth(480);
        
        GridPane.setConstraints(pg, 0, 0, 1, 1);
        GridPane.setConstraints(pi, 0, 1, 1, 3);
        GridPane.setConstraints(pgb, 1, 1, 1, 1);
        GridPane.setConstraints(pgg, 1, 0, 1, 1);
        GridPane.setConstraints(comandi, 1, 2, 1, 1);
        
        GridPane.setConstraints(errore, 1, 3, 1, 1);
        
        contenuto.getChildren().addAll(pg, pi, pgb, pgg, comandi, errore);
        
        return contenuto;
    }
    
    public static Pane costruisciInterfaccia(
            Button aprifile,
            Button indietro,
            Button avanti, 
            TextField data,
            ToggleGroup pazienti,
            Label errore) {
        final BorderPane contenitore = new BorderPane();
        
        contenitore.setTop(costruisciIntestazione(aprifile));
        pannelloPazienti = costruisciPannelloPazienti(pazienti);
        ScrollPane sp = new ScrollPane();
        sp.setContent(pannelloPazienti);
        
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollBarPolicy.NEVER);
        sp.setMinWidth(180);
        sp.setMaxWidth(180);
        sp.getStyleClass().add("lista-utenti");
        String color = (String) GestoreConfigurazione.ottieniParametro(TipoParametro.COLORE_PANNELLO_UTENTI);
        sp.setStyle("-fx-background-color: " + color);
        
        sp.minHeight(pannelloPazienti.minHeightProperty().get());
        sp.minWidth(pannelloPazienti.minWidthProperty().get());
        sp.maxHeight(pannelloPazienti.maxHeightProperty().get());
        sp.maxWidth(pannelloPazienti.maxWidthProperty().get());

        contenitore.setLeft(sp);
        contenitore.setCenter(costruisciPannelliContenuto(indietro, avanti, data, errore));
        
        return contenitore;
    }
    
}

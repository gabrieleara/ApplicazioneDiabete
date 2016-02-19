/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

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
	
	/*
	 * @TODO: immagine come sfondo della classe
	 */
	public static final Button creaBottoneQuadrato(String id, String testo, String classe) {
		Button b = new Button(testo);
		b.getStyleClass().add(classe);
		b.setMinHeight(60.);
		b.setMaxHeight(60.);
		b.setMinWidth(60.);
		b.setMaxWidth(60.);
		b.setId(id);
		return b;
	}
	
	/*
	 * @TODO: immagine come sfondo della classe, pallino nascorto e padding
	 * sinistro necessari.
	 */
	public static RadioButton creaBottonePaziente(String nome, String classe, ToggleGroup gruppo) {
		RadioButton rb = new RadioButton(nome);
		rb.getStyleClass().add(classe);
                
                rb.setMinHeight(50);
                rb.setMaxWidth(180 - 40);
                
                rb.setWrapText(true);
		
		rb.setToggleGroup(gruppo);
		
		return rb;
	}
	
	public static void aggiungiPaziente(ToggleButton paziente) {
            paziente.setMaxWidth(180);
            paziente.setMinWidth(180);
            paziente.setMinHeight(40);
            paziente.setWrapText(true);
            pannelloPazienti.getChildren().add(paziente);
	}
	
	/*
	 * @TODO: recupera classe da impostazioni.
	 */
	private static Pane costruisciIntestazione(Button pulsante) {
		AnchorPane intestazione = new AnchorPane();
		
		intestazione.getStyleClass().add("intestazione");
		
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
	
	/*
	 * @TODO: recupera classe da impostazioni.
	 */
	private static Pane costruisciPannelloPazienti(ToggleGroup gruppo) {
		VBox pannello = new VBox();
		
                pannello.setMinWidth(180);
		pannello.setMaxWidth(180);
		
		
		return pannello;
	}
	
	private static Pane costruisciPannelliContenuto(Button indietro, Button avanti, TextField data, Label errore) {
                PannelloGlucosio pg = new PannelloGlucosio();
		pg.setMinHeight(220 - 32);
		pg.setMinWidth(320 - 66);
                pg.getStyleClass().add("pannello-statistica");
                pg.getStyleClass().add("pannello-glucosio");
		
		PannelloInsulina pi = new PannelloInsulina();
		pi.setMinHeight(240 - 32);
		pi.setMinWidth(320 - 66);
                pi.getStyleClass().add("pannello-statistica");
                pi.getStyleClass().add("pannello-insulina");
		
		PannelloGlucosioBasso pgb = new PannelloGlucosioBasso();
		pgb.setMinHeight(120 - 32);
		pgb.setMinWidth(430 - 66);
                pgb.getStyleClass().add("pannello-statistica");
                pgb.getStyleClass().add("pannello-glucosio-basso");
		
		PannelloGraficoGlicemico pgg = new PannelloGraficoGlicemico();
		pgg.setMinHeight(220 - 26); /**/
                pgg.setMaxHeight(220 - 26);
		pgg.setMinWidth(420 - 60);
                pgg.setMaxWidth(420 - 60);
                pgg.setId("pannello-grafico");
		
		HBox comandi = new HBox();
		comandi.setMinHeight(120);
		comandi.setMinWidth(430);
                comandi.getStyleClass().add("pannello-comandi");
                
                Label l = new Label("Settimana dal ");
		comandi.getChildren().addAll(indietro, l, data, avanti);
		
		GridPane contenuto = new GridPane();
		
		contenuto.setMinWidth(320 + 430);
		contenuto.setMinHeight(240 + 220);
		
		GridPane.setConstraints(pg, 0, 0, 1, 1);
		GridPane.setConstraints(pi, 0, 1, 1, 2);
		GridPane.setConstraints(pgb, 1, 1, 1, 1);
		GridPane.setConstraints(pgg, 1, 0, 1, 1);
		GridPane.setConstraints(comandi, 1, 2, 1, 1);
                
                // GridPane.setConstraints(errore, 1, 3, 1, 1);
		
		contenuto.getChildren().addAll(pg, pi, pgb, pgg, comandi/*, errore*/);
		
		return contenuto;
	}
	
	public static Pane costruisciInterfaccia(
                Button aprifile,
                Button indietro,
                Button avanti, 
                TextField data,
                ToggleGroup pazienti,
                Label errore
        ) {
		final BorderPane contenitore = new BorderPane();
		
		contenitore.setTop(costruisciIntestazione(aprifile));
		
		pannelloPazienti = costruisciPannelloPazienti(pazienti);
                ScrollPane sp = new ScrollPane();
                sp.setContent(pannelloPazienti);
                
                sp.setHbarPolicy(ScrollBarPolicy.NEVER);
                sp.setVbarPolicy(ScrollBarPolicy.NEVER);
                sp.setMinWidth(180);
		sp.setMaxWidth(180);
                sp.setMaxHeight(460);
                sp.setMinHeight(460);
                sp.getStyleClass().add("lista-utenti");
                
                sp.minHeight(pannelloPazienti.minHeightProperty().get());
                sp.minWidth(pannelloPazienti.minWidthProperty().get());
                sp.maxHeight(pannelloPazienti.maxHeightProperty().get());
                sp.maxWidth(pannelloPazienti.maxWidthProperty().get());

		contenitore.setLeft(sp);
		contenitore.setCenter(costruisciPannelliContenuto(indietro, avanti, data, errore));
		
		return contenitore;
	}
	
}

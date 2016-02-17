/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author Gabriele Ara
 */
public class CostruttoreUI {
	private static Pane pannelloPazienti;

	private CostruttoreUI() {
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
		rb.setMinWidth(200);
		rb.setMaxWidth(200);
		rb.setMinHeight(50);
		
		rb.setToggleGroup(gruppo);
		
		return rb;
	}
	
	public static void aggiungiPaziente(ToggleButton paziente) {
		pannelloPazienti.getChildren().add(paziente);
	}
	
	/*
	 * @TODO: recupera classe da impostazioni.
	 */
	private static Pane costruisciIntestazione(Button pulsante) {
		AnchorPane intestazione = new AnchorPane();
		
		intestazione.getStyleClass().add("intestazione");
		
		intestazione.setMinHeight(80.);
		intestazione.setMaxHeight(80.);
		
		intestazione.setMinWidth(900);
		
		Label titolo = new Label("Controllo del glucosio");
		
		AnchorPane.setTopAnchor(titolo, 10.0);
		AnchorPane.setBottomAnchor(titolo, 10.0);
		AnchorPane.setLeftAnchor(titolo, 10.0);
		AnchorPane.setRightAnchor(titolo, 80.0);
		
		intestazione.getChildren().add(titolo);
		
		AnchorPane.setTopAnchor(pulsante, 10.0);
		AnchorPane.setBottomAnchor(pulsante, 10.0);
		AnchorPane.setRightAnchor(pulsante, 10.0);
		
		intestazione.getChildren().add(pulsante);
		
		return intestazione;
	}
	
	/*
	 * @TODO: recupera classe da impostazioni.
	 */
	private static Pane costruisciPannelloPazienti(ToggleGroup gruppo) {
		VBox pannello = new VBox();
		pannello.getStyleClass().add("utenti");
		
		pannello.setMinWidth(200);
		pannello.setMaxWidth(200);
		pannello.setMinHeight(500. - 80.);
		
		return pannello;
	}
	
	private static Pane costruisciPannelliContenuto(Button indietro, Button avanti, TextField data) {
                PannelloGlucosio pg = new PannelloGlucosio();
		pg.setMinHeight(300);
		pg.setMinWidth(350);
		
		PannelloInsulina pi = new PannelloInsulina();
		
		pi.setMinHeight(300);
		pi.setMinWidth(350);
		
		PannelloGlucosioBasso pgb = new PannelloGlucosioBasso();
		
		pgb.setMinHeight(150);
		pgb.setMinWidth(350);
		
		PannelloGraficoGlicemico pgg = new PannelloGraficoGlicemico();
		
		pgg.setMinHeight(300);
		pgg.setMinWidth(350);
                pgg.setId("pannello-grafico");
		
		HBox comandi = new HBox();
		
		comandi.setMinHeight(150);
		comandi.setMinWidth(350);
		
		comandi.getChildren().addAll(indietro, data, avanti);
		
		GridPane contenuto = new GridPane();
		
		contenuto.setMinWidth(700);
		contenuto.setMinHeight(500 - 80);
		
		GridPane.setConstraints(pg, 0, 0, 1, 1);
		GridPane.setConstraints(pi, 0, 1, 1, 2);
		GridPane.setConstraints(pgb, 1, 1, 1, 1);
		GridPane.setConstraints(pgg, 1, 0, 1, 1);
		GridPane.setConstraints(comandi, 1, 2, 1, 1);
		
		contenuto.getChildren().addAll(pg, pi, pgb, pgg, comandi);
		
		return contenuto;
	}
	
	public static Pane costruisciInterfaccia(
                Button aprifile,
                Button indietro,
                Button avanti, 
                TextField data,
                ToggleGroup pazienti
        ) {
		final BorderPane contenitore = new BorderPane();
		
		/* TODO: stile */
		
		contenitore.setTop(costruisciIntestazione(aprifile));
		
		pannelloPazienti = costruisciPannelloPazienti(pazienti);
		contenitore.setLeft(pannelloPazienti);
		contenitore.setCenter(costruisciPannelliContenuto(indietro, avanti, data));
		
		return contenitore;
	}
	
}

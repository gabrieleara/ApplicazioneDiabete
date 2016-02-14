/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.TipoStatistica;
import diabete.pannelli.PannelloGlucosio;
import diabete.pannelli.PannelloInsulina;
import diabete.pannelli.PannelloGraficoGlicemico;
import diabete.pannelli.PannelloGlucosioBasso;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * @author Gabriele Ara
 */
public class CostruttoreUI {
	
	public static final int INDEX_P_GLUCOSIO = 0;
	public static final int INDEX_P_INSULINA = 1;
	public static final int INDEX_P_GRAFICO = 2;
	public static final int INDEX_P_GLUCOSIO_BASSO = 3;
	public static final int INDEX_P_COMANDI = 4;
	
	private static Pane pannelloUtenti;

	private CostruttoreUI() {
		
	}
	
	public static TextField creaCampoTesto(String classe) {
		TextField tf = new TextField();
		
		tf.getStyleClass().add(classe);
		
		return tf;
	}
	
	/*
	 * @TODO: immagine come sfondo della classe
	 */
	public static final Button creaBottoneQuadrato(String testo, String classe) {
		Button b = new Button(testo);
		b.getStyleClass().add(classe);
		b.setMinHeight(60.);
		b.setMaxHeight(60.);
		b.setMinWidth(60.);
		b.setMaxWidth(60.);
		
		return b;
	}
	
	/*
	 * @TODO: immagine come sfondo della classe, pallino nascorso e padding
	 * sinistro necessari.
	 */
	public static RadioButton creaBottoneUtente(String nome, String classe, ToggleGroup gruppo) {
		RadioButton rb = new RadioButton(nome);
		rb.getStyleClass().add(classe);
		rb.setMinWidth(200);
		rb.setMaxWidth(200);
		rb.setMinHeight(50);
		
		rb.setToggleGroup(gruppo);
		
		return rb;
	}
	
	public void aggiungiUtente(ToggleButton utente) {
		pannelloUtenti.getChildren().add(utente);
	}
	
	/*
	 * @TODO: recupera classe da impostazioni.
	 */
	public static Pane creaIntestazione(Button pulsante) {
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
	 * @TODO: recupera classe da impostazioni. Ultimo utente da cache.
	 */
	public static Pane creaPannelloUtenti(ToggleGroup gruppo) {
		VBox pannello = new VBox();
		ObservableList<Node> lista = pannello.getChildren();
		
		Collection<String> utenti = Cache.getValoreIniziale().getUtenti();
		pannello.getStyleClass().add("utenti");
		
		pannello.setMinWidth(200);
		pannello.setMaxWidth(200);
		pannello.setMinHeight(500. - 80.);
		
		RadioButton rb;
		for(String nome : utenti) {
			rb = CostruttoreUI.creaBottoneUtente(nome, "bottoneutente", gruppo);
			lista.add(rb);
		}
		
		return pannello;
	}
	
	public static Pane[] creaPannelliDati(Button b1, Button b2, TextField tf) {
		Pane[] pannelli = new Pane[5];
		
		Cache valori = Cache.getValoreIniziale();
		
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
		
		HBox comandi = new HBox();
		
		comandi.setMinHeight(150);
		comandi.setMinWidth(350);
		
		comandi.getChildren().addAll(b1, tf, b2);
		
		pannelli[INDEX_P_GLUCOSIO] = pg;
		pannelli[INDEX_P_INSULINA] = pi;
		pannelli[INDEX_P_GLUCOSIO_BASSO] = pgb;
		pannelli[INDEX_P_GRAFICO] = pgg;
		pannelli[INDEX_P_COMANDI] = comandi;
		
		return pannelli;
	}
	
	public static Pane creaContenuto(Pane[] pannelli) {
		GridPane contenuto = new GridPane();
		
		contenuto.setMinWidth(700);
		contenuto.setMinHeight(500 - 80);
		
		GridPane.setConstraints(pannelli[INDEX_P_GLUCOSIO], 0, 0, 1, 1);
		GridPane.setConstraints(pannelli[INDEX_P_INSULINA], 0, 1, 1, 2);
		GridPane.setConstraints(pannelli[INDEX_P_GLUCOSIO_BASSO], 1, 1, 1, 1);
		GridPane.setConstraints(pannelli[INDEX_P_GRAFICO], 1, 0, 1, 1);
		GridPane.setConstraints(pannelli[INDEX_P_COMANDI], 1, 2, 1, 1);
		
		contenuto.getChildren().addAll(java.util.Arrays.asList(pannelli));
		
		return contenuto;
	}
	
	public static Pane creaUI(Button intest, ToggleGroup gruppo, Pane[] pannelli) {
		final BorderPane contenitore = new BorderPane();
		
		/* TODO: stile */
		
		contenitore.setTop(creaIntestazione(intest));
		
		pannelloUtenti = creaPannelloUtenti(gruppo);
		contenitore.setLeft(pannelloUtenti);
		
		contenitore.setCenter(creaContenuto(pannelli));
		
		return contenitore;
	}
	
}

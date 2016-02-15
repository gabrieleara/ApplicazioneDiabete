/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.dati.GlicemiaRilevata;
import diabete.dati.TipoStatistica;
import diabete.pannelli.*;
import diabete.util.CalendarioSettimanale;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javafx.application.Application;
import javafx.beans.value.*;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author Gabriele Ara
 */
public class ApplicazioneDiabete extends Application implements ChangeListener<Toggle>, EventHandler<ActionEvent>{
	private Button leggiFile;
	private Button settimanaIndietro;
	private Button settimanaAvanti;
	private TextField settimanaAttuale;
	private ToggleGroup utenti;
	
	private String utenteAttuale;
	private Date dataAttuale;
	
	private Pane[] pannelli;
	
	void setUtenteVisualizzato() {
		ObservableList<Toggle> bottoni = utenti.getToggles();
		
		for (Iterator<Toggle> it = bottoni.iterator(); it.hasNext();) {
			RadioButton utente = (RadioButton) it.next();
			if(utente.getText().equals(utenteAttuale)) {
				utente.setSelected(true);
				utente.requestFocus();
				break;
			}
		}
	}
	
	/*
	 * @ TODO
	 */
	private void aggiornaUI() {
		/* imposta utente nel gruppo */
		setUtenteVisualizzato();
		
		/* ottiene valori glicemici */
		ArrayList<GlicemiaRilevata> gc = GestoreDatiDiabetici.glicemiaSettimanale(utenteAttuale, Calendar.getInstance().getTime());
		
		/* aggiorna i dati del grafico */
		
		PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico) pannelli[CostruttoreUI.INDEX_P_GRAFICO];
		pgg.aggiornaDati(gc);
		
		/* ottiene le statistiche e aggiorna i dati */
		int[] stat = AnalizzatoreDiabetico.analizzaGlucosioMedio(gc);
		PannelloGlucosio pg = (PannelloGlucosio) pannelli[CostruttoreUI.INDEX_P_GLUCOSIO];
		pg.aggiornaDati(stat);
		
		stat = AnalizzatoreDiabetico.analizzaEventiGlucosioBasso(gc);
		PannelloGlucosioBasso pgb = (PannelloGlucosioBasso) pannelli[CostruttoreUI.INDEX_P_GLUCOSIO_BASSO];
		pgb.aggiornaDati(stat);
		
		/* ottiene e aggiorna i dati dell'insulina */
		
		
		/* imposta la data nel campo data */
		
	}
	
	private void aggiornaUI(Date data) {
		CalendarioSettimanale cal = new CalendarioSettimanale();
		
		cal.setTime(data);
		cal.lunedi();
		cal.resetTempoDelGiorno();
		data = cal.getTime();
		if(data.equals(dataAttuale))
			return;
		
		dataAttuale = data;
		
		aggiornaUI();
	}
	
	private void aggiornaUI(String utente) {
		if(utente.equals(utenteAttuale))
			return;
		
		utenteAttuale = utente;
		
		Date ultimaData = DBDiabete.getUltimaData(utente);
		CalendarioSettimanale cal = new CalendarioSettimanale();
		
		cal.setTime(ultimaData);
		cal.lunedi();
		cal.resetTempoDelGiorno();
		dataAttuale = cal.getTime();
		
		aggiornaUI();
		
	}
	
	@Override
	public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
		RadioButton rb = (RadioButton) newValue;
		aggiornaUI(rb.getText());
	}
	
	@Override
	public void handle(ActionEvent event) {
		try {
			if(settimanaAttuale == (TextField) event.getSource()) {
				/**/
				return;
			}
		} catch(ClassCastException ex) {
		}
		
		Button src = (Button) event.getSource();
		
		if(src == leggiFile) {
			/* TODO */
			return;
		}
		
		if(src == settimanaAvanti) {
			/* TODO */
			
			/* TEST */
			aggiornaUI();
			return;
		}
		
		if(src == settimanaIndietro) {
			/* TODO */
			return;
		}
	}
	
	private void impostaValoriIniziali() {
		Cache c = Cache.getValoreIniziale();
		
		utenteAttuale = c.getUtenteAttuale();
		dataAttuale = c.getDataAttuale();
		
		setUtenteVisualizzato();
		
		PannelloGlucosio pg = (PannelloGlucosio) pannelli[CostruttoreUI.INDEX_P_GLUCOSIO];
		pg.aggiornaDati(
				c.getStatistica(TipoStatistica.GLUCOSIO_MEDIO),
				c.getStatistica(TipoStatistica.GLUCOSIO_SOPRA_INTERVALLO),
				c.getStatistica(TipoStatistica.GLUCOSIO_SOTTO_INTERVALLO));
		
		PannelloInsulina pi = (PannelloInsulina) pannelli[CostruttoreUI.INDEX_P_INSULINA];
		pi.aggiornaDati(
				c.getStatistica(TipoStatistica.INSULINA_LENTA),
				c.getStatistica(TipoStatistica.INSULINA_RAPIDA));
		
		PannelloGlucosioBasso pgb = (PannelloGlucosioBasso) pannelli[CostruttoreUI.INDEX_P_GLUCOSIO_BASSO];
		pgb.aggiornaDati(
				c.getStatistica(TipoStatistica.EVENTI_GLUCOSIO_BASSO),
				c.getStatistica(TipoStatistica.DURATA_EVENTI_GLUCOSIO_BASSO));
		
		PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico) pannelli[CostruttoreUI.INDEX_P_GRAFICO];
		pgg.aggiornaDati(c.getDatiDelGrafo());
		
		/* TODO: Cambia nel formato */
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
		
		settimanaAttuale.setText(df.format(dataAttuale));
	}
	
	private Pane creaUI() {
		leggiFile = CostruttoreUI.creaBottoneQuadrato("Apri file", "aprifile");
		settimanaIndietro = CostruttoreUI.creaBottoneQuadrato("Indietro", "settind");
		settimanaAvanti = CostruttoreUI.creaBottoneQuadrato("Avanti", "settavnt");
		settimanaAttuale = CostruttoreUI.creaCampoTesto("setttxt");
		
		utenti = new ToggleGroup();
		
		pannelli = CostruttoreUI.creaPannelliDati(settimanaIndietro,
				settimanaAvanti,
				settimanaAttuale);
		
		Pane contenuto = CostruttoreUI.creaUI(leggiFile, utenti, pannelli);
		
		impostaValoriIniziali();
		
		/* Listeners */
		utenti.selectedToggleProperty().addListener(this);
		
		leggiFile.setOnAction(this);
		settimanaIndietro.setOnAction(this);
		settimanaAvanti.setOnAction(this);
		settimanaAttuale.setOnAction(this);
		
		return contenuto;
	}
	
	private Cache leggiCache(String fileName) {
		Cache cache = null;
		try(
				FileInputStream fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
			) {
			cache = (Cache) ois.readObject();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		return cache;
	}
	
	private void scriviCache(String fileName) {
		/* TODO: Recupera dati */
		
		Cache cache /*= new Cache()*/;
		try(
				FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
			) {
			/* oos.writeObject(cache); */
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		Cache.setValoreIniziale(leggiCache("cache.bin"));
		Pane root = creaUI();
		Scene scene = new Scene(root);
		
		utenteAttuale = Cache.getValoreIniziale().getUtenteAttuale();
		dataAttuale = Cache.getValoreIniziale().getDataAttuale();
		
		primaryStage.setTitle("Controllo del glucosio");
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(900 + 20); // TODO: per quale motivo?
		primaryStage.setMinHeight(500 + 40); // TODO: per quale motivo?
		primaryStage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

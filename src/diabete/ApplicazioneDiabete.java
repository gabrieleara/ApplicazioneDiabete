/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import com.thoughtworks.xstream.XStream;
import diabete.interfaccia.*;
import diabete.dati.*;
import diabete.util.*;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class ApplicazioneDiabete extends Application implements ChangeListener<Toggle>, EventHandler<ActionEvent>{
	private Stage stage;
	private Button leggiFile;
	private Button settimanaIndietro;
	private Button settimanaAvanti;
	private TextField settimanaAttuale;
	private ToggleGroup pazienti;
	
	private StatoApplicazione stato;
	
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
    
	/* TODO:
	 * vedrai non sono necessari, forse servono solo
	 * quello del grafico e quello degli utenti.
	 */
	private Pane[] pannelliDati;
	
	private void aggiornaUtenteVisualizzato() {
		String pazienteAttuale = stato.getPazienteAttuale().get();
		
		ObservableList<Toggle> bottoni = pazienti.getToggles();
		
		for (Iterator<Toggle> it = bottoni.iterator(); it.hasNext();) {
			RadioButton utente = (RadioButton) it.next();
			if(utente.getText().equals(pazienteAttuale)) {
				utente.setSelected(true);
				utente.requestFocus();
				break;
			}
		}
	}
	
	/*
	 * @ TODO
	 */
	private void aggiornaStatistiche() {
		String pazienteAttuale = stato.getPazienteAttuale().get();
		Date dataAttuale = stato.getDataAttuale().get();
		
		/* imposta utente nel gruppo */
		aggiornaUtenteVisualizzato();
                
        /* TODO: imposta la data nel campo data con la bind */
		settimanaAttuale.setText(df.format(dataAttuale));
		
		/* ottiene valori glicemici */
		ArrayList<GlicemiaRilevata> gc;
		int[] si;
		try {
		    gc = GestoreDatiDiabetici.glicemiaSettimanale(pazienteAttuale, dataAttuale);
			si = GestoreDatiDiabetici.insulinaSettimanale(pazienteAttuale, dataAttuale);
		} catch(SQLException ex) {
			ex.printStackTrace(); // TODO
			return;
		}
		
		/* aggiorna i dati del grafico */
		stato.setDatiPerGrafico(gc);
        
		/* ottiene le statistiche e aggiorna i dati */
		int[] statistiche = AnalizzatoreDiabetico.analizza(gc);
		
		statistiche[TipoStatistica.INSULINA_LENTA.valore] = si[0];
		statistiche[TipoStatistica.INSULINA_RAPIDA.valore] = si[1];
		
		stato.setStatistiche(statistiche);
	}
	
	private void aggiornaStatistiche(Date data) {
		Date dataAttuale = stato.getDataAttuale().get();
		
		CalendarioSettimanale cal = new CalendarioSettimanale();
		
		cal.setTime(data);
		cal.lunedi();
		cal.resetTempoDelGiorno();
		data = cal.getTime();
		
		if(data.equals(dataAttuale))
			return;
		
		/* TODO: Controllare da qualche parte se la data è sensata*/
		
		stato.setDataAttuale(data);
	}
	
	private void aggiornaStatistiche(String paziente) { // TODO: listener al cambio del bottone nella toggle
		String pazienteAttuale = stato.getPazienteAttuale().get();
		
		if(paziente.equals(pazienteAttuale))
				return;

		stato.setPazienteAttuale(paziente);

		try {
			stato.setDataAttuale(GestoreDatiDiabetici.ultimaSettimana(paziente));
		} catch(SQLException ex) {
			ex.printStackTrace(); // TODO
		}
	}
        
	private void leggiFile() {
		RaccoltaDatiDiabetici rdd;
		String nomeFile;

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Scegli File");
		File f = fileChooser.showOpenDialog(stage);

		if(f == null)
			return;

		
		try {
			nomeFile = f.getCanonicalPath();
			rdd = LettoreFileXML.leggiFileDatiGlicemici(nomeFile);
		} catch (SAXException ex) {
			ex.printStackTrace(); // getMessage();
			return;
		} catch (IOException ex) {
			ex.printStackTrace(); // getMessage();
			return;
		}
		
		try {
			GestoreDatiDiabetici.salva(rdd);
			
			String paziente = rdd.paziente;
			Collection listaPazienti = stato.getPazienti();
			if(!listaPazienti.contains(paziente))
				listaPazienti.add(paziente);
			else
				aggiornaStatistiche(paziente);
			
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO 
		}

	}
	
	@Override
	public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
		RadioButton rb = (RadioButton) newValue;
		aggiornaStatistiche(rb.getText());
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
			leggiFile();
			return;
		}
		
		if(src == settimanaAvanti) {
			/* TODO */
			
			/* TEST */
			aggiornaStatistiche();
			return;
		}
		
		if(src == settimanaIndietro) {
			/* TODO */
			return;
		}
	}
	
	private void impostaListeners() {		
		stato.getPazienteAttuale().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			aggiornaStatistiche();
		});
		
		stato.getDataAttuale().addListener((ObservableValue<? extends Date> observable, Date oldValue, Date newValue) -> {
			aggiornaStatistiche();
		});
		
		stato.getDatiPerGrafico().addListener((ListChangeListener.Change<? extends GlicemiaRilevata> c) -> {
			/* TODO */
			PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico) pannelliDati[CostruttoreUI.INDEX_P_GRAFICO];
			pgg.aggiornaDati(stato.getDatiPerGrafico());
		});
		
		stato.getPazienti().addListener((ListChangeListener.Change<? extends String> change) -> {
			for(String paziente : change.getAddedSubList()) {
				aggiungiPaziente(paziente);
				stato.setPazienteAttuale(paziente);
			}
		});
		
		PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico) pannelliDati[CostruttoreUI.INDEX_P_GRAFICO];
		pgg.aggiornaDati(stato.getDatiPerGrafico());
		
		pazienti.selectedToggleProperty().addListener(this);
		
		leggiFile.setOnAction(this);
		settimanaIndietro.setOnAction(this);
		settimanaAvanti.setOnAction(this);
		settimanaAttuale.setOnAction(this);
	}
	
	private Pane creaUI() {
		leggiFile = CostruttoreUI.creaBottoneQuadrato("Apri file", "aprifile");
		settimanaIndietro = CostruttoreUI.creaBottoneQuadrato("Indietro", "settind");
		settimanaAvanti = CostruttoreUI.creaBottoneQuadrato("Avanti", "settavnt");
		settimanaAttuale = CostruttoreUI.creaCampoTesto("setttxt");
		
		pazienti = new ToggleGroup();
		
		pannelliDati = CostruttoreUI.creaPannelliDati(settimanaIndietro,
				settimanaAvanti,
				settimanaAttuale);
		
		Pane contenuto = CostruttoreUI.creaUI(leggiFile, pazienti, pannelliDati);
		
		
		/* Listeners */
		impostaListeners();
		
		Collection<String> lista = stato.getPazienti();
		
		for(String nome : lista) {
			aggiungiPaziente(nome);
		}
		
		settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
		aggiornaUtenteVisualizzato();
		
		return contenuto;
	}
	
	@Override
	public void start(Stage primaryStage) {
        stage = primaryStage;
		
		try {
			Cache cache = Cache.leggiCache();
			StatoApplicazione.init(cache);
		} catch(IOException | ClassNotFoundException ex) {
			StatoApplicazione.init();
		}
		
		stato = StatoApplicazione.getInstance();
		
		Pane root = creaUI();
		Scene scene = new Scene(root);
		
		stage.setTitle("Controllo del glucosio");
		stage.setScene(scene);
		stage.setMinWidth(900 + 20); // TODO: per quale motivo?
		stage.setMinHeight(500 + 40); // TODO: per quale motivo?
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private void aggiungiPaziente(String paziente) {
		RadioButton rb = CostruttoreUI.creaBottonePaziente(paziente, "bottoneutente", pazienti);
		CostruttoreUI.aggiungiPaziente(rb);
	}
}

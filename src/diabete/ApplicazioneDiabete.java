/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.configurazione.GestoreConfigurazione;
import diabete.interfaccia.*;
import diabete.dati.*;
import diabete.util.*;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class ApplicazioneDiabete extends javafx.application.Application {
	/* Se ho questo posso recuperare la roba con id e mi serve per
	 * il filechooser.
	 */
	private Stage primaryStage;
	private ToggleGroup pazienti;
	
	private StatoApplicazione stato;
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
	private void aggiornaPazienteVisualizzato() {
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
        
	private void aggiornaGrafico() {
		try {
			PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico)
				primaryStage.sceneProperty().get().lookup("#pannello-grafico");
			pgg.aggiornaDati(stato.getDatiPerGrafico());
		} catch (NullPointerException ex) {

		}
	}
	
	/*
	 * @ TODO
	 */
	private void aggiornaStatistiche() {
		String pazienteAttuale = stato.getPazienteAttuale().get();
		Date dataAttuale = stato.getDataAttuale().get();
		
		/* imposta utente nel gruppo */
		aggiornaPazienteVisualizzato();
                
        /* TODO: imposta la data nel campo data con la bind */
                TextField settimanaAttuale = (TextField)
                    primaryStage.sceneProperty().get().lookup("#sett-attuale");
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
        
	private void leggiFile() {
		RaccoltaDatiDiabetici rdd;
		String nomeFile;

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Scegli File");
		File f = fileChooser.showOpenDialog(primaryStage);

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
			
			stato.setPazienteAttuale(paziente);
			
			if(rdd.rilevazioni.length > 0)
				stato.setDataAttuale(rdd.rilevazioni[0].timestamp);
			else if(rdd.iniezioni.length > 0)
				stato.setDataAttuale(rdd.iniezioni[0].timestamp);
			
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO 
		}

	}
	
	private void impostaListeners(Button leggiFile,
                Button settimanaIndietro,
                Button settimanaAvanti, TextField settimanaAttuale) {		
		stato.getPazienteAttuale().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			try {
				stato.setDataAttuale(GestoreDatiDiabetici.ultimaSettimana(newValue));
			} catch(SQLException ex) {
				stato.setDataAttuale(new Date());
				ex.printStackTrace(); // TODO
			}
			aggiornaStatistiche();
		});
		
		stato.getDataAttuale().addListener((ObservableValue<? extends Date> observable, Date oldValue, Date newValue) -> {
			aggiornaStatistiche();
		});
		
		stato.getDatiPerGrafico().addListener((ListChangeListener.Change<? extends GlicemiaRilevata> c) -> {
			aggiornaGrafico();
		});
		
                /* TODO: testing */
		stato.getPazienti().addListener((ListChangeListener.Change<? extends String> change) -> {
			while(change.next()) {
				if(change.wasAdded()) {
					for(String paziente : change.getAddedSubList()) {
						aggiungiPaziente(paziente);
						stato.setPazienteAttuale(paziente);
					}
				}
			}
		});
		
		pazienti.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
			RadioButton rb = (RadioButton) newValue;
			if(!stato.getPazienteAttuale().get().equals(rb.getText())) {
				stato.setPazienteAttuale(rb.getText());
				//aggiornaStatistiche();
			}
		});
		
                leggiFile.setOnAction((ActionEvent event) -> {
			leggiFile();
		});
		
		settimanaIndietro.setOnAction((ActionEvent event) -> {
			cambiaSettimana(-1);
		});
		
		settimanaAvanti.setOnAction((ActionEvent event) -> {
			cambiaSettimana(1);
		});
		
		settimanaAttuale.setOnAction((ActionEvent event) -> {
			try {
				Date d = df.parse(settimanaAttuale.getText());
				aggiornaStatistiche(d);
			} catch(ParseException ex) {
				ex.printStackTrace(); // TODO
			}
		});
	}
	
	private Pane creaUI() {
		Button leggiFile = CostruttoreUI.creaBottoneQuadrato("leggi-file", "Apri file", "aprifile");
		Button settimanaIndietro = CostruttoreUI.creaBottoneQuadrato("sett-indietro", "Indietro", "settind");
		Button settimanaAvanti = CostruttoreUI.creaBottoneQuadrato("sett-indietro", "Avanti", "settavnt");
		TextField settimanaAttuale = CostruttoreUI.creaCampoTesto("sett-attuale", "setttxt");
		
		pazienti = new ToggleGroup();
		
		Pane contenuto = CostruttoreUI.costruisciInterfaccia(leggiFile,
                        settimanaIndietro, settimanaAvanti, settimanaAttuale,
                        pazienti);
		
		
		/* Listeners */
		impostaListeners(leggiFile, settimanaIndietro, settimanaAvanti, settimanaAttuale);
		
		Collection<String> lista = stato.getPazienti();
		
		for(String nome : lista) {
			aggiungiPaziente(nome);
		}
		
		settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
		
		return contenuto;
	}
	
	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            GestoreConfigurazione.init();
        }   catch (SAXException | IOException ex) {
                ex.printStackTrace();
            }
		
		try {
			Cache cache = Cache.leggiCache();
			StatoApplicazione.init(cache);
		} catch(IOException | ClassNotFoundException ex) {
			StatoApplicazione.init();
		}
		
		stato = StatoApplicazione.getInstance();
		
		Pane root = creaUI();
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Controllo del glucosio");
		primaryStage.setScene(scene);
		
		primaryStage.setOnCloseRequest((WindowEvent we) -> {
			Cache.scriviCache();
		});
		
		aggiornaGrafico();
		aggiornaPazienteVisualizzato();
                
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

	private void aggiungiPaziente(String paziente) {
		RadioButton rb = CostruttoreUI.creaBottonePaziente(paziente, "bottoneutente", pazienti);
		CostruttoreUI.aggiungiPaziente(rb);
	}

	private void cambiaSettimana(int verso) {
		CalendarioSettimanale cs = new CalendarioSettimanale();
		cs.setTime(stato.getDataAttuale().get());
		cs.add(Calendar.DAY_OF_MONTH, verso * 7);
		
		/* TODO:
		 * controlla se esiste la data indicata,
		 * altrimenti prendi quella direttamente più piccola/grande (dipende dal verso)
		 * oppure ancora quella minima/massima.
		 */
		aggiornaStatistiche(cs.getTime());
		// stato.setDataAttuale(cs.getTime());
		
	}
}

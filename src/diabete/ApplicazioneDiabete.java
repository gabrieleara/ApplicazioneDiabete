/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete;

import diabete.configurazione.GestoreConfigurazione;
import diabete.configurazione.TipoParametro;
import diabete.interfaccia.*;
import diabete.dati.*;
import diabete.util.*;

import java.io.*;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import javafx.beans.property.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
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
        private final StringProperty messaggio = new SimpleStringProperty();
	private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
	private void aggiornaPazienteVisualizzato() {
		String pazienteAttuale = stato.getPazienteAttuale().get();
		
		ObservableList<Toggle> bottoni = pazienti.getToggles();
		
		String color = (String) GestoreConfigurazione.ottieniParametro(TipoParametro.COLORE_UTENTE_SELEZIONATO);
		
		for (Iterator<Toggle> it = bottoni.iterator(); it.hasNext();) {
			RadioButton utente = (RadioButton) it.next();
			if(utente.getText().equals(pazienteAttuale)) {
				utente.setSelected(true);
				utente.requestFocus();
				
				utente.setStyle("-fx-background-color: " + color + ";");
			} else {
				utente.setStyle("-fx-background-color: transparent;");
			}
		}
	}
        
	private void aggiornaGrafico() {
		try {
			PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico)
				primaryStage.sceneProperty().get().lookup("#pannello-grafico");
			pgg.aggiornaDati(AnalizzatoreDiabetico.glicemiaMediaOraria(stato.getDatiPerGrafico()));
		} catch (NullPointerException ex) {

		}
	}
	
	private void aggiornaStatistiche() {
		String pazienteAttuale = stato.getPazienteAttuale().get();
		Date dataAttuale = stato.getDataAttuale().get();
		
		/* imposta utente nel gruppo */
		aggiornaPazienteVisualizzato();
                
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
			messaggio.set("Errore nell'interrogazione dal database!");
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
	
	private void aggiornaStatistiche(Date data) throws SQLException {
		Date dataAttuale = stato.getDataAttuale().get();
		
		CalendarioSettimanale cal = new CalendarioSettimanale();
		
		cal.setTime(data);
		cal.lunedi();
		cal.resetTempoDelGiorno();
		data = cal.getTime();
		
		if(data.equals(dataAttuale))
			return;
		
		cal.add(Calendar.DAY_OF_MONTH, 7);
		data = cal.getTime();
		
		data = GestoreDatiDiabetici.settimanaIndietro(
				stato.getPazienteAttuale().get(),
				data);
		
		
		stato.setDataAttuale(data);
		
		TextField tf = (TextField) primaryStage.getScene().lookup("#sett-attuale");
		tf.setText(df.format(data));
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
			messaggio.set("Errore nella validazione del file XML.");
			return;
		} catch (IOException ex) {
			messaggio.set("Errore nella lettura da file.");
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
			// messaggio.set("");
		} catch (SQLException ex) {
			messaggio.set("Errore nell'interrogazione del database.");
		}

	}
	
	private void impostaListeners(Button leggiFile,
                Button settimanaIndietro,
                Button settimanaAvanti, TextField settimanaAttuale) {		
		stato.getPazienteAttuale().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			try {
				stato.setDataAttuale(GestoreDatiDiabetici.ultimaSettimana(newValue));
				// messaggio.set("");
			} catch(SQLException ex) {
				stato.setDataAttuale(new Date());
				messaggio.set("Errore nell'interrogazione del database.");
			}
			aggiornaStatistiche();
		});
		
		stato.getDataAttuale().addListener((ObservableValue<? extends Date> observable, Date oldValue, Date newValue) -> {
			aggiornaStatistiche();
		});
		
		stato.getDatiPerGrafico().addListener((ListChangeListener.Change<? extends GlicemiaRilevata> c) -> {
			aggiornaGrafico();
		});
		
		stato.getPazienti().addListener((ListChangeListener.Change<? extends String> change) -> {
			while(change.next()) {
				if(change.wasAdded()) {
					for(String paziente : change.getAddedSubList()) {
						aggiungiPaziente(paziente);
					}
				}
			}
		});
		
		pazienti.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
			RadioButton rb = (RadioButton) newValue;
			
			if(!stato.getPazienteAttuale().get().equals(rb.getText())) {
				try {
					LoggerAzioniUtente.log("Premuto un paziente nella lista.");
					// messaggio.set("");
				} catch(IOException ex) {
					messaggio.set("Errore nell'invio della stringa di log!");
				}
				stato.setPazienteAttuale(rb.getText());
			}
		});
		leggiFile.setOnAction((ActionEvent event) -> {
			try {
				LoggerAzioniUtente.log("Premuto il bottone di apertura file.");
			} catch(IOException ex) {
				messaggio.set("Errore nell'invio della stringa di log!");
			}
			
			leggiFile();
		});
		
		settimanaIndietro.setOnAction((ActionEvent event) -> {
			try {
				LoggerAzioniUtente.log("Premuto il bottone di settimana indietro.");
			} catch(IOException ex) {
				messaggio.set("Errore nell'invio della stringa di log!");
			}
			
			try {
				stato.setDataAttuale(
						GestoreDatiDiabetici.settimanaIndietro(
								stato.getPazienteAttuale().get(),
								stato.getDataAttuale().get()));
				// messaggio.set("");
			} catch (SQLException ex) {
				messaggio.set("Errore nell'interrogazione del database.");
			}
		});
		
		settimanaAvanti.setOnAction((ActionEvent event) -> {
			try {
				LoggerAzioniUtente.log("Premuto il bottone di settimana avanti.");
			} catch(IOException ex) {
				messaggio.set("Errore nell'invio della stringa di log!");
			}
			
			try {
				stato.setDataAttuale(
						GestoreDatiDiabetici.settimanaAvanti(
								stato.getPazienteAttuale().get(),
								stato.getDataAttuale().get()));
				// messaggio.set("");
			} catch (SQLException ex) {
				messaggio.set("Errore nell'interrogazione del database.");
			}
		});
		
		settimanaAttuale.setOnAction((ActionEvent event) -> {
			try {
				LoggerAzioniUtente.log("Inserito qualcosa nel campo settimana attuale.");
			} catch(IOException ex) {
				messaggio.set("Errore nell'invio della stringa di log!");
			}
			
			try {
				Date d = df.parse(settimanaAttuale.getText());
				settimanaAttuale.setText(df.format(d));
				aggiornaStatistiche(d);
				// messaggio.set("");
			} catch(ParseException ex) {
				messaggio.set("Errore nell'interpretazione della data inserita.");
				settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
			} catch (SQLException ex) {
				messaggio.set("Errore nell'interrogazione del database.");
				settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
			}
		});
	}
	
	private Pane creaUI() {
		Button leggiFile = CostruttoreUI.creaBottoneQuadrato("leggi-file", "Apri file", "bottonequadrato");
		Button settimanaIndietro = CostruttoreUI.creaBottoneQuadrato("sett-indietro", "Indietro", "bottonequadrato");
		Button settimanaAvanti = CostruttoreUI.creaBottoneQuadrato("sett-avanti", "Avanti", "bottonequadrato");
		TextField settimanaAttuale = CostruttoreUI.creaCampoTesto("sett-attuale", "setttxt");
		Label errore = CostruttoreUI.creaEtichetta("errore", "errore");
		
		errore.textProperty().bind(messaggio);
		
		pazienti = new ToggleGroup();
		
		Pane contenuto = CostruttoreUI.costruisciInterfaccia(leggiFile,
                        settimanaIndietro, settimanaAvanti, settimanaAttuale,
                        pazienti, errore);
		
		
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
		messaggio.set("");
        try {
            GestoreConfigurazione.init();
        } catch (SAXException | IOException ex) {
			messaggio.set("Errore nella lettura della configurazione.");
		}
		
		try {
			Cache cache = Cache.leggiCache();
			StatoApplicazione.init(cache);
		} catch(IOException | ClassNotFoundException ex) {
			StatoApplicazione.init();
			messaggio.set(messaggio.get() + "\nErrore nella lettura della cache.");
		}
		
		stato = StatoApplicazione.getInstance();
		
		Pane root = creaUI();
		Scene scene = new Scene(root);
                
        scene.getStylesheets().add((String) 
				GestoreConfigurazione.ottieniParametro(TipoParametro.PERCORSO_CSS));
		
		String nomeFont = (String) GestoreConfigurazione.ottieniParametro(TipoParametro.NOME_FONT);
		root.setStyle("-fx-font-family: " + nomeFont);
		
		primaryStage.setTitle("Controllo del glucosio");
		primaryStage.setScene(scene);
		
		primaryStage.setOnCloseRequest((WindowEvent we) -> {
			try {
				Cache.scriviCache();
			} catch (IOException ex) {
				messaggio.set("Errore nella scrittura nel file di cache!");
			}
		});
		
		aggiornaGrafico();
		aggiornaPazienteVisualizzato();
                
        primaryStage.setMinWidth(1040);
		primaryStage.setMinHeight(645);
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
}

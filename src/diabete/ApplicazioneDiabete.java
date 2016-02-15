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
	private ToggleGroup utenti;
	
	private String utenteAttuale;
	private Date dataAttuale;
	
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
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
                
                /* TODO: imposta la data nel campo data */
		
		/* ottiene valori glicemici */
                ArrayList<GlicemiaRilevata> gc;
                StatisticaInsulina[] si;
                try {
		    gc = GestoreDatiDiabetici.glicemiaSettimanale(utenteAttuale, dataAttuale);
                    si = GestoreDatiDiabetici.insulinaSettimanale(utenteAttuale, dataAttuale);
                } catch(SQLException ex) {
                    ex.printStackTrace(); // TODO
                    return;
                }
                
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
		
		/* TODO: aggiorna i dati dell'insulina */
		PannelloInsulina pi = (PannelloInsulina) pannelli[CostruttoreUI.INDEX_P_INSULINA];
		pi.aggiornaDati(si);
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

            try {
                dataAttuale = GestoreDatiDiabetici.ultimaSettimana(utente);
                aggiornaUI();
            } catch(SQLException ex) {
                ex.printStackTrace(); // TODO
            }
	}
        
        public void leggiFile() {
            RaccoltaDatiDiabetici rdd;
            String nomeFile;
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Scegli File");
            File f = fileChooser.showOpenDialog(stage);
            
            if(f == null)
                return;
            
            try {
                nomeFile = f.getCanonicalPath();
                ValidatoreXML.validaXML(nomeFile, "ril_diabete.xsd");
            } catch (SAXException ex) {
                ex.printStackTrace(); // getMessage();
                return;
            } catch (IOException ex) {
                ex.printStackTrace(); // getMessage();
                return;
            }
            
            try(
                FileInputStream fis = new FileInputStream(nomeFile);
                DataInputStream dis = new DataInputStream(fis);
                ) {
                rdd = (RaccoltaDatiDiabetici) new XStream().fromXML(dis.readUTF());
            } catch (IOException ex) {
                ex.printStackTrace(); // getMessage();
                return;
            }
            
            if(GestoreDatiDiabetici.salva(rdd))
                ;
                
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
			leggiFile();
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
	
	@Override
	public void start(Stage primaryStage) {
            stage = primaryStage;
		Pane root = creaUI();
		Scene scene = new Scene(root);
		
		utenteAttuale = Cache.getValoreIniziale().getUtenteAttuale();
		dataAttuale = Cache.getValoreIniziale().getDataAttuale();
		
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
}

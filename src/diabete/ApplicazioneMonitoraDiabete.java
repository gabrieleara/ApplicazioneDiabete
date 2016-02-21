package diabete;

import diabete.configurazione.*;
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
public class ApplicazioneMonitoraDiabete extends javafx.application.Application {
    private Stage primaryStage;
    private ToggleGroup pazienti;
    
    private StatoApplicazione stato;
    private final StringProperty messaggio = new SimpleStringProperty();
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
        
        String nomeFont = (String)
                GestoreConfigurazione.ottieniParametro(TipoParametro.NOME_FONT);
        root.setStyle("-fx-font-family: " + nomeFont);
        
        primaryStage.setTitle("Controllo del glucosio");
        primaryStage.setMinWidth(1040);
        primaryStage.setMinHeight(645);
        primaryStage.setScene(scene);
        
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            try {
                Cache.scriviCache();
            } catch (IOException ex) {
                messaggio.set("Errore nella scrittura nel file di cache!");
            }
        });
        
        // 01
        aggiornaPazienteVisualizzato();
        aggiornaGrafico();
        
        primaryStage.show();
    }
    
    private void aggiungiPaziente(String paziente) {
        RadioButton rb = CostruttoreUI.creaBottonePaziente(paziente, "bottoneutente", pazienti);
        CostruttoreUI.aggiungiPaziente(rb);
    }

    private void aggiornaData(Date data) throws SQLException {
        Date dataAttuale = stato.getDataAttuale().get();

        CalendarioSettimanale cal = new CalendarioSettimanale();
        
        // 02
        cal.setTime(data);
        cal.lunedi();
        cal.resetTempoDelGiorno();
        data = cal.getTime();

        if(data.equals(dataAttuale))
            return;
        
        // 03
        cal.add(Calendar.DAY_OF_MONTH, 7);
        data = cal.getTime();
        data = GestoreDatiDiabetici.cambiaSettimanaIndietro(
                stato.getPazienteAttuale().get(),
                data);
        
        stato.setDataAttuale(data);
        
        // 04
        TextField tf =
                (TextField) primaryStage.getScene().lookup("#sett-attuale");
        tf.setText(df.format(data));
    }
    
    private void aggiornaGrafico() {
        try {
            PannelloGraficoGlicemico pgg = (PannelloGraficoGlicemico)
            primaryStage.sceneProperty().get().lookup("#pannello-grafico");
            pgg.aggiornaDati(AnalizzatoreDatiDiabetici
                    .analizzaGlicemiaMediaOraria(stato.getDatiPerGrafico()));
        } catch (NullPointerException ex) {
            // 05
        }
    }

    private void aggiornaPazienteVisualizzato() {
        String pazienteAttuale = stato.getPazienteAttuale().get();
        ObservableList<Toggle> bottoni = pazienti.getToggles();

        String color = (String) GestoreConfigurazione
                .ottieniParametro(TipoParametro.COLORE_UTENTE_SELEZIONATO);
        
        // 06
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
    
    private void aggiornaStatistiche() {
        String pazienteAttuale = stato.getPazienteAttuale().get();
        Date dataAttuale = stato.getDataAttuale().get();
        
        aggiornaPazienteVisualizzato();
        
        TextField settimanaAttuale = (TextField)
        primaryStage.sceneProperty().get().lookup("#sett-attuale");
        settimanaAttuale.setText(df.format(dataAttuale));

        ArrayList<GlicemiaRilevata> gc;
        int[] si;
        try {
            gc = GestoreDatiDiabetici
                    .recuperaGlicemiaSettimanale(pazienteAttuale, dataAttuale);
            si = GestoreDatiDiabetici
                    .recuperaInsulinaSettimanale(pazienteAttuale, dataAttuale);
        } catch(SQLException ex) {
            messaggio.set("Errore nell'interrogazione dal database!");
            return;
        }
        
        stato.setDatiPerGrafico(gc);
        
        int[] statistiche = AnalizzatoreDatiDiabetici.produciStatisticheGlicemiche(gc);
        statistiche[TipoStatistica.INSULINA_LENTA.valore] = si[0];
        statistiche[TipoStatistica.INSULINA_RAPIDA.valore] = si[1];

        stato.setStatistiche(statistiche);
    }

    private void leggiFile() {
        RaccoltaDatiDiabetici rdd;
        String nomeFile;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Scegli File");
        File f = fileChooser.showOpenDialog(primaryStage);
        
        // 07
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
            GestoreDatiDiabetici.salvaDatiRilevati(rdd);
            
            String paziente = rdd.paziente;
            Collection listaPazienti = stato.getPazienti();
            
            if(!listaPazienti.contains(paziente))
                listaPazienti.add(paziente);
            
            stato.setPazienteAttuale(paziente);
            
            // 08
            if(rdd.rilevazioni.length > 0)
                stato.setDataAttuale(rdd.rilevazioni[0].timestamp);
        } catch (SQLException ex) {
            messaggio.set("Errore nell'interrogazione del database.");
        }
    }
    
    private Pane creaUI() {
        Button leggiFile = CostruttoreUI.creaBottoneQuadrato("leggi-file", "Apri file", "bottonequadrato");
        Button settimanaIndietro = CostruttoreUI.creaBottoneQuadrato("sett-indietro", "Indietro", "bottonequadrato");
        Button settimanaAvanti = CostruttoreUI.creaBottoneQuadrato("sett-avanti", "Avanti", "bottonequadrato");
        TextField settimanaAttuale = CostruttoreUI.creaCampoTesto("sett-attuale", "setttxt");
        
        // 09
        Label errore = CostruttoreUI.creaEtichetta("errore", "errore");
        errore.textProperty().bind(messaggio);
        
        pazienti = new ToggleGroup();
        
        Pane contenuto = CostruttoreUI.costruisciInterfaccia(leggiFile,
                settimanaIndietro, settimanaAvanti, settimanaAttuale,
                pazienti, errore);
        
        impostaListeners(leggiFile, settimanaIndietro, settimanaAvanti, settimanaAttuale);
        
        Collection<String> lista = stato.getPazienti();
        for(String nome : lista) {
            aggiungiPaziente(nome);
        }
        
        settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
        return contenuto;
    }

    private void impostaListeners(Button leggiFile,
            Button settimanaIndietro,
            Button settimanaAvanti, TextField settimanaAttuale) {
        // 10
        
        // 10.1
        stato.getPazienteAttuale().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                stato.setDataAttuale(
                        GestoreDatiDiabetici.recuperaUltimaSettimana(newValue));
            } catch(SQLException ex) {
                stato.setDataAttuale(new Date());
                messaggio.set("Errore nell'interrogazione del database.");
            }
            aggiornaStatistiche();
        });
        
        // 10.2
        stato.getDataAttuale().addListener((ObservableValue<? extends Date> observable, Date oldValue, Date newValue) -> {
            aggiornaStatistiche();
        });
        
        // 10.3
        stato.getDatiPerGrafico().addListener((ListChangeListener.Change<? extends GlicemiaRilevata> c) -> {
            aggiornaGrafico();
        });
        
        // 10.4
        stato.getPazienti().addListener((ListChangeListener.Change<? extends String> change) -> {
            while(change.next()) {
                // 11
                if(change.wasAdded()) {
                    for(String paziente : change.getAddedSubList()) {
                        aggiungiPaziente(paziente);
                    }
                }
            }
        });
        
        // 10.5
        pazienti.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            RadioButton rb = (RadioButton) newValue;
            
            if(!stato.getPazienteAttuale().get().equals(rb.getText())) {
                log("Premuto un paziente nella lista.");
                
                stato.setPazienteAttuale(rb.getText());
            }
        });
        
        // 10.6
        leggiFile.setOnAction((ActionEvent event) -> {
            log("Premuto il bottone di apertura file.");
            
            leggiFile();
        });
        
        // 10.7
        settimanaIndietro.setOnAction((ActionEvent event) -> {
            log("Premuto il bottone di settimana indietro.");
            
            try {
                stato.setDataAttuale(
                        GestoreDatiDiabetici.cambiaSettimanaIndietro(
                                stato.getPazienteAttuale().get(),
                                stato.getDataAttuale().get()));
            } catch (SQLException ex) {
                messaggio.set("Errore nell'interrogazione del database.");
            }
        });
        
        // 10.8
        settimanaAvanti.setOnAction((ActionEvent event) -> {
            log("Premuto il bottone di settimana avanti.");
            
            try {
                stato.setDataAttuale(
                        GestoreDatiDiabetici.cambiaSettimanaAvanti(
                                stato.getPazienteAttuale().get(),
                                stato.getDataAttuale().get()));
            } catch (SQLException ex) {
                messaggio.set("Errore nell'interrogazione del database.");
            }
        });
        
        // 10.9
        settimanaAttuale.setOnAction((ActionEvent event) -> {
            log("Inserito qualcosa nel campo settimana attuale.");
            
            try {
                Date d = df.parse(settimanaAttuale.getText());
                settimanaAttuale.setText(df.format(d));
                aggiornaData(d);
            } catch(ParseException ex) {
                messaggio.set("Errore nell'interpretazione della data inserita.");
                settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
            } catch (SQLException ex) {
                messaggio.set("Errore nell'interrogazione del database.");
                settimanaAttuale.setText(df.format(stato.getDataAttuale().get()));
            }
        });
    }
    
    private void log(String s) {
        try {
            LoggerAzioniUtente.log(s);
        } catch(IOException ex) {
            messaggio.set("Errore nell'invio della stringa di log!");
        }
    }
    
}


/*
    COMMENTI AL CODICE
    
    01) Imposta i valori iniziali visualizzati dai componenti che necessitano
        un aggiornamento differente dal semplice binding di un valore dello
        stato. Questi componenti hanno comunque un listener che aggiorna i
        valori visualizzati al variare dei corrispondenti valori nello stato.

    02) Per controllare se la data corrisponde alla stessa settimana indicata
        nello stato dell'applicazione si riporta la data al lunedì.

    03) Trucchetto: poiché non tutte le settimane avranno dei dati salvati,
        aggiungere 7 giorni e chiamare cambiaSettimanaIndietro garantisce di
        raggiungere sempre una settimana con dei dati disponibili; questa
        settimana sarà la stessa inserita, la sua antecedente più prossima
        oppure la prima settimana in assoluto della quale è possibile
        visualizzare i dati, se la data inserita è antecedente a tutte le
        settimane.

    04) Questo aggiornamento è necessario per riportare il valore nel campo
        di testo a un valore indicante il lunedì della settimana visualzizata,
        ottenuta secondo quanto al punto 03.
    
    05) Potenzialmente non necessario in seguito alle ultime modifiche, serve
        per evitare errori nella fase iniziale dell'applicazione, quando ancora
        lo scene non è stato impostato.

    06) Cerca il suddetto paziente nell'insieme dei bottoni appartenenti al
        gruppo per selezionarlo.

    07) Operazione annullata dall'utente.

    08) Assume per semplicità che il primo elemento contenga il valore più
        antecedente nel tempo tra quelli rilevati. Comunque poi la classe di
        stato riporta il valore ad un lunedì. Se i dati letti appartengono
        tutti alla stessa settimana tutto ok, altrimenti visualizza una
        qualsiasi delle settimane relative ai dati letti.

    09) Campo di testo volto a mostrare eventuali messaggi di errore all'utente.

    10) Metodo sì più lungo di una videata, ma composto in realtà dai corpi di
        tutti i listener presenti nell'applicazione, in notazione lambda.
        Per facilitare la navigazione cercare i seguenti segnalibri:
        
        10.1)   Variazione del paziente attuale nello stato.
        10.2)   Variazione della data attuale nello stato.
        10.3)   Variazione dei dati glicemici nello stato.
        10.4)   Variazione dell'elenco dei pazienti nello stato.
        10.5)   Variazione del valore del bottone selezionato nel gruppo
                dei pazienti.
        10.6)   Pressione del bottone di apertura file.
        10.7)   Pressione del bottone settimana indietro.
        10.8)   Pressione del bottone settimana avanti.
        10.9)   Variazione del valore del campo di testo.

    11) Le operazioni sulla lista dei pazienti possono essere solo di
        inserimento.
*/
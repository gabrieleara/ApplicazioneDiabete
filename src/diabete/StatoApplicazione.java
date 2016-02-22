package diabete;

import diabete.dati.*;
import diabete.util.CalendarioSettimanale;

import java.util.Collection;
import java.util.Date;
import javafx.beans.property.*;
import javafx.collections.*;

/**
 *
 * @author Gabriele Ara
 */
public class StatoApplicazione {
    private final ObservableList<String> pazienti;
    private final StringProperty pazienteAttuale;
    private final ObjectProperty<Date> dataAttuale;
    private final ObservableList<GlicemiaRilevata> glicemieRilevate;
    private final IntegerProperty[] statistiche;
    
    private static StatoApplicazione stato = null;
    
    public static void init(Cache c) {
        stato = new StatoApplicazione(c);
    }
    
    public static void init() {
        stato = new StatoApplicazione();
    }
    
    public static StatoApplicazione getInstance() throws NullPointerException {
        return stato;
    }
    
    private StatoApplicazione() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.pazienti = new SimpleListProperty<>(observableList);
        
        this.pazienteAttuale = new SimpleStringProperty("");
        
        ObservableList<GlicemiaRilevata> obsList = FXCollections.observableArrayList();
        this.glicemieRilevate = new SimpleListProperty<>(obsList);
        
        this.statistiche = new IntegerProperty[TipoStatistica.NUMERO_TIPI_STATISTICHE];
        
        for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
            this.statistiche[i] = new SimpleIntegerProperty(0);
        
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(new Date());
        cs.impostaLunedi();
        this.dataAttuale = new SimpleObjectProperty<>(cs.getTime());
        
    }
    
    private StatoApplicazione(Cache c) {
        this();
        
        this.pazienti.addAll(c.getPazienti());
        
        this.pazienteAttuale.set(c.getPazienteAttuale());
        
        glicemieRilevate.addAll(c.getDatiPerGrafico());
        
        for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
            this.statistiche[i].set(c.getStatistiche()[i]);
        
        this.dataAttuale.set(c.getDataAttuale());
        
    }

    public ObservableList<String> pazientiProperty() {
        return pazienti;
    }

    public StringProperty pazienteAttualeProperty() {
        return pazienteAttuale;
    }

    public ObjectProperty<Date> dataAttualeProperty() {
        return dataAttuale;
    }

    public ObservableList<GlicemiaRilevata> glicemieRilevateProperty() {
        return glicemieRilevate;
    }

    public IntegerProperty[] statisticheProperty() {
        return statistiche;
    }
    
    public void addPaziente(String paziente) {
        pazienti.add(paziente);
        setPazienteAttuale(paziente);
    }

    public void setPazienteAttuale(String paziente) {
        pazienteAttuale.set(paziente);
    }

    public void setDataAttuale(Date data) {
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.impostaLunedi();
        dataAttuale.set(cs.getTime());
    }

    public void setGlicemieRilevate(Collection<GlicemiaRilevata> dati) {
        glicemieRilevate.remove(0, glicemieRilevate.size());
        glicemieRilevate.addAll(dati);
    }

    public void setStatistica(int stat, TipoStatistica tipo) {
        statistiche[tipo.valore].set(stat);
    }

    public void setStatistiche(int[] statistiche) {
        for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
            this.statistiche[i].set(statistiche[i]);
    }
}

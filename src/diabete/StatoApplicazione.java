/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
	private final ObservableList<GlicemiaRilevata> datiPerGrafico;
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
	/*
	private StatoApplicazione() {
		this.pazienti = new SimpleListProperty<>();
		this.pazienteAttuale = new SimpleStringProperty();
		this.datiPerGrafico = new SimpleListProperty<>();
		this.statistiche = new IntegerProperty[7];
		this.dataAttuale = new SimpleObjectProperty<>();
	}
	*/
	private StatoApplicazione(Cache c) {
		this();
		
		this.pazienti.addAll(c.getPazienti());
		
		this.pazienteAttuale.set(c.getPazienteAttuale());
		
		datiPerGrafico.addAll(c.getDatiPerGrafico());
		
		for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
			this.statistiche[i].set(c.getStatistiche()[i]);
		
		this.dataAttuale.set(c.getDataAttuale());
		
	}
	
	private StatoApplicazione() {
		ObservableList<String> observableList = FXCollections.observableArrayList();
		this.pazienti = new SimpleListProperty<>(observableList);
		
		this.pazienteAttuale = new SimpleStringProperty("");
		
		ObservableList<GlicemiaRilevata> obsList = FXCollections.observableArrayList();
		this.datiPerGrafico = new SimpleListProperty<>(obsList);
		
		this.statistiche = new IntegerProperty[TipoStatistica.NUMERO_TIPI_STATISTICHE];
		
		for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
			this.statistiche[i] = new SimpleIntegerProperty(0);
		
		CalendarioSettimanale cs = new CalendarioSettimanale();
		cs.setTime(new Date());
		cs.lunedi();
		this.dataAttuale = new SimpleObjectProperty<>(cs.getTime());
		
	}

	public ObservableList<String> getPazienti() {
		return pazienti;
	}

	public StringProperty getPazienteAttuale() {
		return pazienteAttuale;
	}

	public ObjectProperty<Date> getDataAttuale() {
		return dataAttuale;
	}

	public ObservableList<GlicemiaRilevata> getDatiPerGrafico() {
		return datiPerGrafico;
	}

	public IntegerProperty[] getStatistiche() {
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
		cs.lunedi();
		dataAttuale.set(cs.getTime());
	}

	public void setDatiPerGrafico(Collection<GlicemiaRilevata> dati) {
		datiPerGrafico.remove(0, datiPerGrafico.size());
		datiPerGrafico.addAll(dati);
	}

	public void setStatistica(int stat, TipoStatistica tipo) {
		statistiche[tipo.valore].set(stat);
	}

	public void setStatistiche(int[] statistiche) {
		for(int i = 0; i < TipoStatistica.NUMERO_TIPI_STATISTICHE; ++i)
			this.statistiche[i].set(statistiche[i]);
	}
}

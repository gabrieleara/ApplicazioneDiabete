/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

import diabete.dati.StatisticaInsulina;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Gabriele Ara
 * @TODO: AnchorPane
 */
public class PannelloInsulina extends javafx.scene.layout.VBox {
	private StringProperty azioneRapida;
	private StringProperty azioneLenta;
	private StringProperty totale;

	public void aggiornaDati(int rapida, int lenta) {
		this.azioneRapida.set(Integer.toString(rapida));
		this.azioneLenta.set(Integer.toString(lenta));
		this.totale.set(Integer.toString(rapida + lenta));
	}
	
	public PannelloInsulina () {
		super();
		this.azioneRapida = new SimpleStringProperty();
		this.azioneLenta = new SimpleStringProperty();
		this.totale = new SimpleStringProperty();
		
		Elemento titolo = new Elemento("Insulina registrata giornaliera", "");
		
		titolo.setTitolo();
		
		Elemento rapida = new Elemento("Insulina ad azione rapida", "", "");
		rapida.getValoreProperty().bind(this.azioneRapida);
		
		Elemento lenta = new Elemento("Insulina ad azione lenta", "", "");
		lenta.getValoreProperty().bind(this.azioneLenta);
		
		Elemento somma = new Elemento("Insulina giornaliera totale", "");
		somma.getValoreProperty().bind(this.totale);
		
		super.getChildren().addAll(titolo, rapida, lenta, somma);
		
		// nodes.addAll(insulinaLogo, elementi);
	}

    public void aggiornaDati(StatisticaInsulina[] si) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

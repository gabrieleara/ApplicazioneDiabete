/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

import diabete.StatoApplicazione;
import diabete.dati.TipoStatistica;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Gabriele Ara
 * @TODO: AnchorPane
 */
public class PannelloInsulina extends javafx.scene.layout.VBox {
	private IntegerProperty azioneRapida;
	private IntegerProperty azioneLenta;
	private IntegerProperty totale;
	
	public PannelloInsulina () {
		super();
		this.azioneRapida = new SimpleIntegerProperty();
		this.azioneLenta = new SimpleIntegerProperty();
		this.totale = new SimpleIntegerProperty();

		IntegerProperty[] stat = StatoApplicazione.getInstance().getStatistiche();

		this.azioneRapida.bind(stat[TipoStatistica.INSULINA_RAPIDA.valore]);
		this.azioneLenta.bind(stat[TipoStatistica.INSULINA_LENTA.valore]);
		
		totale.bind(azioneRapida.add(azioneLenta));
		
		Elemento titolo = new Elemento("Insulina registrata giornaliera", "");
		
		titolo.setTitolo();
		
		Elemento rapida = new Elemento("Insulina ad azione rapida", "", "");
		rapida.getValoreProperty().bind(Bindings.convert(this.azioneRapida));
		
		Elemento lenta = new Elemento("Insulina ad azione lenta", "", "");
		lenta.getValoreProperty().bind(Bindings.convert(this.azioneLenta));
		
		
		Elemento somma = new Elemento("Insulina giornaliera totale", "");
		somma.getValoreProperty().bind(Bindings.convert(this.totale));
		
		super.getChildren().addAll(titolo, rapida, lenta, somma);
	}
}

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
public class PannelloGlucosio extends javafx.scene.layout.VBox {
	private final IntegerProperty valoreMedio;
	private final IntegerProperty valoreSopra;
	private final IntegerProperty valoreDentro;
	private final IntegerProperty valoreSotto;
	
	public PannelloGlucosio () {
		super();
		this.valoreSotto = new SimpleIntegerProperty();
		this.valoreDentro = new SimpleIntegerProperty();
		this.valoreSopra = new SimpleIntegerProperty();
		this.valoreMedio = new SimpleIntegerProperty();
		
		IntegerProperty[] stat = StatoApplicazione.getInstance().getStatistiche();
		
		valoreMedio.bind(stat[TipoStatistica.GLUCOSIO_MEDIO.valore]);
		valoreSopra.bind(stat[TipoStatistica.GLUCOSIO_SOPRA_INTERVALLO.valore]);
		valoreSotto.bind(stat[TipoStatistica.GLUCOSIO_SOTTO_INTERVALLO.valore]);
		
		IntegerProperty tot = new SimpleIntegerProperty(100);
		valoreDentro.bind(tot.subtract(valoreSopra.add(valoreSotto)));
		
		Elemento titolo = new Elemento("Glucosio medio", "mg/dL");
		titolo.getValoreProperty().bind(Bindings.convert(valoreMedio));
		
		titolo.setTitolo();
		
		Elemento sopra = new Elemento("% sopra l'intervallo", "%");
		sopra.getValoreProperty().bind(Bindings.convert(valoreSopra));
		
		Elemento dentro = new Elemento("% nell'intervallo", "%");
		dentro.getValoreProperty().bind(Bindings.convert(valoreDentro));
		
		Elemento sotto = new Elemento("% sotto l'intervallo", "%");
		sotto.getValoreProperty().bind(Bindings.convert(valoreSotto));
		
		super.getChildren().addAll(titolo, sopra, dentro, sotto);
	}
}

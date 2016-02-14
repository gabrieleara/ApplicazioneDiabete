/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.pannelli;

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
		
		/*
				ObservableList<Node> nodes = super.getChildren();
		
		ImageView insulinaLogo = new ImageView("http://i68.tinypic.com/118zo7m.png");
		insulinaLogo.setPreserveRatio(true);
		insulinaLogo.setFitHeight(60.);
		
		VBox elementi = new VBox();
		*/
		Elemento titolo = new Elemento("Insulina registrata giornaliera", "");
		
		titolo.setTitolo();
		
		Elemento azioneRapida = new Elemento("Insulina ad azione rapida", "", "");
		azioneRapida.getValoreProperty().bind(this.azioneRapida);
		
		Elemento azioneLenta = new Elemento("Insulina ad azione lenta", "", "");
		azioneLenta.getValoreProperty().bind(this.azioneLenta);
		
		Elemento totale = new Elemento("Insulina giornaliera totale", "");
		totale.getValoreProperty().bind(this.totale);
		
		super.getChildren().addAll(titolo, azioneRapida, azioneLenta, totale);
		
		// nodes.addAll(insulinaLogo, elementi);
	}
}

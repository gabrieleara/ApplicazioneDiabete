/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.pannelli;

import diabete.dati.GlicemiaRilevata;
import java.util.Collection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

/**
 *
 * @author Gabriele Ara
 * @TODO: AnchorPane
 */
public class PannelloGlucosioBasso extends javafx.scene.layout.VBox {
	private StringProperty eventi;
	private StringProperty durata;

	public void aggiornaDati(int eventi, int durata) {
		
		this.eventi.set(Integer.toString(eventi));
		this.durata.set(Integer.toString(durata));
	}
	
	public PannelloGlucosioBasso () {
		super();
		this.eventi = new SimpleStringProperty();
		this.durata = new SimpleStringProperty();
		
		// ObservableList<Node> nodes = super.getChildren();
		
		ImageView allarmeLogo = new ImageView("http://i68.tinypic.com/118zo7m.png");
		allarmeLogo.setPreserveRatio(true);
		allarmeLogo.setFitHeight(60.);
		
		/* VBox elementi = new VBox();*/ 
		Elemento titolo = new Elemento("Eventi di glucosio basso", "");
		titolo.getValoreProperty().bind(eventi);
		
		titolo.setTitolo();
		
		Elemento valore = new Elemento("Duarata media", "min");
		valore.getValoreProperty().bind(durata);
		
		super.getChildren().addAll(titolo, valore);
		
		// nodes.addAll(allarmeLogo, elementi);
	}
}
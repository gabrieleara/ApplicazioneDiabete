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

/**
 *
 * @author Gabriele Ara
 * @TODO: AnchorPane
 */
public class PannelloGlucosio extends javafx.scene.layout.VBox {
	private StringProperty valoreMedio;
	private StringProperty valoreSopra;
	private StringProperty valoreDentro;
	private StringProperty valoreSotto;

	public void aggiornaDati(int medio, int sopra, int sotto) throws IllegalArgumentException {
		if(sopra + sotto > 100)
			throw new IllegalArgumentException("Sopra e sotto superano 100!");
		
		int dentro = 100 - (sopra + sotto);
		valoreMedio.set(Integer.toString(medio));
		valoreSopra.set(Integer.toString(sopra));
		valoreDentro.set(Integer.toString(dentro));
		valoreSotto.set(Integer.toString(sotto));
		
	}
	
	public PannelloGlucosio () {
		super();
		this.valoreSotto = new SimpleStringProperty();
		this.valoreDentro = new SimpleStringProperty();
		this.valoreSopra = new SimpleStringProperty();
		this.valoreMedio = new SimpleStringProperty();
		
		/* ObservableList<Node> nodes = super.getChildren(); */
		/*
		ImageView glicemiaLogo = new ImageView("http://i68.tinypic.com/118zo7m.png");
		glicemiaLogo.setPreserveRatio(true);
		glicemiaLogo.setFitHeight(60.);
		*/
		
		
		/*
		VBox elementi = new VBox();
		*/
		Elemento titolo = new Elemento("Glucosio medio", "mg/dL");
		titolo.getValoreProperty().bind(valoreMedio);
		
		titolo.setTitolo();
		
		Elemento sopra = new Elemento("% sopra l'intervallo", "%");
		sopra.getValoreProperty().bind(valoreSopra);
		
		Elemento dentro = new Elemento("% nell'intervallo", "%");
		dentro.getValoreProperty().bind(valoreDentro);
		
		Elemento sotto = new Elemento("% sotto l'intervallo", "%");
		sotto.getValoreProperty().bind(valoreSotto);
		
		super.getChildren().addAll(titolo, sopra, dentro, sotto);
		
		/* nodes.addAll(glicemiaLogo, elementi); */
	}
}

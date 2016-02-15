/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

import diabete.AnalizzatoreDiabetico;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Gabriele Ara
 */
public class PannelloGlucosioBasso extends javafx.scene.layout.VBox {
	private StringProperty eventi;
	private StringProperty durata;

	public void aggiornaDati(int eventi, int durata) {
		
		this.eventi.set(Integer.toString(eventi));
		this.durata.set(Integer.toString(durata));
	}
	
	public void aggiornaDati(int[] stat) {
		aggiornaDati(
				stat[AnalizzatoreDiabetico.INDEX_NUMERO_EVENTI],
				stat[AnalizzatoreDiabetico.INDEX_DURATA_EVENTI]
				
				);
	}
	
	public PannelloGlucosioBasso () {
		super();
		this.eventi = new SimpleStringProperty();
		this.durata = new SimpleStringProperty();
		
		Elemento titolo = new Elemento("Eventi di glucosio basso", "");
		titolo.getValoreProperty().bind(eventi);
		
		titolo.setTitolo();
		
		Elemento valore = new Elemento("Duarata media", "min");
		valore.getValoreProperty().bind(durata);
		
		super.getChildren().addAll(titolo, valore);
	}
}
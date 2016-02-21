/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.interfaccia;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Gabriele Ara
 */
public class ElementoStatistico extends AnchorPane {
	private Label nome;
	private Label valore;
	private Label unita;
	
	private Pane riga;
	
	public ElementoStatistico(String nome, String unita) {
		super();
		
		ObservableList<Node> nodes = super.getChildren();
                
		setMinWidth(280);
        setMaxWidth(280);
		setMinHeight(44);
                
		getStyleClass().add("elemento");
		
		this.nome = new Label(nome);
		this.valore = new Label();
		this.unita = new Label(unita);
        
		this.riga = new Pane();
		
		this.riga.setMinWidth(230);
		this.riga.setMaxWidth(230);
		
		this.riga.setMinHeight(1);
		this.riga.setMaxHeight(1);
		
		this.nome.setMaxWidth(250);
		this.nome.setWrapText(true);
                
		AnchorPane.setTopAnchor(this.nome, 10.);
		AnchorPane.setBottomAnchor(this.nome, 10.);
		AnchorPane.setLeftAnchor(this.nome, 10.);
		
		AnchorPane.setTopAnchor(this.valore, 10.);
		AnchorPane.setBottomAnchor(this.valore, 10.);
		AnchorPane.setRightAnchor(this.valore, 55.);
		
		/* text align right */
		this.valore.setTextAlignment(TextAlignment.RIGHT);
		this.unita.setTextAlignment(TextAlignment.RIGHT);
                
		this.valore.getStyleClass().add("valore");
                
		AnchorPane.setTopAnchor(this.unita, 10.);
		AnchorPane.setBottomAnchor(this.unita, 10.);
		AnchorPane.setRightAnchor(this.unita, 0.);
		
		this.unita.setMaxWidth(50);
		this.unita.setMinWidth(50);
		
		this.unita.setWrapText(true);
		this.unita.getStyleClass().add("unita");
		
		AnchorPane.setBottomAnchor(this.riga, 0.);
		AnchorPane.setLeftAnchor(this.riga, 0.);
		
		this.riga.getStyleClass().add("riga");
		
		nodes.addAll(this.nome, this.valore, this.unita, this.riga);
	}
	
	public StringProperty getValoreProperty() {
		return valore.textProperty();
	}
	
	public ElementoStatistico(String nome, String unita, String imgUrl) {
		this(nome, unita);
		
		this.nome.setMaxWidth(190);
		AnchorPane.setLeftAnchor(this.nome, 40.);
		
		getStyleClass().add(imgUrl);
	}
	
	public void setTitolo() {
            this.getStyleClass().add("titolo");
            this.getStyleClass().remove("elemento");
			
			this.riga.setMinWidth(275);
			this.riga.setMaxWidth(275);
            
            if(this.unita.textProperty().get().length() < 1) 
                AnchorPane.setRightAnchor(this.valore, 20.);
	}
	
	public void setGrassetto() {
		String s = this.nome.getText().toUpperCase();
		this.nome.setText(s);
		this.nome.setMaxWidth(130);
		getStyleClass().add("grassetto");
	}
}

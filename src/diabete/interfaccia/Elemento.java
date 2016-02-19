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
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Gabriele Ara
 */
public class Elemento extends AnchorPane {
	private Label nome;
	private Label valore;
	private Label unita;
	
	public Elemento(String nome, String unita) {
		super();
		
		ObservableList<Node> nodes = super.getChildren();
                
		setMinWidth(320);
                setMaxWidth(320);
		setMinHeight(44);
                
                getStyleClass().add("elemento");
		
		this.nome = new Label(nome);
		this.valore = new Label();
		this.unita = new Label(unita);
                
                this.nome.setMaxWidth(140);
                this.nome.setWrapText(true);
                
		AnchorPane.setTopAnchor(this.nome, 0.);
		AnchorPane.setBottomAnchor(this.nome, 0.);
		AnchorPane.setLeftAnchor(this.nome, 0.);
		
		AnchorPane.setTopAnchor(this.valore, 0.);
		AnchorPane.setBottomAnchor(this.valore, 0.);
		AnchorPane.setRightAnchor(this.valore, 60.);
		
		/* text align right */
		this.valore.setTextAlignment(TextAlignment.RIGHT);
		this.unita.setTextAlignment(TextAlignment.RIGHT);
                
                this.valore.getStyleClass().add("valore");
                
		AnchorPane.setTopAnchor(this.unita, 0.);
		AnchorPane.setBottomAnchor(this.unita, 0.);
		AnchorPane.setRightAnchor(this.unita, 0.);
		this.unita.setMaxWidth(50);
		this.unita.setMinWidth(50);
		
		nodes.addAll(this.nome, this.valore, this.unita);
	}
	
	public StringProperty getValoreProperty() {
		return valore.textProperty();
	}
	
	public Elemento(String nome, String unita, String imgUrl) {
		this(nome, unita);
		
		/* Reduce size label */
	}
	
	public void setTitolo() {
            this.getStyleClass().add("titolo");
            this.getStyleClass().remove("elemento");
            
            if(this.unita.textProperty().get().length() > 0) {
                AnchorPane.setRightAnchor(this.valore, 100.);
                this.unita.setMinWidth(90.);
                this.unita.setMaxWidth(90.);
            } else
                AnchorPane.setRightAnchor(this.valore, 0.);
	}
	
	public void setGrassetto() {
		/* set proper class */
		/* all caps */
	}
}

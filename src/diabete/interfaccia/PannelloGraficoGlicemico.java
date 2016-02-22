package diabete.interfaccia;

import diabete.configurazione.GestoreConfigurazione;
import diabete.configurazione.TipoParametro;
import diabete.util.ConvertitoreTemporale;
import diabete.dati.GlicemiaRilevata;
import java.util.Collection;
import java.util.Set;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Gabriele Ara
 */
public class PannelloGraficoGlicemico extends javafx.scene.layout.VBox {
    final ObservableList<XYChart.Data<Number, Number>> datiAttuali;
    final ConvertitoreTemporale conv;
    final XYChart<Number, Number> lineChart;
    
    public PannelloGraficoGlicemico() {
        super();
        this.conv = new ConvertitoreTemporale();
        this.datiAttuali = FXCollections.observableArrayList();
        Label titolo = new Label("Valore medio del glucosio");
        titolo.getStyleClass().add("titolo-grafico");
        super.getChildren().add(titolo);
        
        ObservableList<XYChart.Series<Number, Number>> serie = FXCollections.observableArrayList();
        
        serie.add(new XYChart.Series<>("Concentrazione media oraria", datiAttuali));
        
        /* Asse verticale */
        NumberAxis valore = new NumberAxis();
        valore.setAutoRanging(false);
        valore.setLowerBound(0);
        valore.setUpperBound(350);
        valore.setTickUnit(70);
        valore.setMinorTickCount(3);

        /* Asse orizzontale */
        NumberAxis orario = new NumberAxis();
        orario.setTickLabelFormatter(conv);
        orario.setAutoRanging(false);
        orario.setLowerBound(ConvertitoreTemporale.LOWERBOUND);
        orario.setUpperBound(ConvertitoreTemporale.UPPERBOUND);
        orario.setTickUnit(ConvertitoreTemporale.ORA_IN_MILLIS * 2);
        orario.setMinorTickCount(4);
        
        lineChart = new LineChart(orario, valore, serie);
        lineChart.maxHeightProperty().bind(this.maxHeightProperty());
        
        
        Node back = lineChart.lookup(".chart-plot-background");
        String colore = (String) GestoreConfigurazione.ottieniParametro(TipoParametro.COLORE_SFONDO_GRAFICO);
        back.setStyle("-fx-background-color: " +colore);
        
        String color = (String) GestoreConfigurazione.ottieniParametro(TipoParametro.COLORE_LINEA_GRAFICO);
        
        Set<Node> lineNode = lineChart.lookupAll(".series0");  
        for (final Node line : lineNode) {  
            line.setStyle("-fx-stroke: " + color);  
        }
        
        super.getChildren().add(lineChart);
    }
    
    private void impostaNodoNascosto(Data<?,?> dato) {
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        dato.setNode(rect);
    }
    
    public void aggiornaDati(Collection<GlicemiaRilevata> dati) {
        Number tempo;
        datiAttuali.remove(0, datiAttuali.size());
        
        for(GlicemiaRilevata valore : dati) {
            tempo = conv.fromDate(valore.timestamp); // 01
            Data<Number, Number> data = new Data<>(tempo, valore.valore);
            impostaNodoNascosto(data);
            datiAttuali.add(data);
        }
        
        
    }
}

/*
    COMMENTI AL CODICE
    
    01) Poiché non esiste un asse di tipo data usa il convertitore temporale
        per passare da Date a long, ma non rappresenta il numero di millisecondi
        trascorsi dall' epoch, bensì il numero di millisecondi dalla mezzanotte
        dell'ora indicata, a partire dall'inizio della giornata.
*/
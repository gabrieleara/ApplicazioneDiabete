package diabete.interfaccia;

import diabete.StatoApplicazione;
import diabete.dati.TipoStatistica;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 *
 * @author Gabriele Ara
 */
public class PannelloGlucosioBasso extends javafx.scene.layout.VBox {
    private IntegerProperty eventi;
    private IntegerProperty durata;
    
    public PannelloGlucosioBasso () {
        super();
        
        this.eventi = new SimpleIntegerProperty();
        this.durata = new SimpleIntegerProperty();
        
        IntegerProperty[] stat = StatoApplicazione.getInstance().statisticheProperty();
        this.eventi.bind(stat[TipoStatistica.EVENTI_GLUCOSIO_BASSO.valore]);
        this.durata.bind(stat[TipoStatistica.DURATA_EVENTI_GLUCOSIO_BASSO.valore]);
        
        ElementoStatistico titolo = new ElementoStatistico("Eventi di glucosio basso", "");
        titolo.getValoreProperty().bind(Bindings.convert(this.eventi));
        titolo.setTitolo();
        
        ElementoStatistico valore = new ElementoStatistico("Duarata media", "min");
        valore.getValoreProperty().bind(Bindings.convert(this.durata));
        
        super.getChildren().addAll(titolo, valore);
    }
}
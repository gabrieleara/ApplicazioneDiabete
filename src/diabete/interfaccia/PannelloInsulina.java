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
public class PannelloInsulina extends javafx.scene.layout.VBox {
    private final IntegerProperty azioneRapida;
    private final IntegerProperty azioneLenta;
    private final IntegerProperty totale;
    
    public PannelloInsulina () {
        super();
        this.azioneRapida = new SimpleIntegerProperty();
        this.azioneLenta = new SimpleIntegerProperty();
        this.totale = new SimpleIntegerProperty();

        IntegerProperty[] stat = StatoApplicazione.getInstance().statisticheProperty();

        this.azioneRapida.bind(stat[TipoStatistica.INSULINA_RAPIDA.valore]);
        this.azioneLenta.bind(stat[TipoStatistica.INSULINA_LENTA.valore]);
        
        totale.bind(azioneRapida.add(azioneLenta));
        
        ElementoStatistico titolo = new ElementoStatistico("Insulina registrata giornaliera", "");
        
        titolo.setTitolo();
        
        ElementoStatistico rapida = new ElementoStatistico("Insulina ad azione rapida", "unità/giorno", "rapida");
        rapida.getValoreProperty().bind(Bindings.convert(this.azioneRapida));
        
        ElementoStatistico lenta = new ElementoStatistico("Insulina ad azione lenta", "unità/giorno", "lenta");
        lenta.getValoreProperty().bind(Bindings.convert(this.azioneLenta));
        
        
        ElementoStatistico somma = new ElementoStatistico("Insulina giornaliera totale", "unità/giorno");
        somma.getValoreProperty().bind(Bindings.convert(this.totale));
        
        somma.setGrassetto();
        
        super.getChildren().addAll(titolo, rapida, lenta, somma);
    }
}

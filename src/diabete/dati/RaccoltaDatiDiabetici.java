/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.dati;

import com.thoughtworks.xstream.XStream;
import static java.lang.Thread.sleep;
import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class RaccoltaDatiDiabetici {
    public final String paziente;
    public final GlicemiaRilevata[] rilevazioni;
    public final IniezioneInsulina[] iniezioni;

    public RaccoltaDatiDiabetici(String paziente,
            GlicemiaRilevata[] rilevazioni,
            IniezioneInsulina[] iniezioni) {
        this.paziente = paziente;
        this.rilevazioni = rilevazioni;
        this.iniezioni = iniezioni;
    }
    
    public static void main(String args[]) {
        GlicemiaRilevata[] glicemia = new GlicemiaRilevata[4];
        
        glicemia[0] = new GlicemiaRilevata(new Date(), 110);
        glicemia[1] = new GlicemiaRilevata(new Date(), 115);
        glicemia[2] = new GlicemiaRilevata(new Date(), 100);
        glicemia[3] = new GlicemiaRilevata(new Date(), 98);
        
        try {
            sleep(5000);
        } catch (InterruptedException ex) {
            
        }
        
        IniezioneInsulina[] iniezione = new IniezioneInsulina[4];
        
        iniezione[0] = new IniezioneInsulina(TipoInsulina.INSULINA_LENTA, new Date(), 4);
        iniezione[1] = new IniezioneInsulina(TipoInsulina.INSULINA_RAPIDA, new Date(), 1);
        iniezione[2] = new IniezioneInsulina(TipoInsulina.INSULINA_LENTA, new Date(), 2);
        iniezione[3] = new IniezioneInsulina(TipoInsulina.INSULINA_RAPIDA, new Date(), 5);
        
        RaccoltaDatiDiabetici rdd =
                new RaccoltaDatiDiabetici("Annalisa Gioli",
                        glicemia, iniezione);
        
        String str = new XStream().toXML(rdd);
        
        rdd = (RaccoltaDatiDiabetici) new XStream().fromXML(str);
        System.out.println("Tutto ok?");
        System.out.println(rdd);
    }
}

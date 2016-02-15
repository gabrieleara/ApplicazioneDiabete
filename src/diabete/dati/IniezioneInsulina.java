/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.dati;

import java.util.Date;

/**
 *
 * @author Gabriele Ara
 */
public class IniezioneInsulina {
    public final TipoInsulina tipo;
    public final Date timestamp;
    public final int unita;

    public IniezioneInsulina(TipoInsulina tipo, Date timestamp, int unita) {
        this.tipo = tipo;
        this.timestamp = timestamp;
        this.unita = unita;
    }
}

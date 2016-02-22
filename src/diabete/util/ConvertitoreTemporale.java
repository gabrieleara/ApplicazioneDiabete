package diabete.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.util.StringConverter;

/**
 *
 * @author Gabriele Ara
 */
public class ConvertitoreTemporale extends StringConverter<Number>{

    static final CalendarioSettimanale c = new CalendarioSettimanale();

    public static final long LOWERBOUND;
    public static final long UPPERBOUND;

    public static final long ORA_IN_MILLIS;

    static {
        c.set(1970, 0, 2, 0, 0, 0);
        c.resetTempoDelGiorno();
        LOWERBOUND = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_MONTH, 1);
        UPPERBOUND = c.getTimeInMillis();

        ORA_IN_MILLIS = 60 * 60 * 1000;
    }
    
    public Number fromDate(Date date) {
        // 01
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        c.set(1970, 0, 2);
        return c.getTimeInMillis();
    }
    
    @Override
    public String toString(Number millis) {
        c.setTimeInMillis(millis.longValue());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        
        return dateFormat.format(c.getTime());
    }
    
    @Override
    public Number fromString(String string) {
        return 0;
    }
    
}

/*
    COMMENTI AL CODICE

    01) In teoria dovrebbe ritornare il numero di millisecondi dalla mezzanotte.
        In pratica, farlo dava problemi e a me interessa che gli orari siano
        in corrispondenza 1:1 con un long e che l'ordinamento sia rispettato.
        Per il disegno del grafico è possibile impostare il lowerbound al
        valore che torna meglio, quindi una qualsiasi data va bene, basta usare
        sempre quella.
*/

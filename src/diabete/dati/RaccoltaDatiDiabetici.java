package diabete.dati;

import com.thoughtworks.xstream.XStream;
import diabete.util.CalendarioSettimanale;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // 01
        CalendarioSettimanale cs = new CalendarioSettimanale();
        CalendarioSettimanale fine = new CalendarioSettimanale();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        
        Date settimana = null;
        String pazienteSim = null;
        
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("args.txt"));
            Pattern p = Pattern.compile("(.+),(.+)");
            
            String s = new String(encoded);
            
            Matcher m = p.matcher(s);
            if(!m.matches())
                return;
            
            pazienteSim = m.group(1);
            
            settimana = df.parse(m.group(2));
            
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            return;
        }
        
        cs.setTime(settimana);
        cs.impostaDomenica();
        cs.impostaMezzanotte();
        fine.setTime(cs.getTime());
        
        cs.setTime(settimana);
        cs.impostaLunedi();
        
        cs.resetTempoDelGiorno();
        
        System.out.println("Paziente:\t" + pazienteSim);
        System.out.println("Inizio:\t\t" + cs.getTime());
        System.out.println("Fine:\t\t" + fine.getTime());
        
        ArrayList<GlicemiaRilevata> gc = new ArrayList<>();
        
        int glicemiaPrecedente = 100;
        int raggio = 25;
        int variazione;
        int casuale;
        
        boolean decrescita = false;
        boolean crescita = false;
        
        // 02
        while(cs.before(fine)) {
            // 02.1
            casuale = (int) Math.round(Math.random() * raggio);
            
            // 02.2 - 02.3 - 02.4
            if(crescita)
                variazione = casuale / 4 + 5;
            else if(decrescita)
                variazione = -casuale;
            else
                variazione = casuale - 13;
            
            glicemiaPrecedente += variazione;
            
            // 02.5
            if(decrescita && glicemiaPrecedente < 90)
                decrescita = false;
            
            // 02.6
            if(glicemiaPrecedente > 270)
                decrescita = true;
            
            // 02.7
            if(crescita && glicemiaPrecedente > 160)
                crescita = false;
            
            // 02.8
            if(glicemiaPrecedente < 50)
                crescita = true;
            
            // 02.9
            glicemiaPrecedente += variazione;
            
            gc.add(new GlicemiaRilevata(cs.getTime(), glicemiaPrecedente));
            
            cs.add(Calendar.MINUTE, 15);
        }
        
        GlicemiaRilevata[] glicemia = gc.toArray(new GlicemiaRilevata[gc.size()]);
        
        cs.setTime(settimana);
        cs.impostaLunedi();
        
        ArrayList<IniezioneInsulina> ii = new ArrayList<>();
        
        // 03
        for(int i = 0; i < 7; ++i) {
            ii.add(new IniezioneInsulina(TipoInsulina.INSULINA_LENTA, cs.getTime(), (int) Math.floor(Math.random() * 3 + 6)));
            
            if(Math.random() > 0.2)
                ii.add(new IniezioneInsulina(TipoInsulina.INSULINA_RAPIDA, cs.getTime(), (int) Math.floor(Math.random() * 3 + 2)));
            else
                ii.add(new IniezioneInsulina(TipoInsulina.INSULINA_RAPIDA, cs.getTime(), 2));
            
            cs.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        IniezioneInsulina[] iniezione = ii.toArray(new IniezioneInsulina[ii.size()]);
        
        RaccoltaDatiDiabetici rdd =
                new RaccoltaDatiDiabetici(pazienteSim,
                        glicemia, iniezione);
        
        String str = new XStream().toXML(rdd);
        
        try (
                FileOutputStream fos = new FileOutputStream("datidiprova.xml");
        ) {
            fos.write("<?xml version=\"1.0\"?>".getBytes("UTF-8")); // XML header
            byte[] bytes = str.getBytes("UTF-8");
            fos.write(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

/*
    COMMENTI AL CODICE
    
    01) Questo programma legge da file i suoi argomenti e genera una settimana
        di dati diabetici in formato XML, che in seguito scrive su file.
        E' necessario perché se volessi inserire a mano dei dati glicemici per
        anche una sola settimana e tali da essere un minimo attendibili dovrei
        riuscire a tirare fuori dal cilindro 4 * 7 * 24 = 672 rilevazioni
        glicemiche. Troppi dati per i miei gusti.
        Il file contiene una stringa del tipo "Nome Paziente, yyyy/MM/dd";
        la data indica la settimana di riferimento.

    02) L'algoritmo di generazione procede così:

        02.1)   Si genera una variazione pseudo casuale, in modo che il nuovo
                valore non scosti troppo dal valore generato per i 15 minuti
                precedenti.
        
        02.2)   Se ci si trova in una fase di crescita (vedi 02.8) la
                variazione viene ridotta a un quarto e sommata a 5; in questo
                modo si è sicuri che il valore sommato sarà un minimo superiore
                al precedente, ma comunque l'andamento sarà abbastanza lento.

        02.3)   Se ci si trova in una fase di decrescita (vedi 02.6) la
                variazione viene sottratta per intero al valore precedente,
                per simulare una riduzione rapida del valore della glicemia.

        02.4)   Se non ci si trova in nessuna fase allora la variazione assume
                valori compresi tra -13 e +12.

        02.5)   La fase di decrescita termina una volta raggiunto un valore
                inferiore a 90.

        02.6)   Se la glicemia supera il valore 270, in genere il paziente
                assume una dose di insulina ad azione rapida oppure esegue
                una attività fisica, in modo da ridurre la concentrazione di    
                glucosio nel sangue. Per simulare questo comportamento qualsiasi
                variazione generata da questo punto in avanti sarà negativa
                fino al raggiungimento di una soglia accettabile (02.5),
                secondo quanto espresso in 02.3 .
                
                NOTA: Per semplicità, l'insulina assunta giornalmente dal
                paziente viene calcolata in modo del tutto indipendente
                da quanto emerso in quesa simulazione.

        02.7)   La fase di crescita termina una volta raggiunto un valore
                superiore a 160.

        02.8)   Se la glicemia scende sotto il valore 50, il paziente comincia
                a manifestare sintomi come capogiro, ecc. In questi casi, 
                a parte casi molto gravi, in genere il paziente assume dello
                zucchero o alimenti zuccherosi (succhi di frutta, bibite
                zuccherate, ecc.) in modo da riportare la concentrazione di
                glucosio entro un valore acettabile. Questo processo è comunque
                abbastanza lento. Per simulare questo comportamento, qualsiasi
                variazione generata da questo punto in avanti sarà positiva
                fino al raggiungimento di una soglia accettabile (02.7),
                secondo quanto espresso in 02.2 .

        02.9)   Questo raddppio eseguito DOPO i controlli aiuta ad aggiungere
                quel minimo di variabilità all'algoritmo propria del processo
                che si vuole descrivere: in questo modo gli andamenti di
                crescita e decrescita possono terminare anche prima del
                raggiungimento della soglia indicata, secondo un meccanismo del
                tutto casuale, il che non è poi così insolito nel mondo reale.
        
        I valori generati con questo algoritmo presentano il giusto grado di
        variabilità e le caratteristiche principali proprie del processo
        modellato, seppur essendone una grossa semplificazione, visto che
        questo algoritmo non tiene conto di fattori come i pasti, l'attività
        fisica, il riposo, ecc., che influiscono non poco su questo processo.

    03) Generazione di valori casuali di insulina registrata in una giornata.
        I valori così ottenuti sono abbastanza veritieri, anche se non tengono
        conto del risultato dell'algoritmo al punto 02 .
*/

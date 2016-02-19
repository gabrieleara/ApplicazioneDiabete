/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
		
		CalendarioSettimanale cs = new CalendarioSettimanale();
		
		CalendarioSettimanale fine = new CalendarioSettimanale();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		
		Date settimana = null;
		String pazienteSim = null;
		
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("args.txt"));
			Pattern p = Pattern.compile("(.+),(.+)");
			
			String s = new String(encoded);
			
			System.out.println(s);
			Matcher m = p.matcher(s);
			if(!m.matches())
				return;
			System.out.println(m.group(0));
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			
			pazienteSim = m.group(1);
			
			settimana = df.parse(m.group(2));
			
			System.out.println(settimana);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(settimana);
		
		cs.setTime(settimana);
		cs.domenica();
		cs.setMezzanotte();
		fine.setTime(cs.getTime());
		
		System.out.println("Fine:\t" + fine.getTime());
		
		cs.setTime(settimana);
		cs.lunedi();
		
		cs.resetTempoDelGiorno();
		
		System.out.println("Inizio:\t" + cs.getTime());
		
		ArrayList<GlicemiaRilevata> gc = new ArrayList<>();
		
		int glicemiaPrecedente = 100;
		int raggio = 25;
		int variazione;
		int casuale;
		
		boolean decrescita = false;
		boolean crescita = false;
		
		while(cs.before(fine)) {
			casuale = (int) Math.round(Math.random() * raggio);
			
			if(crescita)
				variazione = casuale / 4 + 5;
			else if(decrescita)
				variazione = -casuale;
			else
				variazione = casuale - 13;
			
			glicemiaPrecedente += variazione;
			
			if(decrescita && glicemiaPrecedente < 90)
				decrescita = false;
			
			if(glicemiaPrecedente > 270)
				decrescita = true;
			
			if(crescita && glicemiaPrecedente > 160)
				crescita = false;
			
			if(glicemiaPrecedente < 50)
				crescita = true;
				
			
			glicemiaPrecedente += variazione;
			
			gc.add(new GlicemiaRilevata(cs.getTime(), glicemiaPrecedente));
			
			//System.out.println(cs.getTime() + "\t" + glicemiaPrecedente);
			cs.add(Calendar.MINUTE, 15);
		}
		
		
		GlicemiaRilevata[] glicemia = gc.toArray(new GlicemiaRilevata[gc.size()]);
		
		cs.setTime(settimana);
		cs.lunedi();
		
		ArrayList<IniezioneInsulina> ii = new ArrayList<>();
		
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
			fos.write("<?xml version=\"1.0\"?>".getBytes("UTF-8")); //write XML header, as XStream doesn't do that for us
			byte[] bytes = str.getBytes("UTF-8");
			fos.write(bytes);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
}

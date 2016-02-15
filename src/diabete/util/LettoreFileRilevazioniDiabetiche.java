/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import com.thoughtworks.xstream.XStream;
import diabete.dati.*;
import java.io.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriele Ara
 */
public class LettoreFileRilevazioniDiabetiche {
    private LettoreFileRilevazioniDiabetiche() {
    }
    
    public RaccoltaDatiDiabetici leggiDaFile(String nomefile) throws SAXException, IOException {
        boolean res = ValidatoreXML.validaXML(nomefile, "ril_diabete.xsd");
        
        if(!res)
            return null;
        
        try(
            FileInputStream fis = new FileInputStream(nomefile);
            DataInputStream dis = new DataInputStream(fis);
            ) {
            return (RaccoltaDatiDiabetici) new XStream().fromXML(dis.readUTF());
        }
    }
}

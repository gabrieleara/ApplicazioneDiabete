package diabete;

import diabete.dati.*;
import diabete.util.*;

import com.mysql.jdbc.Connection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Gabriele Ara
 */
public class GestoreDatiDiabetici {
    
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private GestoreDatiDiabetici() {
        
    }
    
    private static String costruisciInserimentoGlicemie(String paziente, GlicemiaRilevata[] gr) {
        // 01
        
        String queryBase = " INSERT INTO glicemia(paziente, tempo, valore)"
                + " VALUES ";
        
        for(int i = 0; i < gr.length; ++i) {
            queryBase += "('" + paziente + "',"
                    + "'" + df.format(gr[i].timestamp) + "',"
                    + gr[i].valore + ")";
            if(i < gr.length - 1)
                queryBase += ",";
        }
        
        queryBase += "ON DUPLICATE KEY UPDATE valore = VALUES(valore)";
        return queryBase;
    }
    
    private static void inserisciInsulina(String paziente, IniezioneInsulina ii)  throws SQLException {
        // 02
        
        String query =
                " INSERT INTO insulina(paziente, tempo, tipo, unita)"
                + " VALUES ('" + paziente + "',"
                + "'" + df.format(ii.timestamp) + "',"
                + ii.tipo.valore + ","
                + ii.unita + ")";
        
        try(
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            st.executeUpdate(query);
        }
    }
    
    public static void salvaDatiRilevati(RaccoltaDatiDiabetici rdd) throws SQLException {
        String paziente = rdd.paziente;
        
        String controllo =
                "SELECT count(*) AS num FROM paziente WHERE paziente = '"
                + paziente + "'";
        
        String nuovoPaziente =
                "INSERT INTO paziente VALUES ('" + paziente + "')";
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(controllo);
            if(rs.next())
                if( rs.getInt("num") < 1)
                    st.executeUpdate(nuovoPaziente); // 03
            
            String query = costruisciInserimentoGlicemie(paziente, rdd.rilevazioni);
            st.executeUpdate(query);
            
            for(IniezioneInsulina ii : rdd.iniezioni)
                inserisciInsulina(paziente, ii);
        }
    }
    
    private static java.util.Date recuperaSettimanaLimite(String paziente, boolean ultima) throws SQLException {
        java.util.Date data;
        
        String query = " SELECT "
                + ((ultima) ? "max(tempo)" : "min(tempo)") + " AS valore "
                + " FROM glicemia"
                + " WHERE paziente = '" + paziente + "'";
        
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            data = rs.getTimestamp("valore");
            if(data == null)
                throw new SQLException("Errore nella data.");
        }
        
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        
        cs.lunedi();
        cs.resetTempoDelGiorno();
        
        return cs.getTime();
    }
    
    public static java.util.Date recuperaUltimaSettimana(String paziente) throws SQLException {
        return recuperaSettimanaLimite(paziente, true);
    }
    
    public static java.util.Date recuperaPrimaSettimana(String paziente) throws SQLException {
        return recuperaSettimanaLimite(paziente, false);
    }
    
    private static java.util.Date cambiaSettimana(String paziente, java.util.Date settimana, boolean avanti) throws SQLException {
        java.util.Date data;
        
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(settimana);
        if(avanti) {
            cs.domenica();
            cs.setMezzanotte();
        } else {
            cs.lunedi();
            cs.resetTempoDelGiorno();
        }
        
        settimana = cs.getTime();
        
        String query = " SELECT "
                + ((avanti) ? "min(tempo)" : "max(tempo)") + " AS valore "
                + " FROM glicemia"
                + " WHERE paziente = '" + paziente + "'"
                + " AND tempo " + ((avanti) ? '>' : '<')
                + " '" + df.format(settimana) + "'";
        
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            data = rs.getTimestamp("valore");
        }
        
        if(data == null)
            return (avanti)
                    ? recuperaUltimaSettimana(paziente)
                    : recuperaPrimaSettimana(paziente);
        
        cs.setTime(data);
        
        cs.lunedi();
        cs.resetTempoDelGiorno();
        
        return cs.getTime();
    }
    
    public static java.util.Date cambiaSettimanaAvanti(String paziente, java.util.Date settimana) throws SQLException {
        return cambiaSettimana(paziente, settimana, true);
    }
    
    public static java.util.Date cambiaSettimanaIndietro(String paziente, java.util.Date settimana) throws SQLException {
        return cambiaSettimana(paziente, settimana, false);
    }
    
    public static int[] recuperaInsulinaSettimanale(String paziente, java.util.Date data) throws SQLException {
        int[] insulina = new int[2];
        
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();
        
        String dataFormattata = df.format(cs.getTime());
        
        cs.domenica();
        cs.setMezzanotte();
        
        String fineDataFormattata = df.format(cs.getTime());
        
        String query = " SELECT tipo, AVG(totale) AS media FROM ("
                + " SELECT DATE(tempo) AS giorno, tipo, SUM(insulina.unita) as totale"
                + " FROM insulina"
                + " WHERE paziente = '" + paziente + "'"
                + " AND tempo BETWEEN '" + dataFormattata + "'"
                + " AND '" + fineDataFormattata + "'"
                + " GROUP BY giorno, tipo ) AS T"
                + " GROUP BY tipo ORDER BY tipo ASC";
        
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                if(rs.getInt("tipo") == TipoInsulina.INSULINA_LENTA.valore)
                    insulina[0] = rs.getInt("media");
                else
                    insulina[1] = rs.getInt("media");
            }
        }
        
        return insulina;
    }
    
    public static ArrayList<GlicemiaRilevata> recuperaGlicemiaSettimanale(
            String paziente,
            java.util.Date data) throws SQLException {
        ArrayList<GlicemiaRilevata> glicemia = new ArrayList<>();
        
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();
        
        String dataFormattata = df.format(cs.getTime());
        
        cs.domenica();
        cs.setMezzanotte();
        
        String fineDataFormattata = df.format(cs.getTime());
        
        String query = "SELECT * FROM glicemia"
                + " WHERE paziente = '" + paziente + "'"
                + " AND tempo BETWEEN '" + dataFormattata + "'"
                + " AND '" + fineDataFormattata + "'"
                + " ORDER BY tempo ASC";
        
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                glicemia.add(
                        new GlicemiaRilevata(
                                rs.getTimestamp("tempo"),
                                rs.getInt("valore")));
            }
        }
        
        return glicemia;
    }
    
}

/*
    COMMENTI AL CODICE
    
    01) Data la grande mole di dati potenzialmente da inserire, col fine di
        ridurre al minimo i tempi di attesa dell'utente viene eseguita una
        sola query di inserimento multiplo invece di un gran numero di query
        di inserimento separate. Questo permette di ridurre notevolmente i
        tempi di attesa, poiché riduce di molto l'overhead di connessione
        al database.

    02) Per questi dati invece non è necessario fare un inserimento multiplo,
        anche multipli inserimenti vanno più che bene.

    03) Se il paziente non è inserito nel database va registrato.
*/
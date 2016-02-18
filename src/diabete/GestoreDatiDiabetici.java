package diabete;


import diabete.dati.*;
import diabete.util.*;

import com.mysql.jdbc.Connection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriele Ara
 */
public class GestoreDatiDiabetici {

    private GestoreDatiDiabetici() {

    }
	
	private static void inserisciGlicemia(String paziente, GlicemiaRilevata gr)  throws SQLException {
		String query =
				  " INSERT INTO glicemia(paziente, tempo, valore)"
				+ " VALUES ('" + paziente + "',"
				+ "'" + df.format(gr.timestamp) + "',"
				+ gr.valore + ")";
		try(
			Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
		) {
			st.executeUpdate(query);
		}
	}
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static void inserisciInsulina(String paziente, IniezioneInsulina ii)  throws SQLException {
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

    public static boolean salva(RaccoltaDatiDiabetici rdd) throws SQLException {
        String paziente = rdd.paziente;
		
		String controllo =
				"SELECT count(*) AS num FROM paziente WHERE paziente = '" + paziente + "'";
		
		String nuovoPaziente =
				"INSERT INTO paziente VALUES ('" + paziente + "')";
		try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(controllo);
			if(rs.next())
				if( rs.getInt("num") < 1)
					st.executeUpdate(nuovoPaziente);
        }
		
		for(GlicemiaRilevata gr : rdd.rilevazioni)
			inserisciGlicemia(paziente, gr);
		
		for(IniezioneInsulina ii : rdd.iniezioni)
			inserisciInsulina(paziente, ii);
		
		
        return true;
    }

    public static java.util.Date ultimaSettimana(String paziente) throws SQLException {
        java.util.Date ultimaData = null;

        String query =
            " SELECT max(tempo) AS massimo FROM glicemia"
          + " WHERE paziente = '" + paziente + "'";
		
        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);
			rs.next();
            ultimaData = rs.getTimestamp("massimo");
			if(ultimaData == null)
				throw new SQLException("Nessuna data massima.");
        }
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(ultimaData);

        cs.lunedi();
        cs.resetTempoDelGiorno();

        return cs.getTime();
    }

    public static int[] insulinaSettimanale(String paziente, java.util.Date data) throws SQLException {
		int[] insulina = new int[2];

        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();

        String dataFormattata = df.format(cs.getTime());

        // System.out.println("Inizio dati " + dataFormattata);

        cs.domenica();
        cs.setMezzanotte();

        String fineDataFormattata = df.format(cs.getTime());

        // System.out.println("Fine dati " + fineDataFormattata);

        String query =
            " SELECT tipo, AVG(totale) AS media FROM ("
          + " SELECT DATE(tempo) AS giorno, tipo, SUM(insulina.unita) as totale"
          + " FROM insulina"
          + " WHERE paziente = '" + paziente + "'"
          + " AND tempo BETWEEN '" + dataFormattata + "'"
            + " AND '" + fineDataFormattata + "'"
          + " GROUP BY giorno, tipo ) AS T"
          + " GROUP BY tipo ORDER BY tipo ASC";
		
        // System.out.println("Executing query: ");
        // System.out.println(query);

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



    public static ArrayList<GlicemiaRilevata> glicemiaSettimanale(String paziente, java.util.Date data) throws SQLException {
        ArrayList<GlicemiaRilevata> glicemia = new ArrayList<>();

        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();

        String dataFormattata = df.format(cs.getTime());

        // System.out.println("Inizio dati " + dataFormattata);

        cs.domenica();
        cs.setMezzanotte();

        String fineDataFormattata = df.format(cs.getTime());

        // System.out.println("Fine dati " + fineDataFormattata);

        String query = "SELECT * FROM glicemia"
                        + " WHERE paziente = '" + paziente + "'"
                        + " AND tempo BETWEEN '" + dataFormattata + "'"
                                + " AND '" + fineDataFormattata + "'"
                        + " ORDER BY tempo ASC";

        // System.out.println("Executing query: ");
        // System.out.println(query);

        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                glicemia.add( new GlicemiaRilevata(rs.getTimestamp("tempo"), rs.getInt("valore")));
            }
        }

        return glicemia;
    }
	
}

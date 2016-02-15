package diabete;


import diabete.dati.*;
import diabete.util.*;

import com.mysql.jdbc.Connection;

import java.sql.*;
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

    public static boolean salva(RaccoltaDatiDiabetici rdd) {
        /* TODO: salva nel database */
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
            ultimaData =  rs.getTimestamp("massimo");
        }
        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(ultimaData);

        cs.lunedi();
        cs.resetTempoDelGiorno();

        return cs.getTime();
    }

    public static StatisticaInsulina[] insulinaSettimanale(String paziente, java.util.Date data) throws SQLException {
        StatisticaInsulina[] insulina = new StatisticaInsulina[2];

        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();

        String dataFormattata = df.format(cs.getTime());

        System.out.println("Inizio dati " + dataFormattata);

        cs.domenica();
        cs.setMezzanotte();

        String fineDataFormattata = df.format(cs.getTime());

        System.out.println("Fine dati " + fineDataFormattata);

        String query =
            " SELECT tipo, AVG(totale) AS media FROM ("
          + " SELECT DATE(tempo) AS giorno, tipo, SUM(unita) as totale"
          + " FROM insulina"
          + " WHERE paziente = '" + paziente + "'"
          + " AND tempo BETWEEN '" + dataFormattata + "'"
            + " AND '" + fineDataFormattata + "'"
          + " GROUP BY giorno, tipo ) AS T"
          + " GROUP BY tipo ORDER BY tipo ASC";

        System.out.println("Executing query: ");
        System.out.println(query);

        try (
            Connection co = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/diabete", "root", "");
            Statement st = co.createStatement();
        ) {
            ResultSet rs = st.executeQuery(query);

            TipoInsulina tipo = TipoInsulina.fromInt(rs.getInt("tipo"));
            int unita = rs.getInt("unita");
            insulina[0] = new StatisticaInsulina(tipo, unita);

            rs.next();

            tipo = TipoInsulina.fromInt(rs.getInt("tipo"));
            unita = rs.getInt("unita");
            insulina[1] = new StatisticaInsulina(tipo, unita);
        }

        return insulina;
    }



    public static ArrayList<GlicemiaRilevata> glicemiaSettimanale(String paziente, java.util.Date data) throws SQLException {
        ArrayList<GlicemiaRilevata> glicemia = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        CalendarioSettimanale cs = new CalendarioSettimanale();
        cs.setTime(data);
        cs.lunedi();
        cs.resetTempoDelGiorno();

        String dataFormattata = df.format(cs.getTime());

        System.out.println("Inizio dati " + dataFormattata);

        cs.domenica();
        cs.setMezzanotte();

        String fineDataFormattata = df.format(cs.getTime());

        System.out.println("Fine dati " + fineDataFormattata);

        String query = "SELECT * FROM glicemia"
                        + " WHERE paziente = '" + paziente + "'"
                        + " AND tempo BETWEEN '" + dataFormattata + "'"
                                + " AND '" + fineDataFormattata + "'"
                        + " ORDER BY tempo ASC";

        System.out.println("Executing query: ");
        System.out.println(query);

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

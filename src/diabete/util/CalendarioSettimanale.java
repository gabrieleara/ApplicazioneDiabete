/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diabete.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Gabriele Ara
 */
public class CalendarioSettimanale extends GregorianCalendar {

    public CalendarioSettimanale() {
	super();
    }

    public CalendarioSettimanale(TimeZone zone) {
	super(zone);
    }

    public CalendarioSettimanale(Locale aLocale) {
	super(aLocale);
    }

    public CalendarioSettimanale(TimeZone zone, Locale aLocale) {
	super(zone, aLocale);
    }

    public CalendarioSettimanale(int year, int month, int dayOfMonth) {
	super(year, month, dayOfMonth);
    }

    public CalendarioSettimanale(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
	super(year, month, dayOfMonth, hourOfDay, minute);
    }

    public CalendarioSettimanale(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
	super(year, month, dayOfMonth, hourOfDay, minute, second);
    }
    
    public void resetTempoDelGiorno() {
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
    }
	
	public void setMezzanotte() {
		set(HOUR_OF_DAY, 23);
		set(MINUTE, 59);
		set(SECOND, 59);
		set(MILLISECOND, 999);
	}
    
    public void lunedi() {
		set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }
	
	public void domenica() {
		set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    }
}

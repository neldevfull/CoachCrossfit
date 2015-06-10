package br.com.coachcrossfit.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class Util {

	/**
	 * Parses type Date (util) to String
	 */
	public String parseDateToString(Date date){
		Locale locale = new Locale("pt", "BR");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	
	/**
	 * Used to generate the hashed password
	 */
	public String hashPass(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest(pass.getBytes("UTF-8"));
		 
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
		  hexString.append(String.format("%02X", 0xFF & b));
		}
		
		String senha = hexString.toString();
		return senha;
	}
	
	/**
	 * Calculates amount of weeks and creates end date
	 */
	public Date calculateWeek(Date date, long periodCycle){		
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
						
		localDate = localDate.plusWeeks(periodCycle);
		// Substracts one day by default
		localDate = localDate.minusDays(1L);
		date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
	
	/**
	 * Sum amount of days from a date
	 */
	public Date sumDays(Date date, int days){
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		
		localDate = localDate.plusDays(days);
		date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
}

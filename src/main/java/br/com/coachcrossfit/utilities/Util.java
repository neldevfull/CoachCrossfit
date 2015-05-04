package br.com.coachcrossfit.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

	/**
	 * Parseia tipo Date (util) para String
	 */
	public String parseDateToString(Date date){
		Locale locale = new Locale("pt", "BR");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	
	/**
	 * Utilizado para gerar o hash para senha
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
}

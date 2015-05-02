package br.com.coachcrossfit.beans;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.xml.bind.ValidationException;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.utilities.Util;

public class UserBean {
	
	protected Util util = new Util();
	
	/**
	 *  Gera a chave que será utilizado para a senha inicial
	 * @throws ValidationException 
	 */
	public String generateKey(Date dateBirth, Connection connection) throws NoSuchFieldException, SecurityException, SQLException {
		
		int idStudent = 0;
		// Faz o parse de Date para String
		String dateStr = this.util.parseDateToString(dateBirth);
		// Monta a chave que será utilizada como senha inicial
		String[] dateVect = dateStr.split("/");
		dateStr = dateVect[2].substring(2) + dateVect[1] + dateVect[0];
		
		Class<Student> stuRef = Student.class;
		Field field = stuRef.getDeclaredField("idStudent");
		Field[] fields = { field };
		
		GenericsDAO generics = new GenericsDAO(connection);
		ResultSet result = generics.select(fields, "tb_student");

		while (result.next()) {
			idStudent = result.getInt("idStudent");
		}

		idStudent++;
		generics.closeStatement();
		
		return dateStr + idStudent;
	}

}

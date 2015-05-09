package br.com.coachcrossfit.beans.user;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.ValidationException;

import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.utilities.Util;

public class UserBean {
	
	protected Util util = new Util();
	protected Reflections<User> userRef = new Reflections<User>();;
	protected Class<User> userClas = User.class;
	protected String table = "tb_user";	
	protected List<Object> values = new ArrayList<Object>();
	protected List<Object> valuesConditions = new ArrayList<Object>();
	private Field field;
	private Field filedA;
	private Field[] fields;
		
	/**
	 *  Gera a chave que será utilizado para a senha inicial
	 * @throws ValidationException 
	 */
	public String generateKey(Date dateBirth) throws NoSuchFieldException, SecurityException, SQLException {		
		// Faz o parse de Date para String
		String dateStr = this.util.parseDateToString(dateBirth);
		// Monta a chave que será utilizada como senha inicial
		String[] dateVect = dateStr.split("/");
		dateStr = dateVect[2].substring(2) + dateVect[1] + dateVect[0];		
		return dateStr;
	}
	
	/**
	 * Seleciona usuário para login
	 */
	public User selectLoginUSer(User user, GenericsDAO generics) throws NoSuchFieldException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {		
		this.fields = this.userClas.getDeclaredFields();
		
		this.field  = this.userClas.getDeclaredField("emailUser");
		this.filedA = this.userClas.getDeclaredField("passUser"); 
		Field[] fieldsConditions = { this.field, this.filedA };
		
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		
		this.valuesConditions.add(user.getEmailUser());
		String pass = util.hashPass(user.getPassUser());
		this.valuesConditions.add(pass); 
		
		ResultSet result = generics.select(this.fields, fieldsConditions, this.valuesConditions, this.table);
		
		user = this.resultSetUser(result);
		
		result.close();
		generics.closeStatement();
		
		return user;								
	}
	
	/**
	 * Todos os registros de determinado usuário	 
	 */
	public User resultSetUser(ResultSet result) throws SQLException{
		User user = new User();
		while(result.next()){
			user.setIdUser(result.getInt("idUser"));
			user.setEmailUser(result.getString("emailUser"));
			user.setPassUser(result.getString("passUser"));
			user.setTypeUser(result.getInt("typeUser"));
			user.setStatusUser(result.getInt("statusUser"));
		}
		return user;
	}

}

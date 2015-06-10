package br.com.coachcrossfit.tests; 

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.utilities.Util;

public class MethodsTests {
	
	private List<Object> values;

	public void insertCoach() throws NoSuchAlgorithmException,
			UnsupportedEncodingException, SQLException {

		try (Connection connection = new ConnectionFactory().getConnection()) {
			Util util = new Util();
			User user = new User();
//			Coach coach = new Coach();

			// Cadastro usuário
			user.setEmailUser("coach@matheuscoach.com.br");
			user.setPassUser(util.hashPass("880202"));
			user.setStatusUser(1);
			user.setTypeUser(1);
			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);

			// Cria GenericsDAO e Insert Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Busca os campos de UserBean menos o id da Classe
			Reflections<User> ref = new Reflections<User>();
			Class <User> userRef = User.class;		
			Field[] fields = ref.getAllLessId(userRef);
			
			// Atribui a lista os valores de usuário
			this.values = new ArrayList<Object>();
			this.values.add(user.getEmailUser());
			// Faz o hash da senha
			String pass = util.hashPass(user.getPassUser());		
			this.values.add(pass);
			this.values.add(user.getTypeUser());
			this.values.add(user.getStatusUser());																							
																	
			generics.insert(fields, this.values, "tb_user");
			
			// Commit para que haja atomicidade dos dados
			connection.commit();

//			// Cadastro coach
//			coach.setCrefCoach("098780-G/SP");
//			coach.setNameCoach("Matheus");
//			coach.setUser(user);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

//	public void testJoinDuble() {
//		try (Connection connection = new ConnectionFactory().getConnection()) {
//			GenericsDAO generics = new GenericsDAO(connection);
//
//			List<String> fields = new ArrayList<String>();
//			fields.add("*");
//
//			List<String> fieldsConditions = new ArrayList<String>();
//			//fieldsConditions.add("idUser");
//
//			List<Object> valuesConditions = new ArrayList<Object>();
//			//valuesConditions.add(53);
//
//			List<String> joinsConditions = new ArrayList<String>();
//			joinsConditions.add("idUser");
//
//			String join = " INNER JOIN ";
//			String tableA = "tb_user";
//			String tableB = "tb_student";
//			String conditions = " ORDER BY idStudent DESC ";
//
//			ResultSet result = generics.joinDouble(fields, fieldsConditions,
//					valuesConditions, joinsConditions, join, tableA, tableB,
//					conditions);
//
//			while (result.next()) {
//				System.out.println(result.getString("nameStudent"));
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public void passGer() {
		Date dateBirth = new Date();
		Locale locale = new Locale("pt", "BR");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
				locale);
		String dateStr = dateFormat.format(dateBirth);
		String[] dateVect = dateStr.split("/");
		dateStr = dateVect[2].substring(2) + dateVect[1] + dateVect[0];
		System.out.println(dateStr);
	}

	public void idStudentSearch() throws NoSuchFieldException,
			SecurityException {
		int idStudent = 0;
		Class<Student> stuRef = Student.class;
		Field field = stuRef.getDeclaredField("idStudent");
		Field[] fields = { field };

		try (Connection connection = new ConnectionFactory().getConnection()) {
			GenericsDAO generics = new GenericsDAO(connection);
			ResultSet result = generics.select(fields, "tb_student", " ORDER BY idUser DESC");

			while (result.last()) {
				idStudent = result.getInt("idStudent");
			}

			System.out.println(idStudent);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}

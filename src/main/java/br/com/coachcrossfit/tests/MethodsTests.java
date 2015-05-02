package br.com.coachcrossfit.tests;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;

public class MethodsTests {
	
	public void testJoinDuble(){
		try(Connection connection = new ConnectionFactory().getConnection()){			
			GenericsDAO generics = new GenericsDAO(connection);
			
			List<String> fields = new ArrayList<String>();
			fields.add("*");
			
			List<String> fieldsConditions = new ArrayList<String>();
			fieldsConditions.add("idUser");
			
			List<Object> valuesConditions = new ArrayList<Object>();
			valuesConditions.add(53);
			
			List<String> joinsConditions = new ArrayList<String>();			
			joinsConditions.add("idUser");
			
			String join = " INNER JOIN ";
			String tableA = "tb_user";
			String tableB = "tb_student";
									
			ResultSet result = generics.joinDuble(fields, fieldsConditions, valuesConditions, joinsConditions, join, tableA, tableB);			
			
			while(result.next()){
				System.out.println(result.getString("nameStudent"));
			}
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void passGer(){
		Date dateBirth = new Date();
		Locale locale = new Locale("pt", "BR");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		String dateStr = dateFormat.format(dateBirth);
		String[] dateVect = dateStr.split("/"); 
		dateStr = dateVect[2].substring(2)+dateVect[1]+dateVect[0];
		System.out.println(dateStr);
	}

	public void idStudentSearch() throws NoSuchFieldException, SecurityException{
		int idStudent = 0;
		Class<Student> stuRef = Student.class;
		Field field = stuRef.getDeclaredField("idStudent");
		Field[] fields = {field};
		
		try(Connection connection = new ConnectionFactory().getConnection()){			
			GenericsDAO generics = new GenericsDAO(connection);
			ResultSet result = generics.select(fields, "tb_student");			
			
			while(result.last()){
				idStudent = result.getInt("idStudent");
			}
			
			System.out.println(idStudent);
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
				
	}
}

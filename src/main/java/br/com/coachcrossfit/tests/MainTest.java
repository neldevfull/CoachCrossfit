package br.com.coachcrossfit.tests;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.ValidationException;

import br.com.coachcrossfit.beans.StudentBean;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;

public class MainTest {
	public static void main(String[] args){
							
		MethodsTests mt = new MethodsTests(); 
		mt.testJoinDuble();
		
		/*
		Reflections<StudentBean> ref = new Reflections<StudentBean>();
		Class<StudentBean> studRef = StudentBean.class;
		
		List<String> compareClass = new ArrayList<String>();
		compareClass.add("userbean");
		
		Field[] fields = ref.getAllLessId(studRef, compareClass);
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		*/
		
		//mt.passGer();
		/*
		try{
			mt.idStudentSearch();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}*/
		
//		Pattern pattern;
//		Matcher matcher;
//		String dateStr = "24/11/1985";  
//		
//		pattern = Pattern.compile("[a-zA-Z]");
//		matcher = pattern.matcher(dateStr);
//
//		// Verificação
//		if (!matcher.find()){
//			pattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}");
//			matcher = pattern.matcher(dateStr);
//			
//			if(!matcher.find())
//				System.out.println("Não encontrou data");
//			else
//				System.out.println("Data correta");		
//		}
//		else{
//			System.out.println("Data incorreta");
//		}
		
		
	} 
	
	public static void testeException() throws ValidationException{
		throw new ValidationException("Falha");
	}
	
//	public void armazena(){
//		try{
//			Date date = new Date(); 
//			UserBean user = new UserBean("pedrinho3@", 1, date); 
//			
//			StudentControl studentC = new StudentControl(); 		
//			studentC.studentCad(1/*idCoach*/, "PEDRINHO", 80,
//							    date, 80, "", user);
//		}
//		catch(Exception e){
//			System.out.println(e.getMessage());
//			// IMPLEMENTAR AS MENSAGENS OUTPUT QUE SERÃO APRESENTADAS NA TELA
//		}
//		
	}
	

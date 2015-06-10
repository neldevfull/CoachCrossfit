package br.com.coachcrossfit.tests;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.bind.ValidationException;

import br.com.coachcrossfit.beans.student.StudentBean;
import br.com.coachcrossfit.models.Cycle;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.utilities.GetFields;
import br.com.coachcrossfit.utilities.Util;

public class MainTest {
	public static void main(String[] args){
		
		Util util = new Util();		
		Date date = new Date();
		System.out.println(date);
		date = util.calculateWeek(date, Long.parseLong("4"));
		System.out.println(date);
		
		/*
		Instant instant = Instant.ofEpochMilli(new Date().getTime());
		LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		
		System.out.println(localDate);
		
		System.out.println(localDate.plusWeeks(4));
		
		for(int i = 0; i < 4; i++){
			localDate = localDate.plusDays(7);
			System.out.println(localDate);
		}
		
		
		Field[] fields = Cycle.class.getDeclaredFields();
		
		for (Field field : fields) {
			if(field.isAnnotationPresent(GetFields.class)){
				System.out.println(field.getName());
			}
		}
		
		Cycle cycle = new Cycle();
		cycle.setIdCoach(1);
		
		System.out.println(cycle.getIdCoach());
		
		
		 Pattern pattern;
		 Matcher matcher;
		
		String textArea = "Nelson $%%¨&"
				+ "M"; 
		// Verificação		
		pattern = Pattern.compile("[a-zA-Z âÂãÃáÁàÀêÊéÉèÈíÍìÌôÔõÕóÓòÒúÚùÙûÛçÇ]{3,150}"); 
		matcher = pattern.matcher(textArea);
		
		if(!matcher.matches()) 
			System.out.println("Erro 1");
							
		MethodsTests mt = new MethodsTests(); 
		
		try{
			//mt.idStudentSearch();
			//mt.testJoinDuble();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		/*
		
		//mt.testJoinDuble();
		
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
	

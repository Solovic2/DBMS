package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.util.regex.Pattern;

public class DB implements Database{
	
	private Map<String,DocumentBuilder> DBS;
	
	
	
	/********************************Singleton Design Pattern********************************/
	private static DB instance = new DB();
	
	private DB() {}
	
	public DB get_instance() {
		return instance;
	}
	
	
	/********************************create database********************************/
	
	public String createDatabase(String databaseName, boolean dropIfExists)  {	
		if(dropIfExists) {
			try {
				executeStructureQuery("drop database "+ databaseName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			executeStructureQuery("create database "+ databaseName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "DB absolute path";
		
	}
	
	/********************************create\drop database\table********************************/
	public boolean executeStructureQuery(String query) 	throws java.sql.SQLException{
		if(query.contains("create database")){
			String DBname = query.substring(16,query.indexOf(";")-1);
			DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
			try {
				DBS.put( DBname,documentfactory.newDocumentBuilder()) ;
			
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DocumentBuilder k = documentfactory.newDocumentBuilder();
			k.reset();
			
			
		}
		
		
		
		
		else if(query.contains("drop database")) {
			String DBname = query.substring(14,query.indexOf(";")-1);
			if(DBS.containsKey(DBname)) {
				DBS.get(DBname).reset();
				DBS.remove(DBname);
			}
			
			
			
		}
		
		
		else if(query.contains("create table")) {
			String table_name = query.substring(13,query.indexOf("(")-1);
			String Cols = query.substring(query.indexOf("(")+1, query.indexOf(")"));
			String[] cols = Cols.split(",");
			String[] cols_names = new String[cols.length];
			String[] cols_dtype = new String[cols.length];
			int count = 0; 
			for(String i : cols_dtype) {
				String temp = "(\\s+)(\\w)(\\s+)(\\w+)";
				Pattern pattern = Pattern.compile(temp);
				java.util.regex.Matcher matcher = pattern.matcher(i);
				if (matcher.matches()) {
					cols_names[count] = matcher.group(2);
					cols_dtype[count++]  = matcher.group(4);
					}
			}
			DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
			
			Document document = DBS.get(key).newDocument();
			
			
			
			Element root = document.createElement(tagName);
			document.appendChild(root);
			
			for(int i = 0 ; i < count ; i++) {
				Element temp = document.createElement(cols_names[i]);
				document.appendChild(temp);
				temp.appendChild(document.createAttribute(cols_dtype[i]));	
			}
			document.removeChild(document);
			
		}
		
		
		else if(query.contains("drop table")) {
			String table_name = query.substring(11,query.indexOf(";")-1);
			DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
		
			
			
		}
	
		
		
		
		
		
		return false;
	}
	/********************************select from table********************************/
	public Object[][] executeQuery(String query) throws java.sql.SQLException{
		
		
		
		
		
		
		
		
		
		
		
		return null;
		
	}
	/********************************update\insert\delete data in table********************************/
	public int executeUpdateQuery(String query) throws java.sql.SQLException{
		
		
		
		
		
		
		
		return 0;
	}


}

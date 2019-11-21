package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.regex.Pattern;

public class DB implements Database{
	public final static String XMLFilePath = "C:\\Users\\momen\\Desktop";
	
	private Map<String, Map<String, File>> DBS;
	private Stack<String> database_names = null;
	
	/********************************Singleton Design Pattern********************************/
	private static DB instance = new DB();
	
	private DB() {}
	
	public static DB get_instance() {
		return instance;
	}
	
	
	/********************************create database********************************/
	
	public String createDatabase(String databaseName, boolean dropIfExists)  {	
		if(dropIfExists || database_names.contains(databaseName)) {
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
			DBS.put( DBname,null) ;
			database_names.push(DBname);
				return true;
		}
		
		
		
		
		else if(query.contains("drop database")) {
			String DBname = query.substring(14,query.indexOf(";")-1);
			if(DBS.containsKey(DBname)) {
				Map<String,File> tables = DBS.get(DBname); 
				File[] files = (File[]) tables.values().toArray();
				for(int i = 0 ; i < tables.size() ; i++) {
					files[i].delete();
				}
				DBS.remove(DBname);
				database_names.removeElementAt(database_names.indexOf(DBname));
				return true;
			}
			
		}
		
		
		
		else if(query.contains("create table")) {
			if(!database_names.isEmpty()) {
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
				DocumentBuilder documentbuilder = null;
				try {
					documentbuilder = documentfactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document document = documentbuilder.newDocument();
				
				Element root = document.createElement(table_name);
				document.appendChild(root);
				
				for(int i = 0 ; i < count ; i++) {
					Element temp = document.createElement(cols_names[i]);
					document.appendChild(temp);
					temp.appendChild(document.createAttribute(cols_dtype[i]));	
				}
				
				
				
				TransformerFactory transformerfactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					transformer = transformerfactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DOMSource domsource = new DOMSource(document);
				File table_FILE = new File(XMLFilePath+"./"+ table_name +".xml");
				StreamResult streamresult = new StreamResult(table_FILE);
				
				
				try {
					transformer.transform(domsource,streamresult);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				DBS.get(database_names.peek()).put(table_name, table_FILE);
				return true;
			}
			
		}
		
		
		else if(query.contains("drop table")) {
			if(!database_names.isEmpty()) {
				String table_name = query.substring(11,query.indexOf(";")-1);
				if(DBS.get(database_names.peek()).containsKey(table_name)) {
					DBS.get(database_names.peek()).get(table_name).delete();
					DBS.get(database_names.peek()).remove(table_name);
					return true;
				}
			}	
		}
	
		
		
		
		
		
		return false;
	}
	/********************************select from table********************************/
	public Object[][] executeQuery(String query) throws java.sql.SQLException{
		String table_name = null;
		String[] cols_names = null;
		DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentbuilder = null;
		try {
			documentbuilder = documentfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = null;
		try {
			 document = documentbuilder.parse(DBS.get(database_names.peek()).get(table_name));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
 		
		
		
		
		
		
		
		
		
		
		
		
		return null;
		
	}
	/********************************update\insert\delete data in table********************************/
	public int executeUpdateQuery(String query) throws java.sql.SQLException{
		
		
		
		
		
		
		
		return 0;
	}


}
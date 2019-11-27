package eg.edu.alexu.csd.oop.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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

	public class DB implements Database{
	private final static String XMLFilePath = "D:\\DBMS\\databases";
	private  File file =  new File("D:\\DBMS\\DB_PATHES.txt");	
	private Check ch = Check.get_instance();
	@SuppressWarnings("unused")
	   
	private XMLValidation XML_validator = XMLValidation.get_instance(); 
	private static Map<String, Map<String, File>> DBS = new HashMap<String, Map<String,File>>();
	private static Map<String, String> table_DB = new HashMap<String, String>();
	private static Stack<String> database_names = new Stack<String>();
	/************************move to file**********************************/
	public File movetofile(File file1 , File file2) throws IOException {
		
	      BufferedReader brx = new BufferedReader(new FileReader(file1));
	      PrintWriter pwx = new PrintWriter(new FileWriter(file2));
	      String line = null;
	      while ((line = brx.readLine()) != null) {
	        
	          pwx.println(line);
	          pwx.flush();
	        
	      }
	      pwx.close();
	      brx.close();
	   
	      return file2;
	     
		
	}
	/******************************reload **********************************/
	public void reload() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.contains("Name: ")) {
		    	   Map<String, File>temp=new HashMap<String, File>();
		    	  String foldername= line.substring(6, line.length());
		    	  database_names.push(foldername);
		    	 String path=  br.readLine();
		    	 String p = new String();
		    if (path.contains(XMLFilePath)) {
				p=XMLFilePath+"\\"+foldername;
			}
		    else {
		    	p=path.substring(6, path.length());
		    }
		    	//  System.out.println("folder name: "+foldername);
		    	  File folder = new File(p);
		    	  File[] listOfFiles = folder.listFiles();
		    	//  System.out.println("files: ");
		    	  for (File file : listOfFiles) {
		    	      if (file.isFile()) {
		    	    	  if(file.getName().contains("xml")) {
		    	    		  table_DB.put(file.getName().substring(0, file.getName().length()-4), foldername);
		    	    	  }
		    	    	 temp.put(file.getName(), file);
		    	    	
		    	  //        System.out.println(file.getName());
		    	      }
		    	  }
		    	  DBS.put(foldername, temp);
		       }
		    }
		}
		// just for test 
	System.out.println("map1");
		DBS.forEach((key, value) -> System.out.println(key + " : " + value));
		System.out.println("map2");
		table_DB.forEach((key, value) -> System.out.println(key + " : " + value));	
		System.out.println("stack");
		String values = Arrays.toString(database_names.toArray());
        System.out.println(values);
		
	}
	/********************************Singleton Design Pattern********************************/
	private static DB instance = new DB();
	
	private DB() {}
	
	public static DB get_instance() {
		return instance;
	}



/********************************check the first word***************************/
	
	public void mainRegex(String s) throws SQLException {
		s=s.toLowerCase();
		if(!s.contains(" ")){
			System.err.println("ERROPR!! Wrong Syntax");
			return;
		}
		String r=s.substring(0, s.indexOf(" "));
		
		switch(r) {
		case "drop": executeStructureQuery(s);
			break;
		case "select": executeQuery(s);
			break;
		case "delete": executeUpdateQuery(s);
			break;
		case "insert": executeUpdateQuery(s);
			break;
		case "update": executeUpdateQuery(s);
			break;
		case "create": executeStructureQuery(s);
			break;	
		default: System.err.println("ERROR !! Wrong Input");
		    break;
		}	
	}
	
	/*******************************condition calculator*******************************************/
	public boolean condition_calculator (String col_val , String val , char operator) {
		if(operator == '=') {
			return col_val.equals(val);
		}
		else if(operator == '>') {
			return Integer.parseInt(col_val) > Integer.parseInt(val);
		}
		else if(operator == '<') {
			return Integer.parseInt(col_val) < Integer.parseInt(val);
		}
		
		return false;
	}
	
	/********************************create database********************************/
	
	public String createDatabase(String databaseName, boolean dropIfExists)  {	
		if(databaseName.contains(System.getProperty("file.separator")) && databaseName.charAt(1) != ':') {
			databaseName = XMLFilePath+"\\"+databaseName;
		}
		if(dropIfExists && database_names.contains(databaseName)) {
			try {
				executeStructureQuery("drop database "+ databaseName.substring(databaseName.lastIndexOf("\\")+1));
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
		if(databaseName.contains(System.getProperty("file.separator"))) {
			return databaseName;
		}

		return XMLFilePath+"\\"+databaseName;
		
	}
	/********************************create\drop database\table********************************/
	@SuppressWarnings("static-access")
	public  boolean executeStructureQuery(String query) 	throws java.sql.SQLException{
		
		
		/**********************************create database***************************************/
		if(query.contains("create database")){
			String[] str =ch.createcheck(query);
			if(str==null) return false;
			String DBname= str[0];
			if(!database_names.contains(DBname)) {
			@SuppressWarnings("unused")
			DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
			String path = XMLFilePath+"\\"+DBname;
			boolean save =false;
			//create folder in case of path given
			if(DBname.contains(System.getProperty("file.separator"))) {
				 File newFolder = new File(DBname);
		        boolean created =  newFolder.mkdir();
		        if(created) {
		        	path=DBname;
		        	save=true;
		        	DBname=DBname.substring(DBname.lastIndexOf("\\")+1);
		        }
			}
			//create folder in case of name given
			else {
			 File newFolder = new File(XMLFilePath+"\\"+DBname);
		        boolean created =  newFolder.mkdir(); 
		        if(created) {
		        	save=true;   
		        }

			}
			// save database name and its path in DB_PATHES.txt
			if(save==true) {
			 try {

		            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		            BufferedWriter bw = new BufferedWriter(fw);
		            bw.write("Name: "+DBname);
		            bw.newLine();
		            bw.write("PATH: "+path);
		            bw.newLine();	
		            bw.close();
		            Map<String, File> intialize = new HashMap<String, File>()  ;
		            intialize.put("s52", new File(XMLFilePath));
		            //add to stack and map
		            DBS.put( DBname,intialize) ;
		            database_names.push(DBname);
		            System.out.println("DataBase>> "+DBname+" << is Created and Saved");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				
			}
			
				return save;
			}
			else {
				
				System.err.println("Same Database Are Createad");
				return false ;
			}
		}
		
		
		
		/*****************************************drop database********************************************/
		else if(query.contains("drop database")) {
			String DBname =ch.dropscheck(query);
			if(DBname==null)return false;
			if(database_names.contains(DBname)) {
				boolean done=false;
				try {
					String px = new String();
				String linen =new String();
					 BufferedReader brxx = new BufferedReader(new FileReader(file));
					while ((linen = brxx.readLine()) != null) {
						String x = linen;
						if(x.equals("Name: "+DBname)) {
							linen = brxx.readLine();
							px=linen;
							break;
						}

					}
					 String pp = new String();
					    if (px.contains(XMLFilePath)) {
							pp= XMLFilePath+"\\"+DBname;
						}
					    else {
					    	pp=px.substring(6,px.length());
					    }
					    brxx.close();
					//delete folder
					File index = new File(pp);
					// to delete the included files
					delete_dir d =new delete_dir();
					done=d.deleteDirectory(index);
					index.delete();
					
				      File tempFile = new File(file.getAbsolutePath().replaceAll(".txt", ".tmp" ));
				      BufferedReader brx = new BufferedReader(new FileReader(file));
				      PrintWriter pwx = new PrintWriter(new FileWriter(tempFile));
				      String line = null;
				      while ((line = brx.readLine()) != null) {
				        if (!line.trim().equals("Name: "+DBname)&&!line.trim().equals("PATH: "+pp)) {
				          pwx.println(line);
				          pwx.flush();
				        }
				      }
				      pwx.close();
				      brx.close();
				   
				      file.delete();
				   tempFile.renameTo(file);
				    
				     
				     
				  	// remove from stack and map
						DBS.remove(DBname);
						database_names.removeElementAt(database_names.indexOf(DBname));
						System.out.println("DataBase>> "+DBname+" << is Dropped and Deleted");
				     // done=true;
				    }
				    catch (FileNotFoundException ex) {
				      ex.printStackTrace();
				    }
				    catch (IOException ex) {
				      ex.printStackTrace();
				    }
		
				return done;
			}
			
		}
		
		/**************************create table**************************/
		else if(query.contains("create table")) {
			if(!database_names.isEmpty()) {
				String []cr=ch.createcheck(query);
				if(cr==null)return false;
				String table_name = cr[0];
				
				String[] cols_names = new String[(cr.length-1)/2];
				String[] cols_dtype = new String[(cr.length-1)/2];
				int j=1;
				for(int i=0;i<cols_names.length;i++) {
					cols_names[i]=cr[j];
					cols_dtype[i]=cr[j+1];
					j+=2;
				}
				for(int i = 0 ; i < cols_dtype.length ; i++) {
					if(cols_dtype[i].equals("varchar")) {
						cols_dtype[i] = "string";
					}
					else {
						cols_dtype[i] = "integer";
					}
					
				}
				DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentbuilder = null;
				String before_path = new String();
				String read =new String();
				String new_path=new String();
					 BufferedReader buffr;
					try {
						buffr = new BufferedReader(new FileReader(file));
						try {
							while ((read = buffr.readLine()) != null) {
								String x = read;
								if(x.equals("Name: "+database_names.peek())) {
									try {
										read = buffr.readLine();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									before_path=read;
									break;
								}
								

							}
							if (before_path.contains(XMLFilePath)) {
						    	new_path= XMLFilePath+"\\"+database_names.peek();
							}
						    else {
						    	new_path=before_path.substring(6,before_path.length());
						    }
						   buffr.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
									/**************************************creating xml file************************************/
				try {
					documentbuilder = documentfactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Document document = documentbuilder.newDocument();
				
				Element root = document.createElement(table_name);
				document.appendChild(root);
				
				/*Element table = document.createElement("ZeroRow");
				root.appendChild(table);
				
				for(int i = 0 ; i < cols_names.length ; i++) {
					Element temp = document.createElement(cols_names[i]);
					temp.setAttribute("type", cols_dtype[i]);
					table.appendChild(temp);
					
				}*/
				
				
				TransformerFactory transformerfactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					transformer = transformerfactory.newTransformer();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DOMSource domsource = new DOMSource(document);
				File table_FILE = new File(new_path+"\\"+ table_name +".xml");
				StreamResult streamresult = new StreamResult(table_FILE);
				
				
				try {
					transformer.transform(domsource,streamresult);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*************************creating schema file******************************************/
				Document schema = documentbuilder.newDocument();
				root = schema.createElement("xs:schema");
				root.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
				schema.appendChild(root);
				
				
				
				Element element = schema.createElement("xs:element");
				element.setAttribute("name", table_name);
				root.appendChild(element);
				
				Element complextype1 = schema.createElement("xs:complexType");
				element.appendChild(complextype1);
				
				Element sequence1 = schema.createElement("xs:sequence");
				complextype1.appendChild(sequence1);
				
				Element row = schema.createElement("xs:element");
				row.setAttribute("name", "id");
				row.setAttribute("type", "row_type");
				row.setAttribute("minOccurs", "0");
				row.setAttribute("maxOccurs", "unbounded");
				sequence1.appendChild(row);
				
				
				
				
				Element complextype2 = schema.createElement("xs:complexType");
				complextype2.setAttribute("name", "row_type");
				root.appendChild(complextype2);
				
				
				Element sequence2 = schema.createElement("xs:sequence");
				complextype2.appendChild(sequence2);
				
				for(int i = 0 ; i < cols_names.length ; i++) {
					Element temp = schema.createElement("xs:element");
					temp.setAttribute("name", cols_names[i]);
					temp.setAttribute("type", "xs:"+cols_dtype[i]);
					temp.setAttribute("minOccurs", "0");
					temp.setAttribute("maxOccurs", "unbounded");
					sequence2.appendChild(temp);
				}
				domsource = new DOMSource(schema);
				File SchemaFile = new File(new_path+"\\"+ table_name +".xsd");
				streamresult = new StreamResult(SchemaFile);
				
				
				try {
					transformer.transform(domsource,streamresult);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				table_DB.put(table_name, database_names.peek());
				DBS.get(database_names.peek()).put(table_name, table_FILE);
				System.out.println("Table>> "+table_name+" << is Created ");
				return true;
			}
			
		}
		
		
		else if(query.contains("drop table")) {
			if(!database_names.isEmpty()) {
				String table_name = ch.dropscheck(query);
				if(table_name == null) return false;
				if(DBS.get(database_names.peek()).containsKey(table_name)) {
					DBS.get(table_DB.get(table_name)).get(table_name+".xml").delete();
					File xsd = new File(DBS.get(table_DB.get(table_name)).get(table_name+".xml").getAbsolutePath().replace("xml", "xsd"));
					xsd.delete();
					DBS.get(table_DB.get(table_name)).remove(table_name);
					table_DB.remove(table_name);
					return true;
				}
			}	
		}
	
		
		
		
		
		System.err.println("ERROR!! Wrong Input");
		return false;
	}
	/********************************select from table********************************/
	@SuppressWarnings("null")
	public Object[][] executeQuery(String query) throws java.sql.SQLException{
		String table_name = null;
		String[] cols_names = null;
		String[][] Selected;
		String[] condition = null;
		ArrayList<Integer> Selected_rows = new ArrayList<Integer>(0);;
		String []cr = ch.selectcheck(query);
		if(cr==null)return null;
		table_name=cr[0];
		if(query.contains("where")) {
			condition = new String[3];
			condition[0]=cr[cr.length-3];
			condition[1]=cr[cr.length-2];
			condition[2]=cr[cr.length-1];
			cols_names = new String[(cr.length-4)];
		}else {
			cols_names = new String[(cr.length-1)];
		}
		
		
		
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
			 document = documentbuilder.parse(DBS.get(table_DB.get(table_name)).get(table_name+".xml"));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NodeList rows = document.getElementsByTagName("id");
		
		
		for(int i = 0 ; i < rows.getLength() ; i++) {
			if(condition == null || condition_calculator(document.getElementsByTagName(condition[0]).item(i).getTextContent(), condition[2], condition[1].charAt(0))) {
				Selected_rows.add(i);		
			}
		}
		
		if(query.contains("*")){
			Selected = new String[Selected_rows.size()][rows.item(0).getChildNodes().getLength()];
			int c = 0;
			for(int i : Selected_rows) {
				for(int j = 0 ; j < rows.item(0).getChildNodes().getLength() ; j++){
					Selected[c++][j] = rows.item(i).getChildNodes().item(j).getTextContent();
				}
			}
		}
		
		else {
			int v=1;
			for(int i=0;i<cols_names.length;i++) {
				cols_names[i]=cr[v];
				v++;
				
			}
			
			Selected = new String[Selected_rows.size()][cols_names.length]; 
			int c = 0;
			for(int i : Selected_rows) {
				for(int j = 0 ; j < cols_names.length ; j++){
					Selected[c][j] = document.getElementsByTagName(cols_names[c]).item(j).getTextContent(); 
				}c++;
			}
			
		}
		
		for(int i = 0 ; i < Selected.length ; i++) {
			for(int j = 0 ; j < Selected[0].length ; j++) {
				System.out.print(Selected[i][j]+" ");
					}System.out.print("\n");
		}
		
		
		return Selected;
		
	}
	/********************************update\insert\delete data in table********************************/
	public int executeUpdateQuery(String query) throws java.sql.SQLException{
	if(!database_names.isEmpty()) {
		String order = null;
		String table_name = null;
		String []cr = null;
		if(query.contains("update")) {
				cr=ch.updatecheck(query);
			order="update";
			if(cr==null)return 0;
			table_name=cr[0];
		}else if(query.contains("insert")) {
				cr=ch.insertcheck(query);
			order="insert";
			if(cr==null)return 0;
			table_name=cr[0];
		}else if(query.contains("delete")) {
				cr=ch.deletecheck(query);
			order="delete";
			if(cr==null)return 0;
			table_name=cr[0];
		}
		
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
			document = documentbuilder.parse(DBS.get(table_DB.get(table_name)).get(table_name+".xml"));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if (order.equals("insert")) {
			String[] cols_names = new String[(cr.length-1)/2];
			String[] cols_vals = new String[(cr.length-1)/2];
			int k=1;
			for(int i=0;i<cols_names.length;i++) {
				cols_names[i]=cr[k];
				cols_vals[i]=cr[k+1];
				k+=2;
			}
							
			Element root = document.getDocumentElement();
			
			Element NEW = document.createElement("id");
			root.appendChild(NEW);
			Document schema =null;
			try {
				schema = documentbuilder.parse(new File(DBS.get(table_DB.get(table_name)).get(table_name+".xml").getAbsolutePath().replace("xml", "xsd")));
			} catch (SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(int i = 0; i < cols_names.length ; i++) {
				Element temp = document.createElement(cols_names[i]);
				NEW.appendChild(temp);
				temp.setTextContent(cols_vals[i]);
				
			}
			
			
			/*for(int i = 0 ; i < cols_names.length ; i++) {
				Element temp = document.createElement(cols_names[i]);
				NEW.appendChild(temp);
				temp.setTextContent(cols_vals[i]);
			}*/	
			
			TransformerFactory transformerfactory = TransformerFactory.newInstance();
			Transformer transformer = null;
			try {
				transformer = transformerfactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DOMSource domsource = new DOMSource(document);
			File temp = new File(XMLFilePath+"\\"+ "temp" +".xml");
			StreamResult streamresult = new StreamResult(temp);
			
			
			try {
				transformer.transform(domsource,streamresult);
			} catch (TransformerException e) {
				System.err.println("Error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			
			
			if(XML_validator.validate_Xml_Xsd(temp.getAbsolutePath(),DBS.get(table_DB.get(table_name)).get(table_name+".xml").getAbsolutePath().replace("xml", "xsd"))) {
				try {
					movetofile(temp, DBS.get(table_DB.get(table_name)).get(table_name+".xml"));
					System.out.println("You Inserted Into >> "+table_name);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
			
				
				
				System.err.println("Error invalid datatype!");
				
				
			}
			temp.delete();
			
			
			
			
			
		}
		
		
		else if(order.equals("delete")) {
			String[] condition = null;
			if(!query.contains("where")) {
			}
			else {
				condition = new String[3];
				condition[0]=cr[1];
				condition[1]=cr[2];
				condition[2]=cr[3];
				NodeList colvals = document.getElementsByTagName(condition[0]);
				//System.out.println(colvals.item(1));
				//System.out.println(document.getFirstChild().getChildNodes().item(1).getChildNodes().item(0));
				//System.out.println(document.getFirstChild().getChildNodes().item(1).getChildNodes().item(1));
				boolean deleted = false;
				for(int i = 0 ; i < colvals.getLength() ; i++) {
					if(condition == null ||condition_calculator(document.getElementsByTagName(condition[0]).item(i).getTextContent(), condition[2], condition[1].charAt(0))) {
						deleted = true;
						document.getFirstChild().removeChild(document.getFirstChild().getChildNodes().item(i));						
					}
				}
				TransformerFactory transformerfactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					if(deleted) {
						transformer = transformerfactory.newTransformer();
						System.out.println("Delete done");
					}
				} catch (TransformerConfigurationException e) {
					System.err.println("Error!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DOMSource domsource = new DOMSource(document);
				StreamResult streamresult = new StreamResult(DBS.get(table_DB.get(table_name)).get(table_name+".xml"));
				
				try {
					transformer.transform(domsource,streamresult);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
			
			}
				
			
			
			
			
		
			
			
			
			
			
			
			
			
			
			
			
		}
		
		
		else if(order.equals("update")) {
			if(!query.contains("where")) {
				String[] cols_names = new String[(cr.length-1)/3];
				//String[] sign = new String[(cr.length-1)/3];
				String[] cols_vals = new String[(cr.length-1)/3];
				
				for(int i = 0 , j = 1 ; i < cols_names.length ; i++,j+=3) {
					cols_names[i]=cr[j];
					//sign[i]=cr[j+1];
					cols_vals[i]=cr[j+2];
					//System.out.println(cols_names[i] + " "+cols_vals[i]);
				}
				
				for(int i = 0 ; i < cols_names.length ; i++) {
					for(int j = 0 ; j < document.getElementsByTagName(cols_names[i]).getLength() ; j++) {
						document.getElementsByTagName(cols_names[i]).item(j).setTextContent(cols_vals[i]);
				
					}
				}
				System.out.println(document.getElementsByTagName(cols_names[0]).item(0).getTextContent());
			}
			//*****************if the input contains where **********
			else {
				String[] condition = new String[3];
				String[] cols_names = new String[(cr.length-4)/3];
				String[] sign = new String[(cr.length-4)/3];
				String[] cols_vals = new String[(cr.length-4)/3];
				int c = 1;
				for(int i = 0 ; i < cols_names.length ; i++,c+=3) {
					cols_names[i]=cr[c];
					sign[i]=cr[c+1];
					cols_vals[i]=cr[c+2];
				}
				condition[0]=cr[c];
				condition[1]=cr[c+1];
				condition[2]=cr[c+2];
				for(int i = 0 ; i < cols_names.length ; i++) {
					for(int j = 0 ; j < document.getElementsByTagName(cols_names[i]).getLength() ; j++) {
						if(condition_calculator(document.getElementsByTagName(cols_names[i]).item(j).getTextContent(), condition[2] , condition[1].charAt(0))) {
							document.getElementsByTagName(cols_names[i]).item(j).setTextContent(cols_vals[i]);
							}
					}
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
				File temp = new File(XMLFilePath+"\\"+ "temp" +".xml");
				StreamResult streamresult = new StreamResult(temp);
				
				
				try {
					transformer.transform(domsource,streamresult);
					System.out.println("You Updated >> "+table_name);
				} catch (TransformerException e) {
					System.err.println("Error");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
				
				if(XML_validator.validate_Xml_Xsd(temp.getAbsolutePath(),DBS.get(table_DB.get(table_name)).get(table_name+".xml").getAbsolutePath().replace("xml", "xsd"))) {
					try {
						movetofile(temp, DBS.get(table_DB.get(table_name)).get(table_name+".xml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
				
					
					
					System.err.println("Error invalid datatype!");
					
					
				}
				temp.delete();
				}	
			
		
			
			
			}
		}
	return 0;
	}
}

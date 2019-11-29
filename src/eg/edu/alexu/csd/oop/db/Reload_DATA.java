package eg.edu.alexu.csd.oop.db;

import java.awt.image.DataBufferShort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class Reload_DATA {
	
	private final static String XMLFilePath = "D:\\DBMS\\databases";
	private DB DB_instance = DB.get_instance();
	
	public void reload() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(DB_instance.get_saving_file()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.contains("Name: ")) {
		    	   Map<String, File>temp=new HashMap<String, File>();
		    	   String foldername= line.substring(6, line.length());
		    	   DB_instance.database_names_adding(foldername);
		    	   String path=  br.readLine();
		    	   String p = new String();
			       if (path.contains(XMLFilePath)) {
			    	   p = XMLFilePath+"\\"+foldername;
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
			    			   DB_instance.table_DB_adding(file.getName().substring(0, file.getName().length()-4), foldername);
			    			   temp.put(file.getName(), file);	
		    	    	  } 
		    	  //   System.out.println(file.getName());
		    	      }
		    	  }
		    	  
		    	  DB_instance.DBS_adding(foldername, temp);
		       }
		    }
		}
		// just for test 
		/*System.out.println("map1");
		DBS.forEach((key, value) -> System.out.println(key + " : " + value));
		System.out.println("map2");
		table_DB.forEach((key, value) -> System.out.println(key + " : " + value));	
		System.out.println("stack");
		String values = Arrays.toString(database_names.toArray());
        System.out.println(values);*/
		
	}
	
}

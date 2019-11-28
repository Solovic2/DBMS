package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;

public class Facade {
	private DB DB_instance = DB.get_instance();
	
	public void do_query(String query) throws SQLException {
		query = query.toLowerCase();
		if( !query.contains(" ") ){
			System.err.println("syntax error");
			return;
		}
		String First_Word = query.substring(0, query.indexOf(" "));
		
		switch(First_Word) {
		case "drop": DB_instance.executeStructureQuery(query);
			break;
		case "select": DB_instance.executeQuery(query);
			break;
		case "delete": DB_instance.executeUpdateQuery(query);
			break;
		case "insert": DB_instance.executeUpdateQuery(query);
			break;
		case "update": DB_instance.executeUpdateQuery(query);
			break;
		case "create": DB_instance.executeStructureQuery(query);
			break;	
		default: System.err.println("syntax error");
		    break;
		}	
	}
	
}

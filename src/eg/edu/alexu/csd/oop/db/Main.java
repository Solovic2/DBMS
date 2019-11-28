package eg.edu.alexu.csd.oop.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner input=new Scanner(System.in);
		String query = new String();
		Reload_DATA ourinput = new Reload_DATA();
		Facade facade = new Facade();
		System.out.println("*****************************Welcome TO Our DBMS*****************************");
		try {
			ourinput.reload();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			System.out.println("Enter Your SQL query in right synatx (to exit just right'end'):");
			 query = input.nextLine();
			 if(query.equalsIgnoreCase("end")) {
				 break;
			 }
				query = query.toLowerCase();
						try {
							facade.do_query(query);
						} catch (SQLException e) {
							System.err.println("syntax error");
							e.printStackTrace();
						}
				
		}
		System.out.println("*****************************Hope You Enjoyed Our DBMS*****************************");
		
	
	}
}
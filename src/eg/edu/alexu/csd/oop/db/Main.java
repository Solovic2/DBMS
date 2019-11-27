package eg.edu.alexu.csd.oop.db;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.hamcrest.Matcher;

public class Main {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		String s= new String();
		DB ourinput=DB.get_instance();
		System.out.println("******************************Welcome TO Our DBMS******************************");
		System.out.println("Enter Your SQL query in right synatx (to exit just right'end'):");
		try {
			ourinput.reload();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			
			 s=input.nextLine();
			 if(s.equalsIgnoreCase("end")) {
				 break;
			 }
				s=s.toLowerCase();
				
							try {
								ourinput.mainRegex(s);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Enter Your SQL query in right synatx (to exit just right'end'):");

				
		}
		System.out.println("******************************Hope You Enjoyed Our DBMS******************************");
		
	
	}
}
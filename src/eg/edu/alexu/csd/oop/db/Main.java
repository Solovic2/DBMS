package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.hamcrest.Matcher;

public class Main {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		String s=input.nextLine();
		s=s.toLowerCase();
		Check c=new Check();
				try {
					c.mainRegex(s);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
	}
}

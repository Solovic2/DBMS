package eg.edu.alexu.csd.oop.db;

import java.util.regex.Pattern;

public class Check {

	public void mainRegex(String s) {
		s=s.toLowerCase();
		String r=s.substring(0, s.indexOf(" "));
		System.out.println(r);
		String v;
		switch(r) {
		case "drop": v=dropscheck(s);
				System.out.println(v);
			break;
		case "select": v=dropscheck(s);
			break;
		case "delete": v=dropscheck(s);
			break;
		case "insert": v=dropscheck(s);
			break;
		case "update": v=dropscheck(s);
			break;
		case "create": v=dropscheck(s);
			break;			
		}
        
	}
	public String dropscheck(String group) {
        String num =  "(drop)(\\s+)(table)(\\s+)(\\w+)";
        Pattern pattern = Pattern.compile(num);
        java.util.regex.Matcher matcher = pattern.matcher(group);
        if (matcher.matches()) {
        	return matcher.group(5);
        }
        return null;
	}
}

package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Check {
	
	/********************************Singleton Design Pattern********************************/
	private static Check bb = new Check();
	
	private Check() {}
	
	public static Check get_instance() {
		return bb;
	}
	/******************************************************************************************/
	
	public String[] createcheck(String s) throws SQLException {
		
		ArrayList<String> re=new ArrayList<String>(); 
		re.clear();
		String num_tables = "(create)(\\s+)(table)(\\s+)(\\w+)(\\s*+)(\\()(\\s*+)(\\s*[\\w]+\\s+(int|varchar)\\s*(?:,\\s*[\\w]+\\s+(int|varchar)\\s*){0,})(\\s*+)(\\))(\\s*+)";
		String num_database="(create)(\\s+)(database)(\\s+)(\\w+)(\\s*+)";
		String path_database="(create)(\\s+)(database)(\\s+)(([a-zA-Z]:)?(\\\\{0,2}[-@./#&+\\w\\s]+)+)(\\s*+)";
		Pattern pattern = Pattern.compile(num_tables);
        java.util.regex.Matcher matcher = pattern.matcher(s);
		Pattern pattern_database = Pattern.compile(num_database);
        java.util.regex.Matcher matcher_database = pattern_database.matcher(s);
        Pattern pattern_path = Pattern.compile(path_database);
        java.util.regex.Matcher matcher_path = pattern_path.matcher(s);
        if (matcher.matches()) {
        	re.add(matcher.group(5));
        	String[] coma=matcher.group(9).split(",");
        	for(int i=0;i<coma.length;i++) {
            	String temp=coma[i];
            	String regex="(\\s*)(\\w+)(\\s*)(int|varchar)(\\s*)";
            	Pattern pattern_regex = Pattern.compile(regex);
            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
            	matcher_regex.matches();
            	re.add(matcher_regex.group(2));
            	re.add(matcher_regex.group(4));
            }
        	
        	return re.toArray(new String[re.size()]);
        }else if(matcher_database.matches()) {
        	re.add(matcher_database.group(5));
        	return re.toArray(new String[re.size()]);
        }else if(matcher_path.matches()) {
        	re.add(matcher_path.group(5));
        	return re.toArray(new String[re.size()]);
        }

        System.err.println("syntax error");
        return null;
	}
	public String[] updatecheck(String s) throws SQLException {
		ArrayList<String> re=new ArrayList<String>(); 
		re.clear();
		String updateall="(update)(\\s+)(\\w+)(\\s+)(set)(\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))\\s*(?:,\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))){0,})(\\s*)";
		String update="(update)(\\s+)(\\w+)(\\s+)(set)(\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))\\s*(?:,\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))){0,})(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		Pattern pattern_updateall = Pattern.compile(updateall);
        java.util.regex.Matcher matcher_updateall = pattern_updateall.matcher(s);
		Pattern pattern_update = Pattern.compile(update);
        java.util.regex.Matcher matcher_update = pattern_update.matcher(s);
        if(matcher_update.matches()) {
        	re.add(matcher_update.group(3));
        	String[] coma=matcher_update.group(6).split(",");
        	for(int i=0;i<coma.length;i++) {
            	String temp=coma[i];
            	String regex="(\\s*)(\\w+)(\\s*)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|\\d+)(\\s*)";
            	Pattern pattern_regex = Pattern.compile(regex);
            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
            	matcher_regex.matches();
            	re.add(matcher_regex.group(2));
            	re.add(matcher_regex.group(4));
            	String s13;
            	if(matcher_regex.group(6).matches("\\d+")) {
            		s13=""+matcher_regex.group(6);
            	}else {
            		s13=matcher_regex.group(6).substring(1, matcher_regex.group(6).length()-1);
            	}
            	re.add(s13);
            }
        	re.add(matcher_update.group(16));
        	re.add(matcher_update.group(18));
        	String s13;
        	if(matcher_update.group(20).matches("\\d+")) {
        		s13=""+matcher_update.group(20);
        	}else {
        		s13=matcher_update.group(20).substring(1,matcher_update.group(20).length()-1);
        	}
        	re.add(s13);
        	return re.toArray(new String[re.size()]);
        }	
        else if(matcher_updateall.matches()) {
        	re.add(matcher_updateall.group(3));
        	String[] coma=matcher_updateall.group(6).split(",");
            for(int i=0;i<coma.length;i++) {
            	String temp=coma[i];
            	String regex="(\\s*)(\\w+)(\\s*)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|\\d+)(\\s*)";
            	Pattern pattern_regex = Pattern.compile(regex);
            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
            	matcher_regex.matches();
            	re.add(matcher_regex.group(2));
            	re.add(matcher_regex.group(4));
            	String s13;
            	if(matcher_regex.group(6).matches("\\d+")) {
            		s13=""+matcher_regex.group(6);
            	}else {
            		s13=matcher_regex.group(6).substring(1, matcher_regex.group(6).length()-1);
            	}
            	re.add(s13);
            }
            return re.toArray(new String[re.size()]);
        }
        
        System.err.println("syntax error");
        return null;
	}
	public String[] insertcheck(String s) throws SQLException {
		ArrayList<String> re=new ArrayList<String>(); 
		re.clear();
		String insert_int = "(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*+)(\\()(\\s*+)([\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s*+)(\\))(\\s*)(values)(\\s*)(\\()((?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))(?:,(?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))){0,})(\\))(\\s*+)";
		String insert2="(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*+)(values)(\\s*)(\\()((?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))(?:,(?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))){0,})(\\))(\\s*+)";
		Pattern pattern_insertInt = Pattern.compile(insert_int);
	        java.util.regex.Matcher matcher_insertInt = pattern_insertInt.matcher(s);
	        Pattern pattern_insert2 = Pattern.compile(insert2);
	        java.util.regex.Matcher matcher_insert2 = pattern_insert2.matcher(s);
	        if(matcher_insertInt.matches()) {
	        	re.add(matcher_insertInt.group(5));
	        	re.add("1");
	        	String[] coma = matcher_insertInt.group(9).split(",");
	        	String[] coma2 = matcher_insertInt.group(16).split(",");
	        	if(coma.length != coma2.length)return null;
	        	for(int i = 0 ; i < coma.length ; i++) {
	            	String temp = coma[i];
	            	String regex = "(\\s*)(\\w+)(\\s*)";
	            	Pattern pattern_regex = Pattern.compile(regex);
	            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
	            	matcher_regex.matches();
	            	String temp2 = coma2[i];
	            	String regex2 = "(\\s*)((?:\\'[\\s\\S]+\\')|\\d+)(\\s*)";
	            	Pattern pattern_regex2 = Pattern.compile(regex2);
	            	java.util.regex.Matcher matcher_regex2 = pattern_regex2.matcher(temp2);
	            	matcher_regex2.matches();
	            	re.add(matcher_regex.group(2));
	            	String s13;
	            	if(matcher_regex2.group(2).matches("\\d+")) {
	            		s13=""+matcher_regex2.group(2);
	            	}else {
	            		s13=matcher_regex2.group(2).substring(1, matcher_regex2.group(2).length()-1);
	            	}
	            	re.add(s13);
	            }
	        	return re.toArray(new String[re.size()]);
	        }else if(matcher_insert2.matches()) {
	        	re.add(matcher_insert2.group(5));
	        	re.add("2");
	        	String[] coma=matcher_insert2.group(10).split(",");
	        	for(int i=0;i<coma.length;i++) {
	        		String temp=coma[i];
	            	String regex="(\\s*)((?:\\'[\\s\\S]+\\')|\\d+)(\\s*)";
	            	Pattern pattern_regex = Pattern.compile(regex);
	            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
	            	matcher_regex.matches();
	            	String s13;
	            	if(matcher_regex.group(2).matches("\\d+")) {
	            		s13=""+matcher_regex.group(2);
	            	}else {
	            		s13=matcher_regex.group(2).substring(1, matcher_regex.group(2).length()-1);
	            	}
	            	re.add(s13);
	        	}
	        	return re.toArray(new String[re.size()]);
	        }
	        System.err.println("syntax error");
	    return null;
	}
	public String[] deletecheck(String s) throws SQLException {
		ArrayList<String> re=new ArrayList<String>(); 
		re.clear();
		String deleteall="(delete)(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String delete="(delete)(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		Pattern pattern_deleteall = Pattern.compile(deleteall);
        java.util.regex.Matcher matcher_deleteall = pattern_deleteall.matcher(s);
		Pattern pattern_delete = Pattern.compile(delete);
        java.util.regex.Matcher matcher_delete = pattern_delete.matcher(s);
        if(matcher_deleteall.matches()) {
        	re.add(matcher_deleteall.group(5));
        	return re.toArray(new String[re.size()]);
        }else if(matcher_delete.matches()) {
        	re.add(matcher_delete.group(5));
        	re.add(matcher_delete.group(9));
        	re.add(matcher_delete.group(11));
        	String s13;
        	if(matcher_delete.group(13).matches("\\d+")) {
        		s13=""+matcher_delete.group(13);
        	}else {
        		s13=matcher_delete.group(13).substring(1, matcher_delete.group(13).length()-1);
        	}
        	re.add(s13);
        	return re.toArray(new String[re.size()]);
        }
        System.err.println("syntax error");
        return null;
	}
	public String[] selectcheck(String s) throws SQLException {
		ArrayList<String> re=new ArrayList<String>(); 
		re.clear();
		String selectall="(select)(\\s+)([*])(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String selectall_where="(select)(\\s+)([*])(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		String select="(select)(\\s+)(\\s*[\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String select_where="(select)(\\s+)(\\s*[\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";//here??
		//select all without where condition
		Pattern pattern_selectall = Pattern.compile(selectall);
        java.util.regex.Matcher matcher_selectall = pattern_selectall.matcher(s);
        //select all with where condition
        Pattern pattern_selectall_where = Pattern.compile(selectall_where);
        java.util.regex.Matcher matcher_selectall_where = pattern_selectall_where.matcher(s);
        //select  without where condition
        Pattern pattern_select = Pattern.compile(select);
        java.util.regex.Matcher matcher_select = pattern_select.matcher(s);
        //select  with where condition
        Pattern pattern_select_where = Pattern.compile(select_where);
        java.util.regex.Matcher matcher_select_where = pattern_select_where.matcher(s);
        if(matcher_selectall.matches()) {
        	//System.out.println("selsct all");
        	re.add(matcher_selectall.group(7));
        	return re.toArray(new String[re.size()]);
        }else if(matcher_selectall_where.matches()) {
        	//System.out.println("selsct all whith where");
        	/* Table Name */
        	re.add(matcher_selectall_where.group(7));
        	/*Where (name) (=) (value)*/
        	re.add(matcher_selectall_where.group(11));
        	re.add(matcher_selectall_where.group(13));
        	String s13;
        	/* if the value is integer */
        	if(matcher_selectall_where.group(15).matches("\\d+")) {
        		s13=""+matcher_selectall_where.group(15);
        	}else {
        		/* if the value is String */	
        		s13=matcher_selectall_where.group(15).substring(1, matcher_selectall_where.group(15).length()-1);
        	}
        	re.add(s13);
        	return re.toArray(new String[re.size()]);
        }else if(matcher_select.matches()) {
        	//System.out.println("selsct ");
        	re.add(matcher_select.group(7));// table name
            /* Split for colums*/
        	String[] coma=matcher_select.group(3).split(",");
        	 for(int i=0;i<coma.length;i++) {
	            	String temp=coma[i];
	            	/* regex for colums */
	            	String regex="(\\s*)(\\w+)(\\s*)";
	            	Pattern pattern_regex = Pattern.compile(regex);
	            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
	            	matcher_regex.matches();
	            	re.add(matcher_regex.group(2));
        	 }
        	 
        	 return re.toArray(new String[re.size()]);
        }else if (matcher_select_where.matches()) {
        //	System.out.println("selsct whith where");
        	re.add(matcher_select_where.group(7));//table name
            /* Split for colums*/
        	String[] coma=matcher_select_where.group(3).split(",");
            for(int i=0;i<coma.length;i++) {
            	String temp=coma[i];
            	/* regex for colums */
            	String regex="(\\s*)(\\w+)(\\s*)";
            	Pattern pattern_regex = Pattern.compile(regex);
            	java.util.regex.Matcher matcher_regex = pattern_regex.matcher(temp);
            	matcher_regex.matches();
            	re.add(matcher_regex.group(2));
            }
        	/*Where (name) (=) (value)*/
        	re.add(matcher_select_where.group(11));
        	re.add(matcher_select_where.group(13));
            String s13;
        	/* if the value is integer */
        	if(matcher_select_where.group(15).matches("\\d+")) {
        		s13=""+matcher_select_where.group(15);
        	}else {
        		/* if the value is String */	
        		s13=matcher_select_where.group(15).substring(1, matcher_select_where.group(15).length()-1);
        		
        	}
        	re.add(s13);
        	return re.toArray(new String[re.size()]);
        }
        System.err.println("syntax error");
        return null;
	}
	public String dropscheck(String s) throws SQLException {
        String num =  "(drop)(\\s+)(table|database)(\\s+)(\\w+)(\\s*+)";
        Pattern pattern = Pattern.compile(num);
        java.util.regex.Matcher matcher = pattern.matcher(s);
        
        if (matcher.matches()) {
        	return matcher.group(5);
        }
        System.err.println("syntax error");
        return null;
	}
}
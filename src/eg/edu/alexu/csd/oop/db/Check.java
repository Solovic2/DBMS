package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class Check {
	DB ourinput=DB.get_instance();
	boolean forallinput=false;
	int forintinput=0;
	Object [][] forallobject;
	public void mainRegex(String s) throws SQLException {
		s=s.toLowerCase();
		String r=s.substring(0, s.indexOf(" "));
		switch(r) {
		case "drop": dropscheck(s);
			break;
		case "select": selectcheck(s);
			break;
		case "delete": deletecheck(s);
			break;
		case "insert": insertcheck(s);
			break;
		case "update": updatecheck(s);
			break;
		case "create": createcheck(s);
			break;			
		}
	}
	private void createcheck(String s) throws SQLException {
		String num_tables = "(create)(\\s+)(table)(\\s+)(\\w+)(\\s*+)(\\()(\\s*+)(\\s*[\\w]+\\s+(int|varchar)\\s*(?:,\\s*[\\w]+\\s+(int|varchar)\\s*){0,})(\\s*+)(\\))(\\s*+)";
		String num_database="(create)(\\s+)(database)(\\s+)(\\w+)(\\s*+)";
		Pattern pattern = Pattern.compile(num_tables);
        java.util.regex.Matcher matcher = pattern.matcher(s);
		Pattern pattern_database = Pattern.compile(num_database);
        java.util.regex.Matcher matcher_database = pattern_database.matcher(s);
        if (matcher.matches()) {
        	forallinput=ourinput.executeStructureQuery(s);
        }else if(matcher_database.matches()) {
        	forallinput=ourinput.executeStructureQuery(s);
        }
	}
	private void updatecheck(String s) throws SQLException {
		String updateall="(update)(\\s+)(\\w+)(\\s+)(set)(\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))\\s*(?:,\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))){0,})(\\s*)";
		String update="(update)(\\s+)(\\w+)(\\s+)(set)(\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))\\s*(?:,\\s*[\\w]+\\s*([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))){0,})(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		Pattern pattern_updateall = Pattern.compile(updateall);
        java.util.regex.Matcher matcher_updateall = pattern_updateall.matcher(s);
		Pattern pattern_update = Pattern.compile(update);
        java.util.regex.Matcher matcher_update = pattern_update.matcher(s);
        if(matcher_updateall.matches()) {
        	forintinput=ourinput.executeUpdateQuery(s);
        }else if(matcher_update.matches()) {
        	forintinput=ourinput.executeUpdateQuery(s);
        }
	}
	private void insertcheck(String s) throws SQLException {
		String insert_int="(insert)(\\s+)(into)(\\s+)(\\w+)(\\s*+)(\\()(\\s*+)([\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s*+)(\\))(\\s*)(values)(\\s*)(\\()((?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))(?:,(?:(?:\\s*\\'\\s*[\\s\\S]+\\s*\\'\\s*)|(?:\\s*[\\d]+\\s*))){0,})(\\))(\\s*+)";
		Pattern pattern_insertInt = Pattern.compile(insert_int);
	        java.util.regex.Matcher matcher_insertInt = pattern_insertInt.matcher(s);
	        if(matcher_insertInt.matches()) {
	        	forintinput=ourinput.executeUpdateQuery(s);
	        }
	}
	private void deletecheck(String s) throws SQLException {
		String deleteall="(delete)(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String delete="(delete)(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		Pattern pattern_deleteall = Pattern.compile(deleteall);
        java.util.regex.Matcher matcher_deleteall = pattern_deleteall.matcher(s);
		Pattern pattern_delete = Pattern.compile(delete);
        java.util.regex.Matcher matcher_delete = pattern_delete.matcher(s);
        if(matcher_deleteall.matches()) {
        	forintinput=ourinput.executeUpdateQuery(s);
        }else if(matcher_delete.matches()) {
        	forintinput=ourinput.executeUpdateQuery(s);
        }
	}
	private void selectcheck(String s) throws SQLException {
		String selectall="(select)(\\s+)([*])(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String selectall_where="(select)(\\s+)([*])(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
		String select="(select)(\\s+)(\\s*[\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s+)(from)(\\s+)(\\w+)(\\s*+)";
		String select_where="(select)(\\s+)(\\s*[\\w]+\\s*(?:,\\s*[\\w]+\\s*){0,})(\\s+)(from)(\\s+)(\\w+)(\\s+)(where)(\\s+)(\\w+)(\\s*+)([=><])(\\s*)((?:\\'[\\s\\S]+\\')|(?:\\d+))(\\s*+)";
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
        	forallobject=ourinput.executeQuery(s);
        }else if(matcher_selectall_where.matches()) {
        	forallobject=ourinput.executeQuery(s);
        }else if(matcher_select.matches()) {
        	forallobject=ourinput.executeQuery(s);
        }else if (matcher_select_where.matches()) {
        	forallobject=ourinput.executeQuery(s);
        }
	}
	public void dropscheck(String s) throws SQLException {
        String num =  "(drop)(\\s+)(table|database)(\\s+)(\\w+)(\\s*+)";
        Pattern pattern = Pattern.compile(num);
        java.util.regex.Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
        	forallinput=ourinput.executeStructureQuery(s);
        }
	}
}
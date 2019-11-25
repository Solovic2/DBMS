package eg.edu.alexu.csd.oop.db;

import java.io.File;

public class delete_dir {
	public static boolean deleteDirectory(File dir) { 
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) { 
				boolean success = deleteDirectory(children[i]);
				if (!success) { 
					return false; 
					}
				
				}
			
			}
		return dir.delete();
		
	}

}

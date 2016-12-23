/**
 * 
 */
package com.services.projects.bean;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.services.credentials.profiles.DAOProfile;

/**
 * @author thomas
 *
 */
public class DOAProject {
	private static final String PROJECT_DEFAULT = "Default";
	private static final String HTTP_UPLOAD_PARAM_VAL = "project";
	/**
	 * Static absolute file path where the xml files are stored
	 */
	private static String urlDbPath;
	/**
	 * Static relative DB dir location
	 */
	private static String DB_PATH="DB/projects";
	private static TreeMap<String, Path> repository=null;
	private static DOAProject instance=null;
	
	private DOAProject(){
		
	}
	
	
	/**
	 * Initial the DAO based on the root DB directory folder
	 * @param rootCtxPath
	 * @return
	 */
	public static DOAProject getInstance(String rootCtxPath){
		
		if(instance != null){
			return instance;
		}else{
			instance = new DOAProject();
			urlDbPath = initUrlDbPath(rootCtxPath,DB_PATH);
			repository = new TreeMap<String, Path>();
			System.out.println(urlDbPath);
			Path folder = Paths.get(urlDbPath);
			Collection<File> _files = FileUtils.listFiles(folder.toFile(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : _files) {
				System.out.println(file.getPath());
				String _name  = FilenameUtils.getBaseName(file.getName());
				String _ext  = FilenameUtils.getExtension(file.getName());
				if(_ext.equalsIgnoreCase("xml") && _name.startsWith(HTTP_UPLOAD_PARAM_VAL+"_")){
					String[] _tmp = _name.split("_"); 
					repository.put(_tmp[1], Paths.get(file.toURI()));
				}
				
			}
			
		}
		return 	instance;		
	}


	/**
	 * @param rootCtxPath
	 */
	private static String initUrlDbPath(String rootCtxPath, String dbPathREl) {
		String _res=null;
		String _endSep="";
		if(!rootCtxPath.endsWith("/"))_endSep="/";
		_res=rootCtxPath+_endSep+DB_PATH;
		return _res;
	}

	
	

}

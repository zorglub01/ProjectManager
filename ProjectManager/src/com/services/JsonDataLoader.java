package com.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;






/**
 * Servlet implementation class JsonDataLoader
 */
public class JsonDataLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JsonDataLoader() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Set<String> _listProfiles = config.getServletContext().getResourcePaths("/DB/profiles/");
		for (String string : _listProfiles) {
			System.out.println(string);
			if(string.endsWith(".xml"))	System.out.println("adding : " + string);;
		}
	}
    
    
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext _ctx = getServletContext();
		InputStream fileXmlUrl = _ctx.getResourceAsStream("/WEB-INF/classes/com/lang/util/msg.properties");
		Properties _pProperties = new Properties();
		_pProperties.load(fileXmlUrl);
		ObjectMapper mymapper = new ObjectMapper();
		mymapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		MyResult _result = new MyResult();
		String jsonInString = mymapper.writeValueAsString(_result);
		System.out.println(jsonInString);
		String _res=  _pProperties.getProperty("test_listaction");
		response.setContentType("application/json");
		response.getWriter().append(_res);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	

}

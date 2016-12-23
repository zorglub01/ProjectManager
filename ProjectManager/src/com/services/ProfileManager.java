package com.services;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.credentials.profiles.DAOProfile;
import com.services.credentials.profiles.UserProfile;
import com.services.projects.model.Profile;
import com.services.projects.utils.ModelManagerHelper;

/**
 * Servlet implementation class ProfileManager
 */
public class ProfileManager extends HttpCRUDControler {
	private static final long serialVersionUID = 1L;
    
    
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String rootCtxPath =  getServletContext().getRealPath("/");
		DAOProfile.getInstance(rootCtxPath);

	}



	public JsonJtableWrapper deleteModel(HttpServletRequest request, JsonJtableWrapper _res) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonJtableWrapper updateModel(HttpServletRequest request, JsonJtableWrapper _res) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * @throws JAXBException 
	 * @throws MalformedURLException 
	 * @see com.services.HttpCRUDControler#createModel(javax.servlet.http.HttpServletRequest, com.services.JsonJtableWrapper)
	 */
	public JsonJtableWrapper createModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
			
			
			UserProfile newProfile = DAOProfile.getInstance().create(request.getParameterMap());
			_res.setRecord(newProfile.getProfile());
			_res.setResult("OK");
			
			return _res;
	}

	public JsonJtableWrapper loadModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
		
		for (UserProfile xmlProfile : DAOProfile.getInstance().getRegisterProfiles()) {
			_res.getRecords().add(xmlProfile.getProfile());
		}
		_res.setResult("OK");
		
		return _res;
	}

	
	
	
	

}

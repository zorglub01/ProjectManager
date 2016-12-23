package com.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.credentials.profiles.DAOProfile;
import com.services.credentials.profiles.UserProfile;
import com.services.projects.model.Profile;
import com.services.projects.model.ProfileURI;
import com.services.projects.model.Resource;
import com.services.projects.utils.ModelManagerHelper;

/**
 * Servlet implementation class ResourcesManager
 */
public class ResourcesManager extends HttpCRUDControler {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourcesManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	@Override
	public JsonJtableWrapper deleteModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
		String _profileId = request.getParameter("id");
		try {
			DAOProfile.getInstance().deleteResources(_profileId, request.getParameterMap());
			_res.setResult("OK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_res.setResult("ERROR");
			_res.setErrorMessage(e.getMessage());
		}
		return _res;
	}

	@Override
	public JsonJtableWrapper updateModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
		return this.createModel(request, _res);
	}

	@Override
	public JsonJtableWrapper createModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
		String _profileId = request.getParameter("id");
		UserProfile _res1;
		try {
			_res1 = DAOProfile.getInstance().updateResources(_profileId, request.getParameterMap());
			ProfileURI _res2 = _res1.getResourceURI(request.getParameter(UserProfile.HTTP_PARAM_ResourcePath));
			_res.setRecord(_res2);
			_res.setResult("OK");
		} catch (IOException e) {
			
			e.printStackTrace();
			_res.setResult("ERROR");
		}
		
		return _res;
	}

	@Override
	public JsonJtableWrapper loadModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException {
		String _profileId = request.getParameter("id");
		UserProfile _res1 = DAOProfile.getInstance().findProfileByName(_profileId);
		for (ProfileURI iterable_element : _res1.getProfile().getResources().getResource()) {
			_res.getRecords().add(iterable_element);
		}
		_res.setResult("OK");
		return _res;
	}

}

/**
 * 
 */
package com.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

/**
 * @author thomas
 *
 */
public abstract class HttpCRUDControler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7512522924270504124L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonJtableWrapper _res = new JsonJtableWrapper();
		_res.setResult("ERROR");
		
		String action = request.getParameter("action");
		try{
			if(action.equalsIgnoreCase("read")){
				
					_res  = this.loadModel( request, _res);
				
			}else if(action.equalsIgnoreCase("create")){
				_res  = this.createModel( request, _res);
			}else if(action.equalsIgnoreCase("update")){
				_res  = this.updateModel( request, _res);
			}else if(action.equalsIgnoreCase("delete")){
				_res  = this.deleteModel( request, _res);
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(this.getClass().getSimpleName() + " FAILED for action:" + action);
			_res.setErrorMessage(" FAILED for action:" + action + " stack:" + e.getMessage());
		}
		response.getWriter().append(_res.toJsonString());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
	
	public abstract JsonJtableWrapper deleteModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException;

	public abstract JsonJtableWrapper updateModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException;

	public abstract JsonJtableWrapper createModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException;

	public abstract JsonJtableWrapper loadModel(HttpServletRequest request, JsonJtableWrapper _res) throws JAXBException ;

}

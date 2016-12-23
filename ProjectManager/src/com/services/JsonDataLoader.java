package com.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Properties;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.util.PropertiesHelper;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		InputStream fileXmlUrl = getServletContext().getResourceAsStream("/WEB-INF/classes/com/lang/util/msg.properties");
		Properties _pProperties = new Properties();
		_pProperties.load(fileXmlUrl);
		JSONObject _jObject = new JSONObject();
		try {
			_jObject.put("Result", "OK");
			
			_jObject.put("Records", "OK");
			TreeMap<String, Person> _map = new TreeMap<String, Person>();
			_map.put("ID1", new Person(new Integer(1),"toto", new Integer(45)));
			_map.put("ID2", new Person(new Integer(2),"titi", new Integer(30)));
			_map.put("ID3", new Person(new Integer(3),"toto", new Integer(50)));
			_jObject.put("Records", _map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(_jObject);
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
	
	class Person {
		public Person(Integer personId, String name, Integer age) {
			super();
			this.personId = personId;
			this.name = name;
			this.age = age;
			this.recordDate = Date.valueOf("2016-12-24");
		}
		Integer personId;
		String name;
		Integer age;
		Date recordDate;
		/**
		 * @return the personId
		 */
		public Integer getPersonId() {
			return this.personId;
		}
		/**
		 * @param personId the personId to set
		 */
		public void setPersonId(Integer personId) {
			this.personId = personId;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the age
		 */
		public Integer getAge() {
			return this.age;
		}
		/**
		 * @param age the age to set
		 */
		public void setAge(Integer age) {
			this.age = age;
		}
		/**
		 * @return the recordDate
		 */
		public Date getRecordDate() {
			return this.recordDate;
		}
		/**
		 * @param recordDate the recordDate to set
		 */
		public void setRecordDate(Date recordDate) {
			this.recordDate = recordDate;
		}
		
		
	}
	
	

}

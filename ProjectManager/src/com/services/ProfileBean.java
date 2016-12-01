/**
 * 
 */
package com.services;

import java.util.HashMap;
import java.util.Map;



/**
 * @author thomas.foret
 * @param <AjaxBehaviorEvent>
 *
 */
public class ProfileBean {

	  private Map<String, String> exampleData = new HashMap<String, String>() {{ 
	        put("dune", "The Dune Book"); 
	        put("lotr", "The Lord of the Rings Book");
	    }};

	    private String searchString; 
	    private String book;

	    public void updateBook() {
	        book = exampleData.get(searchString);
	    }

	    public String getSearchString() {

	        return searchString;
	    }

	    public void setSearchString(String searchString) {
	        this.searchString = searchString;
	    }

	    public String getBook() {
	        return book;
	    }

	
}

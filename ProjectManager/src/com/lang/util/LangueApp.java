/**
 * 
 */
package com.lang.util;

import java.util.Locale;

import javax.faces.context.FacesContext;

/**
 * @author thomas.foret
 *
 */
public class LangueApp {

	  public String activerFR() {
		    FacesContext context = FacesContext.getCurrentInstance();
		    context.getViewRoot().setLocale(Locale.FRENCH);
		    
		    return null;
		  }

		  public String activerEN() {
		    FacesContext context = FacesContext.getCurrentInstance();
		    context.getViewRoot().setLocale(Locale.ENGLISH);
		    return null;
		  }

	
}


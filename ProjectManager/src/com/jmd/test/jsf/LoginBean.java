/**
 * 
 */
package com.jmd.test.jsf;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.SessionContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.omg.SecurityLevel2.CredentialsHelper;

import com.services.credentials.CredentialsManager;

/**
 * @author thomas.foret
 *
 */

public class LoginBean {

  private String nom;
  private String mdp;
  private List<SelectItem> menuItems;

  private void addMenuItems(SelectItem menuItem) {	
	this.getMenuItems().add(menuItem);
  }

  public List<SelectItem> getMenuItems() {
	if(this.menuItems == null){
		this.menuItems = new ArrayList<SelectItem>();
	}
	return this.menuItems;
  }

  public String getMdp() {
    return mdp;
  }

  public String getNom() {
    return nom;
  }

  public void setMdp(String string) {
    mdp = string;
  }

  public void setNom(String string) {
    nom = string;
  }
  
  public String checkPersonne(){
	  System.out.println(this.getNom() +"/" + this.getMdp() );
	  FacesContext fCtx = FacesContext.getCurrentInstance();
	  HttpSession session = (HttpSession )fCtx.getExternalContext().getSession(true);
	  try {
		  
		this.menuItems = CredentialsManager.getMenuEntry(session,this);
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  	  
	  
	  return "login";
  }
  
  
  
  
}
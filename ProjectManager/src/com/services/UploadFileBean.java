/**
 * 
 */
package com.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.credentials.profiles.DAOProfile;
import com.services.projects.bean.DAOProject;

/**
 * @author thomas.foret
 *
 */
public class UploadFileBean {

	private UploadedFile uploadedFile;
	private boolean success;
	private boolean failure;
	

	/**
	 * @return the dbContentOverview
	 */
	public String getFileScan() {
		String _res = "Error";
		String _scope = this.getScope();
		try{
			if (DAOProfile.getInstance().getHTTP_UPLOAD_PARAM_VAL().equalsIgnoreCase(_scope)) {
				_res = DAOProfile.getInstance().scanDbFile();
			}else if (DAOProject.getInstance().getHTTP_UPLOAD_PARAM_VAL().equalsIgnoreCase(_scope)) {
				_res = DAOProject.getInstance().scanDbFile();
			}
			
		}catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return _res;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		ExternalContext _ctx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession _session = (HttpSession)_ctx.getSession(true);
		String _scopeFromParam = _ctx.getRequestParameterMap().get("scope");
		String _scopeFromSession = (String) _session.getAttribute("scope");
		System.out.println("param from HTPP req:" + _scopeFromParam + " from session" + _scopeFromSession);
		
		if(_scopeFromParam != null ){
			_session.setAttribute("scope",_scopeFromParam);
		}
		String _res = (String) _session.getAttribute("scope");
		return _res;
	}

	/**
	 * @param scope
	 *            the scope to set
	 */
	public void setScope(String scope) {
		HttpSession _session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		 _session.setAttribute("scope",scope);
		
	}

	public String upload() {
		String result = "";
		try {
			InputStream stream = uploadedFile.getInputStream();
			long size = uploadedFile.getSize();
			System.out.println("Size of the file is " + size);
			String filename = FilenameUtils.getBaseName(uploadedFile.getName());
			String extension = FilenameUtils.getExtension(uploadedFile.getName());
			
			String _scope = this.getScope();
			if(DAOProfile.getInstance().getHTTP_UPLOAD_PARAM_VAL().equals(_scope)){
				DAOProfile.getInstance().importFile(stream,filename);
			}if(DAOProject.getInstance().getHTTP_UPLOAD_PARAM_VAL().equals(_scope)){
				DAOProject.getInstance().importFile(stream,filename);
			}
			else{
				ServletContext cContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
				
				String rootCtxPath =  cContext.getRealPath("/");
				Path _tmpFilePath = Paths.get(rootCtxPath + "/upload");
				Path file = Files.createTempFile(_tmpFilePath, filename + "_", "." + extension);
				Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
			}
			
			success = true;
			failure = false;
			System.out.println("File Upload Successful.");
			result = "ok";
		} catch (Exception ioe) {
			System.out.println("File Upload Unsuccessful.");
			ioe.printStackTrace();
			success = false;
			failure = true;
			result = "no";
		}
		return result;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isFailure() {
		return failure;
	}

	public void setFailure(boolean failure) {
		this.failure = failure;
	}

}

/**
 * 
 */
package com.services;




import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * @author thomas.foret
 *
 */
public class UploadFileBean {

	private UploadedFile uploadedFile;
	private boolean success;
	private boolean failure;

	public String upload() {
		String result = "";
		try {
			InputStream stream = uploadedFile.getInputStream();
			long size = uploadedFile.getSize();
			System.out.println("Size of the file is " + size);
			
			ServletContext cContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			String _rootPathApp = cContext.getRealPath("/");
			System.out.println(_rootPathApp);
			Path folder = Paths.get(_rootPathApp + "DB");
			
			String filename = FilenameUtils.getBaseName(uploadedFile.getName());
			String extension = FilenameUtils.getExtension(uploadedFile.getName());
			Path file = Files.createTempFile(folder, filename + "_", "."+extension);
			Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
			success = true;
			failure = false;
			System.out.println("File Upload Successful.");
			result = "ok";
		} catch (Exception ioe) {
			System.out.println("File Upload Unsuccessful." );
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

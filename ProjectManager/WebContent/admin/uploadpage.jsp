<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<f:view>
	<f:loadBundle basename="com.lang.util.msg" var="msg" />
	<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><h:outputText value="#{msg.login_titre}" /></title>
	</head>
	<body>
		<h:form enctype="multipart/form-data">
			<h:outputText value="Here is a File upload example." />

			<t:inputFileUpload value="#{uploadFileBean.uploadedFile}" size="20" />
			<h:commandButton value="Load the file"
				action="#{uploadFileBean.upload}" />
			<t:outputText value="File Uploaded Successfully."
				rendered="#{uploadFileBean.success}"
				style="color:green;font-weight:bold" />
			<t:outputText value="Error in File Uploading."
				rendered="#{uploadFileBean.failure}"
				style="color:red;font-weight:bold" />


		</h:form>
	</body>
</f:view>

</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<f:view>
<f:loadBundle basename="com.lang.util.msg" var="msg" />
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Projects manager</title>
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.theme.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />

<script type="text/javascript"	src="/ProjectManager/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery-ui-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery.appendGrid-1.6.2.js"></script>

<script>
jQuery(document).ready(function () {
    $("#projectAdminMenu").accordion();
   // $("#projectDetailContent").attr("src", "/ProjectManager/admin/profileview.faces");
});
  </script> 
</head>
<body>
<form id="projectAdminForm" action="">
	<div id="wrap1">
	<table cellspacing="10" cellpadding="10">
	<tr>
		<td width="200px" style="vertical-align: top">
			<div id="projectAdminMenu" style="font-size: 75%; height: 600px; width: 240px;">
			<c:forEach items="#{projectBeanWrapper.availableProjects }" var="cProject">
				<h3><h:outputText value="#{cProject.label }"/></h3>
				<div>
					<ul>
						<li>
						<t:htmlTag value="a">
							<f:param name="data-url" value="#{cProject.value}" />
							<f:param name="href" value="/ProjectManager/project/phaseview.faces?project=#{cProject.value}" />
							<f:param name="target" value="projectDetailContentFrame" />
							<h:outputText value="Sprints setup"/>
						</t:htmlTag>
						</li>
					</ul>
				</div>
			</c:forEach>
			</div>
		</td>
		<td width="1200px" valign="top">
        	<iframe id="projectDetailContent" name="projectDetailContentFrame"  style="width: 1200px; height: 2000px; border-width: 0;"></iframe>
        </td>
	</tr>
	</table>
	
	</div>
</form>

</f:view> 
</body>
</html>
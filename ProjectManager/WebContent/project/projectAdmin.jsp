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
<link rel="stylesheet" type="text/css"	href="/ProjectManager/script/jquery/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/script/jquery/jquery-ui.theme.min.css" />
<script type="text/javascript"	src="/ProjectManager/script/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/script/jquery/jquery-ui-1.11.1.min.js"></script>

<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />
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
	<table cellspacing="1" cellpadding="1">
	<tr>
		<td width="150px" style="vertical-align: top">
			<div id="projectAdminMenu" style="font-size: 75%; height: 600px; width: 200px;">
			<c:forEach items="#{projectBeanWrapper.availableProjects }" var="cProject">
				<h3><h:outputText value="#{cProject.label }"/></h3>
				<div>
					<ul>
						<li>
						<t:htmlTag value="a">
							<f:param name="data-url" value="#{cProject.value}" />
							<f:param name="href" value="/ProjectManager/project/phaseview2.faces?project=#{cProject.value}" />
							<f:param name="target" value="projectDetailContentFrame" />
							<h:outputText value="Sprints setup"/>
						</t:htmlTag>
						</li>
						<li>
						<t:htmlTag value="a">
							<f:param name="data-url" value="#{cProject.value}" />
							<f:param name="href" value="/ProjectManager/project/tasktimetracking.faces?project=#{cProject.value}" />
							<f:param name="target" value="projectDetailContentFrame" />
							<h:outputText value="Track Tasks"/>
						</t:htmlTag>
						</li>
						<li>
						<t:htmlTag value="a">
							<f:param name="data-url" value="#{cProject.value}" />
							<f:param name="href" value="/ProjectManager/project/tasktrackingview.faces?project=#{cProject.value}" />
							<f:param name="target" value="projectDetailContentFrame" />
							<h:outputText value="Task Tracking"/>
						</t:htmlTag>
						</li>
						<li>
						<t:htmlTag value="a">
							<f:param name="data-url" value="#{cProject.value}" />
							<f:param name="href" value="/ProjectManager/admin/uploadpage.faces?scope=project&project=#{cProject.value}" />
							<f:param name="target" value="projectDetailContentFrame" />
							<h:outputText value="#{msg.Upload_label}"/>
						</t:htmlTag>
						</li>
					</ul>
				</div>
			</c:forEach>
			</div>
		</td>
		<td width="100%" valign="top">
        	<iframe id="projectDetailContent" name="projectDetailContentFrame"  style="width: 100%; height: 2000px; border-width: 0;"></iframe>
        </td>
	</tr>
	</table>
	
	</div>
</form>

</f:view> 
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<f:view>
<f:loadBundle basename="com.lang.util.msg" var="msg" />
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Global Admin Setup</title>
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.theme.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />

<script type="text/javascript"	src="/ProjectManager/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery-ui-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery.appendGrid-1.6.2.js"></script>

<script>
jQuery(document).ready(function () {
    $("#credAdminMenu").accordion();
    //$("#credAdminFrame").attr("src", "/ProjectManager/admin/profileview.faces");
});
  </script> 
</head>
<body>

<form id="credAdminForm"action="">
	<div id="wrap">
	<table cellspacing="10" cellpadding="10">
	<tr>
		<td width="200px" style="vertical-align: top">
			<div id="credAdminMenu" style="font-size: 75%; height: 600px; width: 240px;">
				<h3>Profile and Credentials</h3>
				<div>
					<ul>
						<li>
							<a href="/ProjectManager/admin/profileview.faces" target="credAdminFrame">Profile setup</a>
						</li>
						<li>
							<a href="/ProjectManager/admin/uploadpage.faces?scope=profile" target="credAdminFrame">Import-Export</a>
						</li>
					</ul>
				</div>
				<h3>User</h3>
				<div>
					<ul>
						<li>
							<a href="/ProjectManager/admin/profileview.faces" target="credAdminFrame">Profile setup</a>
						</li>
						<li>
							<a href="/ProjectManager/admin/uploadpage.faces?scope=profile" target="credAdminFrame">Import-Export</a>
						</li>
					</ul>
				</div>
				<h3>Project Settings</h3>
				<div>
					<ul>
						<li>
							<a href="/ProjectManager/admin/projectview.faces" target="credAdminFrame">New Project Setup</a>
						</li>
						<li>
							<a href="/ProjectManager/admin/uploadpage.faces?scope=project" target="credAdminFrame">Import-Export</a>
						</li>
					</ul>
				</div>
				<h3> Sample</h3>
				<div>
					<ul>
						<li>
							<a href="/ProjectManager/SubGrid-sample/sample.html" target="credAdminFrame">Sub Grid</a>
						</li>
						<li>
							<a href="/ProjectManager/sample1/sample.html" target="credAdminFrame">Sample1</a>
						</li>
						<li>
							<a href="/ProjectManager/sample2/sample.html" target="credAdminFrame">Sample2</a>
						</li>
					</ul>
				</div>
			</div>
		</td>
		<td width="1200px" valign="top">
        	<iframe id="credAdminFrame" name="credAdminFrame"  style="width: 1200px; height: 2000px; border-width: 0;"></iframe>
        </td>
	</tr>
	</table>
	
	</div>
</form>

</f:view> 
</body>
</html>
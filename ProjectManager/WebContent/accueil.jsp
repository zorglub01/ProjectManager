<%@page import="com.jmd.test.jsf.LoginBean"%>
<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Insert title here</title>

  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.theme.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />

<script type="text/javascript"	src="/ProjectManager/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery-ui-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery.appendGrid-1.6.2.js"></script>

</head>
<body>
<f:view>
<f:loadBundle basename="com.lang.util.msg" var="msg" />


<table style="width: 100%">
<thead>
	<tr>
		<th><h:outputText value="#{msg.accueil_header}" /><h:outputText value="#{login.nom }"/></th>
	</tr>
</thead>
<tbody>
	<tr>
		<td>
			<div id="mainmenu">
			  <ul>	
			  	<c:forEach items="#{login.menuItems}" var="menuEntry">
			  		<li>
			  			<t:htmlTag value="a">
							<f:param name="data-url" value="#{menuEntry.value}" />
							<f:param name="href" value="#About" />
							<f:param name="class" value="accueiltabs1" />
							<h:outputText value="#{menuEntry.label }"/>
						</t:htmlTag>
			  		</li>
			  	</c:forEach>
			    <li><a href="#About" data-url="/ProjectManager/help/help.faces" class="accueiltabs1">About</a></li>    
			  </ul>	
			  <div id="About"></div>		    
			</div>	
		</td>
		
	</tr>
	<tr>
		<td><iframe id="globalContentFrm" name="globalContentFrm"  style="width: 100%; height: 2000px; border-width: 1;"></iframe></td>
	</tr>
</tbody>
</table>

<p></p>



</f:view>
</body>

<script>
  $(document).ready( function() {
    $( "#mainmenu" ).tabs();
    
    $('.accueiltabs1').click(function(){
    	var $this = $(this);
    	//alert($this.attr('href') +" / " + $this.attr('data-url') + "/" + $this.data('url'));
    	$('#globalContentFrm').attr("src", $this.data('url'));
    	//$('#globalContentFrm').load($this.data('url'));
    	
    });
    
 
  } );
  </script>

</html>
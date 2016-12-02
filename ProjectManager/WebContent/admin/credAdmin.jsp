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
  <title>jQuery UI Menu - Categories</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
  <style>
  .ui-menu { width: 200px; }
  .ui-widget-header { padding: 0.2em; }
  body {min-height:100%;margin:0;} 
  </style>
</head>
<body>

<table style="width: 100%">
<thead>
	<tr>
		<th style="width: 15%" ></th>
		<th style="width: 85%" ></th>
	</tr>		
</thead>
<tbody>
	<tr>
		<td>
		<div id=sidebar" class="ui-widget-area"> 
	<ul id="menu" >
  		<li class="ui-widget-header"><div><h:outputText value="#{msg.admin_user_credentials}"/>	</div></li>
  		<li><a href="#tabs-3" data-url="admin/profileview.faces" class="tabs"><h:outputText value="#{msg.admin_entry_profile}"/></a></li>
  		<li><a href="#tabs-4" data-url="admin/groupsview.faces" class="tabs"><h:outputText value="#{msg.admin_entry_group}"/></a></li>
  		<li><a href="#tabs-5" data-url="admin/userview.faces" class="tabs"><h:outputText value="#{msg.admin_entry_user}"/></a></li>
  		<li class="ui-widget-header"><div><h:outputText value="#{msg.admin_project_credentials}"/></div></li>
  		<li><div><h:outputText value="#{msg.admin_entry_project}"/></div></li>
  		<li><div><h:outputText value="#{msg.admin_entry_calendar}"/></div></li>
  		<li><div><h:outputText value="#{msg.admin_entry_workload}"/></div></li>
	</ul>
	</div>
		</td>
		<td><div id="content" ></div>
		</td>
	</tr>
</tbody>
</table>

<script>
  $( function() {
    $( "#menu" ).menu({
      items: "> :not(.ui-widget-header)"
    });
    
    $('.tabs').click(function(){
    	var $this = $(this);
    	//alert($this.attr('href') +" / " + $this.attr('data-url') + "/" + $this.data('url'));
    	$('#content').load($this.data('url'));
    });
    
    
  } );
  </script> 
</f:view> 
</body>
</html>
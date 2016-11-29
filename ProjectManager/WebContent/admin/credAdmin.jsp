<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
  </style>
</head>
<body>
<div class="content-right twelve columns">
	<div id="content" style="float:right;"></div>
	<div id=sidebar" class="ui-widget-area"> 
	<ul id="menu" >
  		<li class="ui-widget-header"><div>Users Credentials</div></li>
  		<li><a href="#tabs-3" data-url="admin/credAdmin.faces" class="tabs">Profiles</a></li>
  		<li><a href="#tabs-4" data-url="test_html/content1.html" class="tabs">Groups</a></li>
  		<li><a href="#tabs-5" data-url="content1.html" class="tabs">User</a></li>
  		<li class="ui-widget-header"><div>Projects Settings</div></li>
  		<li><div>Project</div></li>
  		<li><div>Calendar</div></li>
  		<li><div>workload</div></li>
	</ul>
	</div>
</div>
<script>
  $( function() {
    $( "#menu" ).menu({
      items: "> :not(.ui-widget-header)"
    });
    
    $('.tabs').click(function(){
    	var $this = $(this);
    	alert($this.attr('href') +" / " + $this.attr('data-url') + "/" + $this.data('url'));
    	$('#content').load($this.data('url'));
    });
    
    
  } );
  </script> 
 
</body>
</html>
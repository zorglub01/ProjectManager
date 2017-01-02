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
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery-ui.theme.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />

<script type="text/javascript"	src="/ProjectManager/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery-ui-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/jquery.appendGrid-1.6.2.js"></script>

<script type="text/javascript">

$(document).ready(function () {
    // Initialize appendGrid
    var mydata1 = $('textarea[id*="beandata"]').val();
    
    //alert(mydata1);
    try{
    	var mydata=JSON.parse(mydata1);
    }catch(e){
    	console.error("Parsing error:", e);
    	alert("marche pas from bean");
    }
    
    //alert(mydata);
    $('#tblAppendGrid').appendGrid({
        caption: 'Project Setup',
        initRows: 1,
        columns: [
            { name: 'keyId', display: '#Key', type: 'text'},
            { name: 'name', display: 'name', type: 'text'},
            { name: 'shortName', display: 'shortName', type: 'text'},
            { name: 'description', display: 'description', type: 'text'},
            { name: 'startDate', display: 'startDate', type: 'ui-datepicker', uiOption: {dateFormat: 'yy-mm-dd'}},
            { name: 'endDate', display: 'endDate', type: 'ui-datepicker', uiOption: {dateFormat: 'yy-mm-dd'}},
            { name: 'nbPhase', display: 'nbSprint', type: 'number'}
            ],
         initData:mydata
    });
    
    $('#btnSave').button().on('click', function () {
        // Get grid values in array mode
        var allData = $('#tblAppendGrid').appendGrid('getAllValue');
        alert(JSON.stringify(allData));
        $('textarea[id*="beandata"]').val(JSON.stringify(allData));
        $('input[id*="bbButonId"]').click();
    });
    
    
    
    $('#btnLoad1').button().click(function () {
    	var mydata1=$('textarea[id*="beandata"]').val();
    	try{
        	var datalocal=JSON.parse(mydata1);
        	$('#tblAppendGrid').appendGrid('load', datalocal);
        }catch(e){
        	console.error("Parsing error:", e);
        	alert("marche pas");
        }
        
    });
    
});
</script>



<title><h:outputText value="#{msg.login_titre}" /></title>
	</head>
	<body>
	<div class="ui-widget-content" style="padding: 12px;">
		<table id="tblAppendGrid" style="width:100%"></table>
    
	    <button id="btnSave" type="button">Save</button>
	    
	    <button id="btnLoad1" type="button">Load from text</button>
	</div>
	<div class="ui-widget-content">	
	<h:form id="bbForm">
		<h:inputTextarea id="beandata"  value="#{projectBeanWrapper.projectListAsJson}"></h:inputTextarea>
		<h:commandButton id="bbButonId" value="Load the file"	action="#{projectBeanWrapper.saveProject}" style="display:none"/>
	</h:form>
	</div>
	</body>
</f:view>

</html>
<%@page import="com.jmd.test.jsf.LoginBean"%>
<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<f:view>
	<f:loadBundle basename="com.lang.util.msg" var="msg" />
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<link rel="stylesheet"	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"/>
	<link rel="stylesheet" href="/resources/demos/style.css"/>
	<link rel="stylesheet" href="/ProjectManager/jtable/themes/metro/blue/jtable.min.css" type="text/css"/>
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script src="/ProjectManager/jtable/jquery.jtable.min.js" type="text/javascript"></script>
	
	</head>

	
	<body>
	<div id="profileContents"></div>
		
	</body>
		
	<script type="text/javascript">
    $(document).ready(function () {
    	
        $('#profileContents').jtable({
            title: 'Table of people',
            actions: {
                listAction: '/ProjectManager/ProfileManager?action=read',
                createAction: '/ProjectManager/ProfileManager?action=create',
                updateAction: '/ProjectManager/ProfileManager?action=update',
                deleteAction: '/ProjectManager/ProfileManager?action=delete'
            },
            fields: {
                keyId: {
                    title: 'Id',
                	key: true,
                    list: true
                },
                name: {
                    title: 'Profile Name',
                    width: '10%'
                },
                
                resources: {
                    title: '',
                    width: '5%',
                    sorting: false,
                    edit: false,
                    create: false,
                    display : function (cData){
                    	var $img = $('<img src="img/action_2downarrow.png" title="Resources details" />');
                    	$img.click( function() {
                    		alert("hello1" + cData.record.name);
                    		$('#profileContents').jtable('openChildTable',
                    				$img.closest('tr'),
                    				{
                    					title: cData.record.name + ' credentials',
                    					actions:{
                    						listAction: '/ProjectManager/ResourcesManager?action=read&id=' + cData.record.name,
                    						createAction: '/ProjectManager/ResourcesManager?action=create&id=' + cData.record.name,
                    						updateAction: '/ProjectManager/ResourcesManager?action=update&id=' + cData.record.name,
                    						deleteAction: '/ProjectManager/ResourcesManager?action=delete&id=' + cData.record.name
                    					},
                    					fields: {
                    						path:{
                    							title: 'Resource path',
                    							width:'50%',
                    		                	key: true,
                    		                    list: true,
                    		                    create:true
                    						},
                    						description: {
                    							title: 'Description',
                    		                    width: '30%'
                    						},
                    						credentials: {
                    							title: 'Credentials',
                    		                    width: '20%',
                    		                    display : function(crData){
                    		                    	var $tmpVal = crData.record.credentials;
                    		                    	return "CRUD:" + $tmpVal.rights.toString();
                    		                    }
                    						}
                    					}
                    				},
                    				function (data){                    					
                    					data.childTable.jtable('load');
                    				}
                    		);
                    		
                    	});
                    	return $img;
                    }                  
                },
                description: {
                    title: 'Description',
                    width: '20%'
                }
            }
        });
        
        $('#profileContents').jtable('load');
    });
</script>


</f:view>

</html>

<%@page import="com.jmd.test.jsf.LoginBean"%>
<%@page import="sun.applet.resources.MsgAppletViewer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
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

	<header></header>
	<body>
		<table style="width: 100%">
			<thead>
				<tr>
					<th>Profile manager view</th>
				</tr>
				<tr>
					<td>
						<div id="profileMenu">
							<ul>
								<li><a href="#profileView">Profile List</a></li>
								<li><a href="#profileCreate">Profile create</a></li>

								<li><a href="#profileHelp">Profile Help</a></li>
							</ul>
							<div id="profileView"><div id="profileContents"></div>
							</div>
							<div id="profileCreate">
								<p>Creation de profile</p>
								<iframe src="admin/uploadpage.faces"></iframe>
							</div>

							<div id="profileHelp">
								<p>Manage profiles</p>
								<iframe src=""></iframe>
							</div>
						</div>

					</td>
				</tr>
			</thead>

		</table>
		<h:inputText id="propertyId" value="#{msg.test_listaction}" />
	</body>
	<footer></footer>
	<script>
		$(document).ready(function() {
			$("#profileMenu").tabs({
				beforeLoad : function(event, ui) {
					ui.jqXHR.fail(function() {
						ui.panel.html("Couldn't load this tab. We'll try to fix this as soon as possible. "
										+ "If this wouldn't be a demo.");
									});
							}
						});
			
			
		});
	</script>
	
	<script type="text/javascript">
    $(document).ready(function () {
        $('#profileContents').jtable({
            title: 'Table of people',
            actions: {
                listAction: 'JsonDataLoader',
                createAction: '/GettingStarted/CreatePerson',
                updateAction: '/GettingStarted/UpdatePerson',
                deleteAction: '/GettingStarted/DeletePerson'
            },
            fields: {
                PersonId: {
                    title: 'Id',
                	key: true,
                    list: true
                },
                Name: {
                    title: 'Author Name',
                    width: '30%'
                },
                Age: {
                    title: 'Age',
                    width: '20%'
                },
                RecordDate: {
                    title: 'Record date',
                    width: '30%',
                    type: 'date',
                    create: false,
                    edit: false
                }
            }
        });
        $('#profileContents').jtable('load');
    });
</script>


</f:view>

</html>

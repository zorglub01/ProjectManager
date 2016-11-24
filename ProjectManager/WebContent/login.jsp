<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<f:view>
	<f:loadBundle basename="com.lang.util.msg" var="msg" />
	<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><h:outputText value="#{msg.login_titre}" /></title>

	</head>
	<body>
		<h:form>

			<table>
				<tr>
					<td><h:commandLink action="#{langueApp.activerFR}"	immediate="true">
							<h:graphicImage value="images/francais.jpg" style="border: 0px" />
						</h:commandLink></td>
					<td><h:commandLink action="#{langueApp.activerEN}"	immediate="true">
							<h:graphicImage value="images/anglais.jpg" style="border: 0px" />
						</h:commandLink></td>
					<td width="100%">&nbsp;</td>
				</tr>
			</table>

			<h3>
				<h:outputText value="#{msg.login_identification}"></h:outputText>
			</h3>
			<table>
				<tr>
					<td><h:outputText value="#{msg.login_nom}"></h:outputText></td>
					<td><h:inputText value="#{login.nom}" /></td>
				</tr>
				<tr>
					<td><h:outputText value="#{msg.login_mdp}"></h:outputText></td>
					<td><h:inputSecret value="#{login.mdp}" /></td>
				</tr>
				<tr>
					<td colspan="2"><h:commandButton value="#{msg.login_Login }" action="#{login.checkPersonne }" /></td>
				</tr>
			</table>
		</h:form>
	</body>
</f:view>

</html>

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
    var mydata1 = $('#beandata').val();
    //alert(mydata1);
    try{
    	var mydata=JSON.parse(mydata1);
    }catch(e){
    	console.error("Parsing error:", e);
    	alert("marche pas from bean");
    }
    
    //alert(mydata);
    $('#tblAppendGrid').appendGrid({
        caption: 'Profiles DB file',
        initRows: 1,
        columns: [
            { name: 'localFile', display: 'file name', type: 'text',  ctrlCss: { width: '100%'}},
            { name: 'hrefLocation', display: 'Url', type: 'text',  ctrlCss: { width: '100%'}}
            ],
        initData: mydata,
        hideRowNumColumn: true,
        customRowButtons: [
            {
                uiButton: { icons: { primary: 'ui-icon-download' }, label: 'Export' },
                click: setFavorite, btnCss: { 'min-width': '30px' },
                btnAttr: { title: 'Export data!' }, atTheFront: true
            }
        ],
        hideButtons: {
            append:true,
        	remove: true,
            removeLast: true,
            insert:true,
            moveUp:true,
            moveDown:true
        }
    });
    
    function setFavorite(evtObj, uniqueIndex, rowData) {
        alert('I know you love this album now :)' + rowData.hrefLocation);
        window.open(rowData.hrefLocation);
        // Check the caller button exist in event object
        //if (evtObj && evtObj.target) {
            // Do something on the button, such as disable the button
           // $(evtObj.target).button('disable');
        //}
    }
    
    $('#btnSave').button().on('click', function () {
        // Get grid values in array mode
        var allData = $('#tblAppendGrid').appendGrid('getAllValue');
        alert(JSON.stringify(allData));
    });
    
    $('#btnLoad').button().click(function () {
    	var datalocal=[
            { 'localFile': 'C:\\Test\\file.xml'}
        ];
        $('#tblAppendGrid').appendGrid('load', datalocal);
    });
    
    $('#btnLoad1').button().click(function () {
    	var mydata1=$('#beandata').val();
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
		<h:form enctype="multipart/form-data">
			
			
			<h:outputText value="Here is a File upload example." />
			<h:outputText   value="DB binding scope : #{uploadFileBean.scope}"/>
			<t:inputFileUpload value="#{uploadFileBean.uploadedFile}" size="20" />
			<h:commandButton value="Load the file"	action="#{uploadFileBean.upload}" >
				<f:attribute name="scope" value="#{uploadFileBean.scope}"/>
			</h:commandButton>
			<t:outputText value="File Uploaded Successfully."
				rendered="#{uploadFileBean.success}"
				style="color:green;font-weight:bold" />
			<t:outputText value="Error in File Uploading."
				rendered="#{uploadFileBean.failure}"
				style="color:red;font-weight:bold" />
		</h:form>
	
	<table id="tblAppendGrid" style="width:100%">
    </table>
    <button id="btnSave" type="button">Save</button>
    <button id="btnLoad" type="button">Load</button>
    <button id="btnLoad1" type="button">Load from text</button>
	</div>	
	<h:inputTextarea id="beandata"  value="#{uploadFileBean.fileScan}"></h:inputTextarea>
	</body>
</f:view>

</html>
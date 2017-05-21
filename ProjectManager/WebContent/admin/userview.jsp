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
	    <!-- The jQuery library is a prerequisite for all jqSuite products -->
    <script type="text/ecmascript" src="/jsSamples/guiriddo/js/jquery.min.js"></script> 
    <script type="text/ecmascript" src="/jsSamples/guiriddo/js/jquery-ui.min.js"></script> 
    <!-- This is the Javascript file of jqGrid -->   
    <script type="text/ecmascript" src="/jsSamples/guiriddo/js/trirand/src/jquery.jqGrid.js"></script>
    <!-- This is the localization file of the grid controlling messages, labels, etc.
    <!-- We support more than 40 localizations -->
    <script type="text/ecmascript" src="/jsSamples/guiriddo/js/trirand/i18n/grid.locale-en.js"></script>
    <!-- A link to a jQuery UI ThemeRoller theme, more than 22 built-in and many more custom -->
    <link rel="stylesheet" type="text/css" media="screen" href="/jsSamples/guiriddo/css/jquery-ui.css" />
    <!-- The link to the CSS that the grid needs -->
    <link rel="stylesheet" type="text/css" media="screen" href="/jsSamples/guiriddo/css/trirand/ui.jqgrid.css" />
 	<meta charset="utf-8" />
	

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
    
    
    function setDatePicker(element,elmId) {
        $(element).datepicker({
            id: elmId,
            dateFormat: 'yy-mm-dd',
            //minDate: new Date(2010, 0, 1),
            maxDate: new Date(2020, 0, 1),
            showOn: 'focus',
            onClose: dpOnclose
        });
    }
    
 
    function getParentRowId(subGrid_id){
    	var _subPart = subGrid_id.split('_');
    	return _subPart[0];
    	
    }
    
 
    function stringToDate(strDate){
    	var parts=strDate.split('-');
    	var _res = new Date(parts[0],parts[1]-1,parts[2]);
    	return _res;
    }
    
    function getProfileValues(){
    	var _profiles = $('#jqGridProfile').jqGrid('getRowData');
    	var _res=[];
    	_profiles.forEach(function(_profile){
    		_res.push( _profile.name+':'+_profile.name);
    	}); 
    	return _res.join(';');
    }
    
    function getProfileJsonMap(data){
    	var _res="";
    	if(data.profile)
    		_res = data.profile[0].name;
    	return _res;
    }
    
    $("#jqGrid").jqGrid({
        datatype: "jsonstring",
		datastr: mydata,
		editurl: 'clientArray',
		rowNum:10,
        height: 500,
		width: 800,
        colModel: [
            { label: '#UserId', name: 'keyId', width: 75, key:true },
            { label: 'Login', name: 'login', width: 90, editable: true },
            { label: 'Pwd', name: 'password',width: 80,editable: true,edittype:"text" },
            { label: 'Name', name: 'firstName', width: 80, editable: true, 	edittype:"text"},
            { label: 'Email', name: 'email', width: 140, formatter:'email',editable: true},
            {label:'Profile',editable:true,name:'profile_data', jsonmap:getProfileJsonMap, 
            	edittype:'select',
            	editoptions: {value:getProfileValues},
            	editrules:{
                	required:true
                }
            }
            	
        ],
        viewrecords: true, // show the current page, data rang and total records on the toolbar
        loadonce : true,
        //onSelectRow: editRow, 
        caption: "User settings",
        //afterInsertRow : computSubGridTotal,
        //gridComplete: checkgridComplete,
        
        pager: "#jqGridPager"
    });
    
    $('#jqGrid').navGrid('#jqGridPager',
            // the buttons to appear on the toolbar of the grid
            { edit: true, add: true, del: true, search: true, refresh: false, view: false, position: "left", cloneToTop: false },
            // options for the Edit Dialog
            {
                editCaption: "The Edit Dialog",
                recreateForm: true,
				checkOnUpdate : true,
				checkOnSubmit : true,
                closeAfterEdit: true,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            },
            // options for the Add Dialog
            {
                closeAfterAdd: true,
                recreateForm: true,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            },
            // options for the Delete Dailog
            {
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            });
    
  
    $("#jqGridProfile").jqGrid({
        datatype: "json",
		url: '/ProjectManager/ProfileManager?action=read',
		rowNum:10,
        height: 200,
		width: 240,
        colModel: [
            { label: '#Id', name: 'keyId', width: 10, key:true },
            { label: 'Name', name: 'name', width: 80},
            { label: 'Description', name: 'description',width: 150}
        ],
        viewrecords: true, // show the current page, data rang and total records on the toolbar
        loadonce : true,
        //onSelectRow: editRow, 
        caption: "Available Profiles",
        jsonReader : {root:'Records'}
    });
    
    
     
    $('#btnSave').button().click(function () {        
        var allData = $('#jqGrid').jqGrid('getGridParam','data');
        $('textarea[id*="beandata"]').val(JSON.stringify(allData));
        $('input[id*="bbButonIdPhase"]').click();
    });

});
</script>



<title><h:outputText value="#{msg.login_titre}" /></title>
	</head>
	<body>
		
    	
    	<table style="width:100%">
    	<tr>
    		<td>
    			<table id="jqGrid"></table>
    			<div id="jqGridPager"></div>
    		</td>
    		<td style="text-align:end; vertical-align:top">
    			<table id="jqGridProfile"></table>
    		</td>
    	</tr>
    	<tr>
    		<td></td>
    		<td style="text-align:end"><button id="btnSave" type="button" >Save</button></td>
    	</tr>
    	</table>
    	
		
	  
	<div class="ui-widget-content" >	
	<h:form id="bbForm">
		<t:outputText value="#{userManagerBean.errorMessage}"	rendered="#{userManagerBean.asError}"	style="color:red;font-weight:bold" />
		<h:inputTextarea id="beandata"  value="#{userManagerBean.usersAsJson}" style="display:none"></h:inputTextarea>
		<h:commandButton id="bbButonIdPhase" value="SaveToBean"	action="#{userManagerBean.saveUsers}" style="display:none"/>
	</h:form>
	</div>
	
	</body>
</f:view>

</html>
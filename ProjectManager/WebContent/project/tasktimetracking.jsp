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
<link rel="stylesheet" type="text/css"	href="/ProjectManager/script/jquery/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css"	href="/ProjectManager/script/jquery/jquery-ui.theme.min.css" />
<link rel="stylesheet" type="text/css" href="/ProjectManager/script/jquery/jquery-ui.theme.alternate.min.css"/>
<script type="text/javascript"	src="/ProjectManager/script/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript"	src="/ProjectManager/script/jquery/jquery-ui-1.11.1.min.js"></script>

<link rel="stylesheet" type="text/css"	href="/ProjectManager/jquery.appendGrid-1.6.2.css" />
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
    
    function convertToTaskArray(myArray) {
    	  var newArray = [];
    	  $.each(myArray, function(index, element) {
    	    newArray.push({
    	    id:  element.id,
    	    name: element.name,
    	      description: element.description,
    	      wlInf : element.metric.estimate.wlInf,
    	      wlSup : element.metric.estimate.wlSup,
    	      startDate: element.metric.estimate.startDate,
    	      endDate: element.metric.estimate.endDate,
    	      currentState:element.status.currentState,
    	      prevState:element.status.prevState,
    	      lastBEntry:element.trackEntries.budgetLine.track[0].progressFactor,
    	      lastPEntry:element.trackEntries.progressLine.track[0].progressFactor,
    	      budgetLine:element.trackEntries.budgetLine,
    	      progressLine:element.trackEntries.progressLine,
    	    });
    	  });
    	  return newArray ;
    }
    
    
    $('#tblTaskTracking').appendGrid({
        caption: 'Task tracker',
        initRows: 1,
        hideRowNumColumn: true,
        hideButtons: {
            append:true,
            removeLast: true,
            remove: true,
            insert:true,
            moveUp:true,
            moveDown:true
        },
        columns: [
        	{ name: 'SprintId', display: '#Sprint', type: 'text',ctrlCss: { width: '100%'},invisible: true},
            { name: 'SprintName', display: 'Sprint name', type: 'text',  ctrlCss: { width: '100%'},ctrlAttr: { 'readonly':true}},
            { name: 'taskId', display: '#TId', type: 'text', ctrlCss: { width: '100%'},invisible: true},
            { name: 'taskName', display: 'TaskName', type: 'text', ctrlCss: { width: '100%'},ctrlAttr: { 'readonly':true}},
            { name: 'taskDesc', display: 'Desc', type: 'text', ctrlCss: { width: '100%'},ctrlAttr: { 'readonly':true}, resizable: true},
            { name: 'taskWl', display: 'WL', type: 'number',ctrlCss: { width: '50px'},ctrlAttr: { 'readonly':true}},
            { name: 'taskWlConsumed', display: 'WLConsumed', type: 'number',ctrlCss: { width: '50px'},ctrlAttr: { 'readonly':true}}
            ],
        initData: mydata,
        
        customRowButtons: [
            {
                uiButton:  { icons: { primary: 'ui-icon-star' }, label: 'Hide' },
                click: showHideSprint, btnCss: { 'min-width': '10px' },
                btnAttr: { title: 'Show Hide task time entries' }, atTheFront: false
            }
        ],
        useSubPanel: true,
        subPanelBuilder: function (cell, uniqueIndex) {
            // Create a table object and add to sub panel
            
            var subgrid = $('<table></table>').attr('id', 'tblSubGridTask_' + uniqueIndex).appendTo(cell);
            // Optional. Add a class which is the CSS scope specified when you download jQuery UI
            subgrid.addClass('alternate');
            // Initial the sub grid
            subgrid.appendGrid({
                initRows: 0,
                hideRowNumColumn: true,
                maxBodyHeight: 300,
                maintainScroll: true,
                hideButtons: {
                    append:true,
                    removeLast: true,
                	remove: false,                    
                    insert:false,
                    moveUp:true,
                    moveDown:true
                },
                
                columns: [
                	
                    { name: 'taskOwner', display: 'Owner',invisible: false, value:'NONE'},
                    { name: 'trackTime', display: 'time',type: 'ui-datepicker',uiOption: {dateFormat: 'yy-mm-dd'},
                    	value: new Date().toISOString().split("T")[0],ctrlCss: { width: '100px'}
                    },
                    { name: 'progressFactor', display: 'WLConsumed',type: 'number', value:1,ctrlCss: { width: '50px'}}
                ]
                
            });
        },
        subPanelGetter: function (uniqueIndex) {
            // Return the sub grid value inside sub panel for `getAllValue` and `getRowValue` methods
            var _subGirdVal = $('#tblSubGridTask_' + uniqueIndex).appendGrid('getAllValue');
            var _resObj = {taskWlEntries:[]};
            _subGirdVal.forEach(function(cTask){
            	_resObj.taskWlEntries.push(cTask);
            });
            
            return _resObj;
        },
        rowDataLoaded: function (caller, record, rowIndex, uniqueIndex) {
            // Check SubGridData exist in the record data
           
            if (record.taskWlEntries) {
                // Fill the sub grid
                $('#tblSubGridTask_' + uniqueIndex, caller).appendGrid('load', record.taskWlEntries);
            } 
        }
    });
    

  
    
    function showHideSprint(evtObj, uniqueIndex, rowData){
    	$('#tblSubGridTask_' + uniqueIndex+'-wrapper').toggle();
    	if(evtObj && evtObj.target){
    		var _btnText = $(evtObj.target).text();
    		if(_btnText == 'Hide'){
    			$(evtObj.target).text('Show');
    		}else{
    			$(evtObj.target).text('Hide');
    		}
    		
    	}
    	
    }

    
    $('#btnSave').button().click(function () {
        // Get grid values in array mode
        var allData = $('#tblTaskTracking').appendGrid('getAllValue');
        //alert(JSON.stringify(allData));
        $('textarea[id*="beandata"]').val(JSON.stringify(allData));
        $('input[id*="bbButonIdPhase"]').click();
    });
    
    
    
    $('#btnLoad1').button().click(function () {
    	var mydata1=$('textarea[id*="beandata"]').val();
    	try{
        	var datalocal=JSON.parse(mydata1);
        	$('#tblTaskTracking').appendGrid('load', datalocal);
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
		
		<h:outputText value="#{msg.project_sdate_label}" /><h:inputText id="bbProjSDate" value="#{projectBeanWrapper.currentProjectStartDate}"></h:inputText>
		<h:outputText value="#{msg.project_edate_label}" /><h:inputText id="bbProjEDate" value="#{projectBeanWrapper.currentProjectEndDate}"></h:inputText>
		<h:outputText value="Budget:" /><h:outputText id="bbProjWL" value="#{projectBeanWrapper.budjet}"></h:outputText>
		<table id="tblTaskTracking" style="width:100%"></table>    
	    <button id="btnSave" type="button">Save</button>
	    
	    
		</div>
	<div class="ui-widget-content">	
	<h:form id="bbForm">
		<t:outputText value="#{projectBeanWrapper.errorMessage}"	rendered="#{projectBeanWrapper.asError}"	style="color:red;font-weight:bold" />
		<h:inputText id="bbProjName" value="#{projectBeanWrapper.currentProject }"></h:inputText>
		<h:inputTextarea id="beandata"  value="#{projectBeanWrapper.taskTrackingAsJson}"></h:inputTextarea>
		<h:commandButton id="bbButonIdPhase" value="SaveToBean"	action="#{projectBeanWrapper.saveTracking}" style="display:none"/>
	</h:form>
	</div>
	
	</body>
</f:view>

</html>
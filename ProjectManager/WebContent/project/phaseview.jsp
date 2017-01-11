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
    
    var wlOptions=[
    	{label:'1 m.h', value:1, group:'ManHours'},
    	{label:'2 m.h', value:2, group:'ManHours'},
    	{label:'3 m.h', value:3, group:'ManHours'},
    	{label:'5 m.h', value:5, group:'ManHours'},
    	{label:'1 m.d', value:8, group:'ManDays', title:'ie 8 m.h'},
    	{label:'2 m.d', value:16, group:'ManDays',title:'ie 16 m.h'},
    	{label:'3 m.d', value:24, group:'ManDays',title:'ie 24 m.h'},
    	{label:'5 m.d', value:40, group:'ManDays',title:'ie 40 m.h'},
    	{label:'8 m.d', value:64, group:'ManDays',title:'ie 64 m.h'},
    	{label:'13 m.d', value:104, group:'ManDays',title:'ie 104 m.h'}
    ];
    
    var stateOptions=[
    	{label:'NEW', value:"NEW"},
    	{label:'ASSIGNED', value:"ASSIGNED"},
    	{label:'INPROGRESS', value:"INPROGRESS"},
    	{label:'PENDING', value:"PENDING"},
    	{label:'CLOSED', value:"CLOSED"},
    	{label:'REJECTED', value:"REJECTED"}
    ];
    
    //alert(mydata);
    $('#tblAppendGrid').appendGrid({
        caption: 'Project Sprint list',
        initRows: 1,
        columns: [
            { name: 'name', display: 'Sprint name', type: 'text',  ctrlCss: { width: '100%'}},
            { name: 'startDate', display: 'begin', type: 'ui-datepicker', uiOption: {dateFormat: 'yy-mm-dd'}, onChange:checkSprintEndDate},
            { name: 'endDate', display: 'end', type: 'ui-datepicker', uiOption: {dateFormat: 'yy-mm-dd'}, onChange:checkSprintStartDate},
            { name: 'workload', display: 'Budget', type: 'number'}
            ],
        initData: mydata,
        customRowButtons: [
            {
                uiButton: { icons: { primary: 'ui-icon-circle-plus' }, label: 'Collapse' },
                click: showHideSprint, btnCss: { 'min-width': '20px' },
                btnAttr: { title: 'Show Hide task list' }, atTheFront: false
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
                columns: [
                    { name: 'name', display: 'task name'},
                    { name: 'description', display: 'task desc',type:'text', value: 'def desc' },
                    { name: 'metric.estimate.wlInf', display: 'wlInf',type: 'select',
                    	ctrlOptions: wlOptions, onChange: checkWLSup, value:1
                    },
                    { name: 'metric.estimate.wlSup', display: 'wlSup',type: 'select',
                    	ctrlOptions: wlOptions, onChange: checkWLInf, value:1	
                    },                    
                    { name: 'metric.estimate.startDate', display: 'StartDate',type: 'ui-datepicker', 
                    	uiOption: {dateFormat: 'yy-mm-dd'},
                    	onChange: checkEndDate,
                    	value: new Date().toISOString().split("T")[0]
    
                    },
                    { name: 'metric.estimate.endDate', display: 'EndDate',type: 'ui-datepicker', 
                    	uiOption: {dateFormat: 'yy-mm-dd'},
                    	onChange: checkStartDate,
                    	value: new Date().toISOString().split("T")[0]
                    },
                    { name: 'status.currentState', display: 'Status',type: 'select',                     	
                    	ctrlOptions: stateOptions,
                    	value:'NEW'
                    },
                    { name: 'status.prevState', display: 'prevStatus',type: 'select',                     	
                    	ctrlOptions: stateOptions,
                    	value:'NEW'
                    }
                ]
                
            });
        },
        subPanelGetter: function (uniqueIndex) {
            // Return the sub grid value inside sub panel for `getAllValue` and `getRowValue` methods
            var _subGirdVal = $('#tblSubGridTask_' + uniqueIndex).appendGrid('getAllValue');
            var _resObj = {taskList:{task:[]}}
            _subGirdVal.forEach(function(cTask){
            	var taskJson = {
            			"name": cTask.name,
						"description": cTask.description,
						"metric": {
							"description": "Metric desc",
							"estimate": {
								"wlInf": cTask['metric.estimate.wlInf'],
								"wlSup": cTask['metric.estimate.wlSup'],
								"startDate": cTask['metric.estimate.startDate'],
								"endDate": cTask['metric.estimate.endDate']
							},
							"keyId": "0"
						},
						"status": {
							"prevState": cTask['status.prevState'],
							"currentState": cTask['status.currentState'],
							"upDate": "2001-01-01"
						},
						"id": "t1"
            	}
            	_resObj.taskList.task.push(taskJson);
            });
            
            return _resObj;
        },
        rowDataLoaded: function (caller, record, rowIndex, uniqueIndex) {
            // Check SubGridData exist in the record data
            var sWL=0;
            if (record.taskList.task) {
                // Fill the sub grid
                $('#tblSubGridTask_' + uniqueIndex, caller).appendGrid('load', record.taskList.task);
                
                record.taskList.task.forEach(function(cTask){
                	var _tmpVal = cTask.metric.estimate.wlSup;
                	sWL += parseInt(_tmpVal);
                });
            }
           $(caller).appendGrid('setCtrlValue','workload',rowIndex,sWL); 
        }
        
    });
    
    function showHideSprint(evtObj, uniqueIndex, rowData){
    	$('#tblSubGridTask_' + uniqueIndex).toggle();
    }

    function checkSprintStartDate(evt, rowIndex){
    	var endDate = evt.target;
    	var startDate = $('#tblAppendGrid').appendGrid('getCellCtrl', 'startDate', rowIndex);
    	 
    	if( endDate.value < startDate.value) {
    		$('#tblAppendGrid').appendGrid('setCtrlValue', 'startDate', rowIndex,endDate.value );
    	}
    }    

    function checkSprintEndDate(evt, rowIndex){
    	var startDate = evt.target;
    	var  endDate = $('#tblAppendGrid').appendGrid('getCellCtrl', 'endDate', rowIndex);
    	 
    	if( endDate.value < startDate.value) {
    		$('#tblAppendGrid').appendGrid('setCtrlValue', 'endDate', rowIndex,startDate.value );
    	}
    }
    
    function checkStartDate(evt, rowIndex){
    	var _callId = this.caller.id;
    	var endDate = evt.target;
    	var startDate = $('#'+_callId).appendGrid('getCellCtrl', 'metric.estimate.startDate', rowIndex);
    	 
    	if( endDate.value < startDate.value) {
    		$('#'+_callId).appendGrid('setCtrlValue', 'metric.estimate.startDate', rowIndex,endDate.value );
    	}
    }

    function checkEndDate(evt, rowIndex){
    	var _callId = this.caller.id;
    	var startDate = evt.target;
    	var  endDate = $('#'+_callId).appendGrid('getCellCtrl', 'metric.estimate.endDate', rowIndex);
    	 
    	if( endDate.value < startDate.value) {
    		$('#'+_callId).appendGrid('setCtrlValue', 'metric.estimate.endDate', rowIndex,startDate.value );
    	}
    }

    
    
    
    function checkWLInf(evt, rowIndex){
    	var _callId = this.caller.id;
    	var wlSup = evt.target;
    	var wlInf = $('#'+_callId).appendGrid('getCellCtrl', 'metric.estimate.wlInf', rowIndex);
    	var iwlInf =+wlInf.value;
    	var iwlSup = +wlSup.value; 
    	if( iwlSup < iwlInf) {
    		$('#'+_callId).appendGrid('setCtrlValue', 'metric.estimate.wlInf', rowIndex,wlSup.value);
    	}
    }

    function checkWLSup(evt, rowIndex){
    	var _callId = this.caller.id;
    	var wlInf = evt.target;
    	var wlSup = $('#'+_callId).appendGrid('getCellCtrl', 'metric.estimate.wlSup', rowIndex);
    	var iwlInf =+wlInf.value;
    	var iwlSup = +wlSup.value; 
    	if( iwlSup < iwlInf) {
    		$('#'+_callId).appendGrid('setCtrlValue', 'metric.estimate.wlSup', rowIndex,wlInf.value);
    	}
    }
    
    
    $('#btnSave').button().click(function () {
        // Get grid values in array mode
        var allData = $('#tblAppendGrid').appendGrid('getAllValue');
        //alert(JSON.stringify(allData));
        $('textarea[id*="beandata"]').val(JSON.stringify(allData));
        $('input[id*="bbButonIdPhase"]').click();
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
		<h:outputText value="#{msg.sprint_project_label}" /><h:inputText id="bbProjName" value="#{projectBeanWrapper.currentProject }"></h:inputText>
		<h:outputText value="#{msg.project_sdate_label}" /><h:inputText id="bbProjSDate" value="#{projectBeanWrapper.currentProjectStartDate}"></h:inputText>
		<h:outputText value="#{msg.project_edate_label}" /><h:inputText id="bbProjEDate" value="#{projectBeanWrapper.currentProjectEndDate}"></h:inputText>
		<h:outputText value="Budget" /><h:inputText id="bbProjWL" value=""></h:inputText>
		<table id="tblAppendGrid" style="width:100%"></table>    
	    <button id="btnSave" type="button">Save</button>
	    <button id="btnLoad1" type="button">Load from text</button>
		</div>
	<div class="ui-widget-content">	
	<h:form id="bbForm">
		<t:outputText value="#{projectBeanWrapper.errorMessage}"	rendered="#{projectBeanWrapper.asError}"	style="color:red;font-weight:bold" />
		<h:inputTextarea id="beandata"  value="#{projectBeanWrapper.projectPhases}"></h:inputTextarea>
		<h:commandButton id="bbButonIdPhase" value="SaveToBean"	action="#{projectBeanWrapper.savePhase}" style="display:none"/>
	</h:form>
	</div>
	
	</body>
</f:view>

</html>
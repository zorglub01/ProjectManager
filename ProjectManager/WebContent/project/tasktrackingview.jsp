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
    
    
    function subPanelGetter (uniqueIndex) {
        // Return the sub grid value inside sub panel for `getAllValue` and `getRowValue` methods
        var _subGirdVal = $('#'+uniqueIndex).jqGrid('getGridParam','data');
        var _resObj = {task:[]}
        _subGirdVal.forEach(function(cTask){
        	var taskJson = {
        			"name": cTask.name,
					"description": cTask.description,
					"metric": {
						"description": "Metric desc",
						"estimate": {
							"wlInf": cTask.t_wlInf,
							"wlSup": cTask.t_wlSup,
							"startDate": cTask.t_startDate,
							"endDate": cTask.t_endDate
						},
						"keyId": "0"
					},
					"status": {
						"prevState": cTask.t_prevState,
						"currentState": cTask.t_currentState,
						"upDate": "2001-01-01"
					},
					"id": cTask.id
        	}
        	_resObj.task.push(taskJson);
        });
        return _resObj;
    }
    

    
    function getWlValues(){
    	return "1:1 mh;2:2 mh;3:3 mh;5:5 mh;8:1 md;16:2 md;24:3 md;40: 5 md;64:8 md;104:13 md";
    }
    
    function getStatusValues(){
    	return "NEW:NEW;ASSIGNED:ASSIGNED;INPROGRESS:INPROGRESS;PENDING:PENDING;CLOSED:CLOSED;REJECTED:REJECTED";
    }

    function getParentRowId(subGrid_id){
    	var _subPart = subGrid_id.split('_');
    	return _subPart[0];
    	
    }
    
    
    

    
    function navButtonCopyTask(){
    	var _subGridIdx = $(this)[0].id;
    	var _currentRowid = $('#'+_subGridIdx).jqGrid('getGridParam','selrow');
    	var _nbTask = $('#'+_subGridIdx).jqGrid('getGridParam','records');
    	if(_currentRowid){
    		var _dataToCopy = $('#'+_subGridIdx).jqGrid('getRowData',_currentRowid);
    		_dataToCopy.id="";
    		_nbTask++;
    		_dataToCopy.name=_dataToCopy.name +"_copy"+_nbTask;
    		$('#'+_subGridIdx).jqGrid('addRowData','copy_'+_currentRowid,_dataToCopy);
    	}else{
    		alert("Select a task for copy");
    	}
    }
    
    
    function checkgridComplete(){
    	var _subGridIdx = $(this)[0].id;
    	var _sumSprint = sumCol(_subGridIdx,'t_wlSup');
    	var _parentRowId = getParentRowId(_subGridIdx);
    	$('#jqGrid').jqGrid('setCell',_parentRowId,'Budget',_sumSprint,'','',true);
    	//alert("Sprint/Budget :" + _subGridIdx +'-'+_sumSprint);
    	
    }
    
    function sumCol( idGrid,colName){
    	var _subGirdVal = $('#'+idGrid).jqGrid('getGridParam','data');
    	var _res=0;
    	_subGirdVal.forEach(function(cTask){
    		var ival = +cTask[colName];
    		_res+=ival ;
    	});
    	return _res;
    }
    
    
    function fillDefaultStatus(cellvalue, options, rowObject){
    	var _res = "NEW";
    	if(cellvalue) _res=cellvalue;
    	return _res;
    }
    

    function validateWLInf(value,colum){
    	
        var wlSup = $('select[id="t_wlSup"]')[0].value
        if(value && wlSup){
        	var ivalue =+value;
        	var iwlSup =+wlSup;
        	if(ivalue > iwlSup){
        		$('select[id="t_wlSup"]')[0].value = ivalue;
        		return [false,"Wokload inf greater than workload sup. WL SUp corrected"];
        	}
        	else
        		return [true,""];
        }
    }
    
    function validateWLSup(value,colum){
        var wlInf = $('select[id="t_wlInf"]')[0].value
        if(value && wlInf){
        	var ivalue =+value;
        	var iwlInf =+wlInf;
        	if(iwlInf > ivalue){
        		$('select[id="t_wlInf"]')[0].value = ivalue
        		return [false,"Wokload sup lower than workoad inf. WL Inf corrected"];
        	}
        	else
        		return [true,""];
        }
    }
    
    
    function validateStartDate(value,colum){
        var endDate = $('input[id="t_endDate"]')[0].value 
        if(endDate && value){
        	if(endDate < value){
        		$('input[id="t_endDate"]').val(value);
        		return [false,"Start date greater than end date. Autocorrected"];
        	}
        	else
        		return [true,""];
        }
        
    }
    
    function stringToDate(strDate){
    	var parts=strDate.split('-');
    	var _res = new Date(parts[0],parts[1]-1,parts[2]);
    	return _res;
    }
    
    
    function validateEndDate(value,colum){
    	
        var startDate = $('input[id="t_startDate"]')[0].value 
        if(startDate && value){
        	if(value < startDate ){
        		$('input[id="t_endDate"]').val(startDate);
        		return [false,"End date lower than start date, Auto corrected"];
        	}
        	else
        		return [true,""];
        }
        
    }
   
    
    
    
    function showChildGrid(parentRowID, parentRowKey) {
        var childGridID = parentRowKey + "_table";
        var childGridPagerID = parentRowKey + "_pager";

        // send the parent row primary key to the server so that we know which grid to show
        var _locData = $("#jqGrid").getGridParam('data');
        var result = $.grep(_locData, function(element, index) {
   			return (element.taskId === parentRowKey);
		});
        var childGridURL =[];
        if(result.length > 0)  	childGridURL = result[0].taskWlEntries;
        
 
        // add a table and pager HTML elements to the parent grid row - we will render the child grid here
        $('#' + parentRowID).append('<table id=' + childGridID + '></table>');

        $("#" + childGridID).jqGrid({
            datastr: childGridURL  ,
            datatype: "jsonstring",
            editurl: 'clientArray',
            //onSelectRow: editSubRow, 
            page: 1,
            colModel: [
            	{ label: 'trackTimeId', name: 'trackTimeId',key:true},
                { label: 'trackTime', name: 'trackTime'},
                { label: 'taskOwner', name: 'taskOwner'},
                { label: 'progressFactor', name: 'progressFactor'}
            ],
            viewrecords: true,
			loadonce: true,
            width: '100%',
            height: '100%',
            rowNum : 10
        });
    }
    
    
    $("#jqGrid").jqGrid({
        datatype: "local",
		data: mydata,
		editurl: 'clientArray',
		rowNum:10,
        height: 400,
		width: 1100,
        colModel: [
        	{ label: 'taskId', name: 'taskId', width: 75, key:true,editable: false },
            { label: '#Sprint', name: 'SprintId', width: 75, editable: false },
            { label: 'SprintName', name: 'SprintName', width: 90, editable: false},
            { label: 'taskName', name: 'taskName', width: 75, editable: false },
            { label: 'taskDesc', name: 'taskDesc', width: 75, editable: false },
            { label: 'WL Estimate', name: 'taskWl', width: 75, editable: false },
            { label: 'WL Consumed', name: 'taskWlConsumed', width: 75, editable: false }
        ],
        viewrecords: true, // show the current page, data rang and total records on the toolbar
        loadonce : true,
        //onSelectRow: editRow, 
        caption: "Task Tracking View",
        //afterInsertRow : computSubGridTotal,
        //gridComplete: checkgridComplete,
        subGrid: true, // set the subGrid property to true to show expand buttons for each row
        subGridRowExpanded: showChildGrid, // javascript function that will take care of showing the child grid
	    subGridOptions : {
			// load the subgrid data only once
			// and the just show/hide
			reloadOnExpand :false,
			// select the row when the expand column is clicked
			selectOnExpand : true,
			expandOnLoad: true
		},
        pager: "#jqGridPager"
    });
    
    $('#jqGrid').navGrid('#jqGridPager',
            // the buttons to appear on the toolbar of the grid
            { search: true, refresh: true, view: false, position: "left", cloneToTop: false },
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
    
  
     
    $('#btnSave').button().click(function () {
        var allData = [];
        var _rowIds = $("#jqGrid").getDataIDs();
        _rowIds.forEach(function(rowId){
        	$("#jqGrid").restoreRow(rowId);
        	var _mainVal = $("#jqGrid").getRowData(rowId);
        	var _subVal = subPanelGetter(rowId +'_table');
        	_mainVal.taskList = _subVal;
        	allData.push(_mainVal);
        }); 
        
        $('textarea[id*="beandata"]').val(JSON.stringify(allData));
        //$('input[id*="bbButonIdPhase"]').click();
    });
    
});
</script>



<title><h:outputText value="#{msg.login_titre}" /></title>
	</head>
	<body>
		<div style="float:right">
    		<button id="btnSave" type="button" >Save</button>
    	</div>
		<table id="jqGrid"></table>
    	<div id="jqGridPager"></div>
    	
		
	  
	<div class="ui-widget-content" style="position:relative">	
	<h:form id="bbForm">
		<t:outputText value="#{projectBeanWrapper.errorMessage}"	rendered="#{projectBeanWrapper.asError}"	style="color:red;font-weight:bold" />
		<h:inputTextarea id="beandata"  value="#{projectBeanWrapper.taskTrackingAsJson}" style="display:true"></h:inputTextarea>
		<h:inputTextarea id="bbCurrentProject"  value="#{projectBeanWrapper.currentProject }" style="display:true"></h:inputTextarea>
		<h:commandButton id="bbButonIdPhase" value="SaveToBean"	action="#{projectBeanWrapper.saveTracking}" style="display:true"/>
	</h:form>
	</div>
	
	</body>
</f:view>

</html>
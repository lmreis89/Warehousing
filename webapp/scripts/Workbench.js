var hasResponse;
function initPanels()
{
	$("#executeStart").jqxButton({
		width : '81',
		height : '27'
	});

	$("#slicerfilter").jqxButton({
		width : '81',
		height : '27'
	});

	$('#jqxTreeMeasures').jqxTree({
		height : '320px',
		hasThreeStates : false,
		checkboxes : true,
		width : '298px',
		enableHover : false
	});

	$('#navigationStart').jqxNavigationBar({
		width : '300px',
		expandAnimationDuration : 3000,
		collapseAnimationDuration : 500
	});

	$("#jqxMenu").jqxMenu({
		width : '1022px',
		height : '30px'
	});
	
	$('#jqxTabs').jqxTabs({ width: 695, position: 'top', selectionTracker: true, animationType: 'fade' });

	$("#jqxMenu").css('visibility', 'visible');

	$('#sliceWindow').hide();

	$('#jqxTreeMeasures .jqx-tree-dropdown-root').children().each(function() {
		$(this).children().each(function() {
			if ($(this).attr('class') == 'chkbox jqx-widget jqx-checkbox') {
				$(this)	.remove();
			}
		});
	});

	$('#jqxTreeMeasures .chkbox.jqx-widget.jqx-checkbox').bind('click',function(event) {
		var checked;
		$(this).children().children().children().each(function() {
			checked = $(this).attr('class');
		});

		if (checked == "jqx-checkbox-check-checked") {
			$(this).parent().attr('valid',"");

			$(this).parent().parent().children().each(function() {

				if ($(this).attr('valid') == null) {
					$('#jqxTreeMeasures').jqxTree('checkItem',this,false);
				}

			});

			$(this).parent().removeAttr('valid');
		}

	});

	$('#jqxDockMain').jqxDockPanel({
		width : '1024',
		height : '530'
	});

	$('#navigationStart').bind('collapsingItem', function(event){
		handleNavigation(event);
	});


	$('#executeStart').bind('click', function() {
		drill();
	});

	$('#popup').hide();

	$('#popStatus').hide();
	
	$('#slicerfilter').bind('click',handleWindow);
	
	$('#status').bind('click',function(){
		$('#popStatus').show();
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/pageInfo.jsp",
			success: function(data){

				$('#popStatusContent').empty();
				$('#popStatusContent').append(data);
				$('#popStatus').jqxWindow({showCollapseButton: true, maxHeight: 450, maxWidth: 830, minHeight: 100, minWidth: 100, height: 600, width: 600,isModal:true, modalOpacity:0.4});

				$('#popStatus').bind('closed',function(){
					$('#popStatus').remove();
					$('#jqxWidgetStart').append($('<div id="popStatus"></div>').append('<div id="popStatusHeader"><span>Current Query</span></div><div id="popStatusContent"></div>'));
					$('#popStatus').hide();
				});
			}
		});
	});
	
	$('#popSave').hide();
	
	
	$('#save').bind('click',function(){
		$('#popSave').show();
		$('#popSave').jqxWindow({showCollapseButton: true, maxHeight: 450, maxWidth: 830, minHeight: 200, minWidth: 200, height: 250, width: 250,position : 'center',isModal:true, modalOpacity:0.4});
	});
	
	$('#saveSubmit').bind('click',function(){
		var name = $('#inputText').val();
		$.ajax({
			type : "GET",
			url : "ReportServlet",
			data : {
				op: 'save',
				name: name
			}
		});
		$('#inputText').attr('value',"");
		$('#popSave').jqxWindow('closeWindow');
	});
	
	$('#jqxMenu').bind('itemclick',function(event){
		var li = event.args;
		
		if($(li).attr('id') != 'save' && $(li).attr('id') != status)
		{
			if($(li).children().size() == 0)
			{
				if($(li).attr('rep') != null)
				{
					var report = $(li).text();
					$.ajax({
						type : "GET",
						url : "ReportServlet",
						data : {
							op: 'load',
							name: report
						},
						success : function(){
							$('#executeStart').trigger('click');
						}
					});
				}
				else if($(li).attr('cube') != null)
				{
					$.ajax({
						type : "GET",
						url : "StartServlet",
						data : {
							op : "cube",
							value : $(li).attr('cube')
						},
						success : function(){
							window.location = "http://localhost:8080/Workbench.jsp";
						}
					});
				}
			}
		}
		
	});

}

function handleNavigation(event)
{
	$('#navigationStart').jqxNavigationBar('setContentAt',1,doLoading());
	if (event.item == 0) {
		$.ajax({
			type : "GET",
			url : "FactServlet"
		});

		$('#jqxTreeMeasures .jqx-tree-dropdown-root').find('.chkbox.jqx-widget.jqx-checkbox').each(function() {
			var checked;

			$(this).children().children().children().each(function() {
				checked = $(this).attr('class');
			});

			if (checked == "jqx-checkbox-check-checked") {
				var li = $(this).parent();
				var agg = $(li).attr('agg');

				var parsed = agg.split("/");
				var factID = parsed[0];
				var aggVal = parsed[1];

				$.ajax({
					type : "GET",
					url : "MeasuresServlet",
					data : {
						factID : factID,
						aggVal : aggVal
					}
				});
			}
		});

		setTimeout(function() {
			$.ajax({
				type : "GET",
				url : "MeasuresServlet",
				data : {
					factID : "",
					aggVal : ""
				},
				success : initTree
			});

		}, 1500);
	}
}

function initTree() {
	
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/Tree.jsp',
		dataType : 'html',
		success : function(data) {
			$('#navigationStart').jqxNavigationBar('setContentAt',1,$('<div id="jqxTreeStart"></div>').append(data));

			$('#jqxTreeStart').jqxTree({
				height : '320px',
				hasThreeStates : false,
				checkboxes : true,
				width : '298px'
			});

			$('#jqxTreeStart .jqx-tree-dropdown-root').children().each(function() {
				$(this).children().each(function() {
					if ($(this).attr('class') == 'chkbox jqx-widget jqx-checkbox') {
						$(this).remove();
					}
				});
			});

			$('#jqxTreeStart .chkbox.jqx-widget.jqx-checkbox').bind('click',function(event) {
				var checked;
				$(this).children().children().children().each(function() {
					checked = $(this).attr('class');
				});

				var li = $(this).parent();
				var lvl = $(li).attr('lvl');
				var parsed = lvl.split("/");
				var dimID = parsed[0];
				var lvlID = parsed[1];

				if (checked == "") {
					$.ajax({
						type : "GET",
						url : "DimTreeServlet",
						data : {
							op : "rem",
							dimID : dimID,
							lvlID : lvlID
						}
					});
				} 
				else 
				{
					$.ajax({
						type : "GET",
						url : "DimTreeServlet",
						data : {
							op : "add",
							dimID : dimID,
							lvlID : lvlID
						}
					});
				}

			});
		}
	});
}

function handleWindow()
{
	$('#sliceWindow').show();
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/SliceFilter.jsp',
		dataType : 'html',
		success : function(data) {

			$('#sliceWindowContent').empty();

			$('#sliceWindowContent').append(data);

			$('#sliceWindow').jqxWindow({showCollapseButton: true, maxHeight: 450, maxWidth: 830, minHeight: 200, minWidth: 200, height: 450, width: 830,position : 'center'});


			$('#sliceWindowContent').find('input').each(function(){
				$(this).jqxButton({ height: 27, width: 81});

			});

			$('#slice input').bind('click',function(){

				var display = "";
				$(this).parent().parent().parent().find('.settings-label').each(function(){
					display = $(this).text();
				});

				var slice = $(this).attr('slice');
				var toParse = slice.split("/");

				var dimID = toParse[0];
				var levelID = toParse[1];
				var type = toParse[2];

				$.ajax({
					type : "GET",
					url : "DimensionSessionServlet",
					data : {
						dimID : dimID,
						levelID : levelID,
						display: display
					},
					success: function(){
						setTimeout('slicePopup('+type+')',500);
					}
				});

			});

			$('#filter input').bind('click',function(){
				var toParse = $(this).attr('filter');
				var display = "";

				$(this).parent().parent().parent().find('.settings-label').each(function(){
					display = $(this).text();
				});


				var parsed = toParse.split("/");

				var op = parsed[0];
				var factID = parsed[1];

				$.ajax({
					type : "GET",
					url : "FactSessionServlet",
					data : {
						op: op,
						factID: factID,
						display : display
					},
					success: function(){
						filterPopup();
					}
				});
			});

			$("#jqxAddButtonMenu").jqxButton({height: 27, width: 100});
			$("#slice").css('border',"none");
			$("#slice").css('border',"none");
			$("#filter").css('border',"none");
			$("#filter").css('border',"none");
			$('#filter').jqxPanel({height: 270, width:343});
			$('#slice').jqxPanel({height: 270, width:343});
			$("#jqxDockPanelMenu").jqxDockPanel({ width: 810, height: 440 });

			$('#jqxAddButtonMenu').css('width','100px');
			$("#jqxAddButtonMenu").css('height','30px');


		}
	});
}
var min;
var max;
var display;
var source;
function filterPopup(data)
{
	$('#popup').show();
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/NumericFilter.jsp",
		success: function(data){

			$('#popupContent').empty();
			$('#popupContent').append(data);
			$('#popupHeader').empty();
			$('#popupHeader').append($('<span>Measure: '+display+'</span>'));
			$('#popup').jqxWindow({showCollapseButton: true, showCloseButton: false, maxHeight: 450, maxWidth: 830, minHeight: 200, minWidth: 200, height: 350, width: 450,isModal:true, modalOpacity:0.4});


			$('#popup').bind('closed',function(){
				$('#popup').remove();
				$('#jqxWidgetStart').append($('<div id="popup"></div>').append('<div id="popupHeader"></div><div id="popupContent"></div>'));
			});

			$("#jqxButtonNumFilter").jqxButton({ width: '89', height: '25'});
			$("#FilterCancelNumFilter").jqxButton({ width: '89', height: '25'});
			$("#numericInputNumFilter").jqxNumberInput({ width: '150px', height: '25px', decimal: 0.00, min: min, max: max, spinButtons: true, inputMode: 'simple'});
			$("#dropDownFilterList").jqxDropDownList({ source: source, width: 150, height: 25, selectedIndex: 0});
			$("#jqxDockPanelNumFilter").jqxDockPanel({
				width : 300,
				height : 270
			});


			$('#jqxButtonNumFilter').bind('click',function(){
				var value = $('#numericInputNumFilter').jqxNumberInput('val');
				var selectedItem = $('#dropDownFilterList').jqxDropDownList('getSelectedItem');

				$.ajax({
					type : "GET",
					url : "FilterServlet",
					data : {
						op: $(selectedItem.element).text(),
						value : value
					},
					success: function(){
						$('#popup').jqxWindow('closeWindow');
					}
				});
			});

			$("#FilterCancelNumFilter").bind('click',function(){
				$.ajax({
					type : "GET",
					url : "FilterServlet",
					data : {
						op: "",
						value : null
					},
					success: function(){
						$('#popup').jqxWindow('closeWindow');
					}
				});
			});



		}
	});

}

function slicePopup(type)
{	
	if(type == 0)
	{
		popupLOV();
	}
	else if(type == 1)
	{
		popupDate();
	}
	else if(type == 2)
	{
		popupNumeric();
	}
	else
	{
		popupBoolean();
	}
}

function popupLOV()
{
	$('#popup').show();

	
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/StringLOV.jsp",
		success: function(data){

			$('#popupContent').empty();
			$('#popupContent').append(data);
			$('#popupHeader').empty();
			$('#popupHeader').append($('<span>Dimension: '+display+'</span>'));
			$('#popup').jqxWindow({showCollapseButton: true, showCloseButton: false, maxHeight: 600, maxWidth: 830, minHeight: 200, minWidth: 200, height: 460, width: 350,isModal:true, modalOpacity:0.4});
			$('#popup').bind('closed',function(){
				$('#popup').remove();
				$('#jqxWidgetStart').append($('<div id="popup"></div>').append('<div id="popupHeader"></div><div id="popupContent"></div>'));
			});


			$.ajax( {
				type: "GET",
				url: "StringLOVServlet",
				dataType: "html",
				success: function(data) {
					var array = new Array();
					var string = "";
					for(var i in data)
					{
						if(data[i] === ',')
						{
							array.push(string);
							string="";
						}
						else
						{
							string+=data[i];
						}
					}
					startPanels(array);
				},
				error: function()
				{

				}
			} );


			function startPanels(data)
			{
				$("#jqxListString").jqxListBox({ source: data, multiple : true, width: '300', height: '260', incrementalSearch: true, searchMode: "contains"});         
				$("#jqxButtonString").jqxButton({ width: '150', height: '25'});
				$("#jqxDockPanelString").jqxDockPanel({ width: '300', height: '370'});

				var displaySelectedItems = function () {
					var items = $("#jqxListString").jqxListBox('getSelectedItems');
					var selection = "Selected Items: ";
					for (var i = 0; i < items.length; i++) {
						selection += items[i].label + (i < items.length - 1 ? ", " : "");
					}

				}

				$("#jqxButtonString").bind('click',function(){
					var items = $("#jqxListString").jqxListBox('getSelectedItems');

					if(items.length > 0)
					{
						$(items).each(function(){
							$.ajax({
								type : "GET",
								url : "SliceServlet",
								data : {
									value : this.label
								},
								success: function(){
									$('#popup').jqxWindow('closeWindow');
								}
							});
						});	
					}
					else
					{
						$.ajax({
							type : "GET",
							url : "SliceServlet",
							data : {
								value : null
							},
							success: function(){
								$('#popup').jqxWindow('closeWindow');
							}
						});
					}

				});       

			}
		}
	});
}

function popupBoolean()
{
	$('#popup').show();
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/Boolean.jsp",
		success: function(data){

			$('#popupContent').empty();
			$('#popupContent').append(data);
			$('#popupHeader').empty();
			$('#popupHeader').append($('<span>Dimension: '+display+'</span>'));
			$('#popup').jqxWindow({showCollapseButton: true, showCloseButton: false, maxHeight: 450, maxWidth: 830, minHeight: 100, minWidth: 100, height: 150, width: 260,isModal:true, modalOpacity:0.4});

			$('#popup').bind('closed',function(){
				$('#popup').remove();
				$('#jqxWidgetStart').append($('<div id="popup"></div>').append('<div id="popupHeader"></div><div id="popupContent"></div>'));
			});

			$("#boolCancel").jqxButton({height: 25, width: 80});
			$("#boolSubmit").jqxButton({height: 25, width: 80});
			$('#switch').jqxSwitchButton({height: 27, width: 81, checked: true});
			$('#boolDock').jqxDockPanel({height: 120, width: 200});

			$('#boolCancel').bind('click',function(){
				$.ajax({
					type : "GET",
					url : "SliceServlet",
					data : {
						value : null
					},
					success: function(){
						$('#popup').jqxWindow('closeWindow');
					}
				});
			});

			$('#boolSubmit').bind('click',function(){
				$('#popup').jqxWindow('closeWindow');
			});

			$('#switch').bind('checked',function(){
				$.ajax({
					type : "GET",
					url : "BooleanServlet",
					data : {
						value : 1
					}
				});
			});

			$('#switch').bind('unchecked',function(){
				$.ajax({
					type : "GET",
					url : "BooleanServlet",
					data : {
						value : 0
					}
				});
			});

			$.ajax({
				type : "GET",
				url : "BooleanServlet",
				data : {
					value : 1
				}
			});
		}
	});
}

function popupNumeric()
{
	$('#popup').show();
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/Numeric.jsp",
		success: function(data){

			$('#popupContent').empty();
			$('#popupContent').append(data);
			$('#popupHeader').empty();
			$('#popupHeader').append($('<span>Dimension: '+display+'</span>'));
			$('#popup').jqxWindow({showCollapseButton: true, showCloseButton: false, maxHeight: 450, maxWidth: 830, minHeight: 200, minWidth: 200, height: 500, width: 450,isModal:true, modalOpacity:0.4});
			$('#popup').bind('closed',function(){
				$('#popup').remove();
				$('#jqxWidgetStart').append($('<div id="popup"></div>').append('<div id="popupHeader"></div><div id="popupContent"></div>'));
			});

			var data = [];
			$('#jqxListNumeric').jqxListBox({
				source : data,
				multiple : false,
				width: '148px',
				height: '300px',
				incrementalSearch : true,
				searchMode : "contains"
			});

			$("#jqxButtonNumeric").jqxButton({ width: '89', height: '25'});
			$("#numericInputNumeric").jqxNumberInput({ width: '150px', height: '25px', decimal: 0.00, min: min, max: max, spinButtons: true, inputMode: 'simple'});
			$("#jqxAddButtonNumeric").jqxButton({ width: '89', height: '25'});
			$("#jqxDockPanelNumeric").jqxDockPanel({
				width : 600,
				height : 490
			});


			var contextMenu = $("#popupNumeric").jqxMenu({ width: '120px', height: '30px', autoOpenPopup: false, mode: 'popup'});

			$("#jqxListNumeric").bind('mousedown', function (event) {
				var rightClick = isRightClick(event);
				if (rightClick) {
					var scrollTop = $(window).scrollTop();
					var scrollLeft = $(window).scrollLeft();
					contextMenu.jqxMenu('open', parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
					return false;
				}
			});

			$('#jqxListNumeric').bind('contextmenu', function (e) {
				return false;
			});
			function isRightClick(event) {
				var rightclick;
				if (!event) var event = window.event;
				if (event.which) rightclick = (event.which == 3);
				else if (event.button) rightclick = (event.button == 2);
				return rightclick;
			}

			$('#removeNumeric').bind('click',function(){
				var item = $("#jqxListNumeric").jqxListBox('getSelectedItem');

				var label = item.label;
				var index = 0;
				var totalItems = $("#jqxListNumeric").jqxListBox('getItems');

				$(totalItems).each(function(){
					if(this.label != label)
					{
						index++;	
					}
					else
					{
						$("#jqxListNumeric").jqxListBox('removeAt',index);
					}
				});
			});

			$('#jqxAddButtonNumeric').bind('click',function(){
				var value = $('#numericInputNumeric').jqxNumberInput('val');


				if(value <= max && value >= min)
				{
					var items = $("#jqxListNumeric").jqxListBox('getItems');
					var contains = false;

					$(items).each(function(){
						if(this.value == value)
						{
							contains = true;
						}
					});

					if(!contains)
					{
						$('#jqxListNumeric').jqxListBox('addItem',''+value);
					}				
				}
			});

			$('#jqxButtonNumeric').bind('click',function(){

				var items = $("#jqxListNumeric").jqxListBox('getItems');

				if(items.length > 0)
				{
					$(items).each(function(){

						$.ajax({
							type : "GET",
							url : "SliceServlet",
							data : {
								value : this.label
							},
							success: function(){
								$('#popup').jqxWindow('closeWindow');
							}
						});
					});
				}
				else
				{
					$.ajax({
						type : "GET",
						url : "SliceServlet",
						data : {
							value : null
						},
						success: function(){
							$('#popup').jqxWindow('closeWindow');
						}
					});
				}
			});
		}
	});
}


function popupDate()
{
	$('#popup').show();
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/Date.jsp",
		success: function(data){

			$('#popupContent').empty();
			$('#popupContent').append(data);
			$('#popupHeader').empty();
			$('#popupHeader').append($('<span>Dimension: '+display+'</span>'));
			$('#popup').jqxWindow({showCollapseButton: true, showCloseButton: false, maxHeight: 450, maxWidth: 830, minHeight: 200, minWidth: 200, height: 350, width: 380,isModal:true, modalOpacity:0.4});
			$('#popup').bind('closed',function(){
				$('#popup').remove();
				$('#jqxWidgetStart').append($('<div id="popup"></div>').append('<div id="popupHeader"></div><div id="popupContent"></div>'));
			});

			$("#jqxCalendarDate").jqxDateTimeInput({ width: '250px', height: '25px', formatString: "yyyy/MM/dd"});
			$("#jqxButtonDate").jqxButton({ width: '100', height: '25'});
			$("#jqxCancelButtonDate").jqxButton({ width: '100', height: '25'});
			$("#jqxDockDate").jqxDockPanel({ width: '300', height: '250'});

			$('#jqxButtonDate').bind('click',function(){
				var date = $('#inputElement').val();
				var parsed = date.split("/");
				var dateParsed = "";

				$(parsed).each(function(){
					dateParsed += this;
					dateParsed += "-";
				});

				dateParsed = dateParsed.substring(0,dateParsed.length-1);
				dateParsed += ' 00:00:00';
				$.ajax({
					type : "GET",
					url : "SliceServlet",
					data : {
						value : dateParsed
					},
					success: function(){
						$('#popup').jqxWindow('closeWindow');
					}
				});

			});

			$('#jqxCancelButtonDate').bind('click',function(){
				$.ajax({
					type : "GET",
					url : "SliceServlet",
					data : {
						value : null
					},
					success: function(){
						$('#popup').jqxWindow('closeWindow');
					}
				});
			});
		}
	});
}


function buildTable()
{
	$('#jqxTabs').css('visibility','visible');
	$.ajax({
		type: 'GET',
		url: "QueryServlet",
		dataType: 'xml',
		success: 
			function(data)
			{
			handleTableData(data);
			},
			error:
				function(jqXHR, textStatus, errorThrown)
				{
				alert('Error: ' + textStatus + errorThrown);
				}
	});
}

var headers;
var tableData;
var currentHeader ;
var jqxWidget ;
var currentCol;
var grid ;
var grid2;



function handleTableData(data)
{

	$('#jqxTabs').jqxTabs('setContentAt', 0, $('<div>').append($('<div id="gridPaged">')));
	$('#jqxTabs').jqxTabs('setContentAt', 1, $('<div>').append($('<div id="gridGrouped">')));
	
	//headers
	grid = $("#gridPaged");
	grid2 = $('#gridGrouped');

	// prepare the data
	currentHeader = 0;
	headers = [];
	tableData = [];
	jqxWidget = $('#jqxWidgetStart');
	var cols = [];
	var header = $(data).children().children("headers");
	var rowset = $(data).children().children("row-set");
	header.children("head").each(
			function()
			{
				buildHeaders($(this));
			}
	);

	//pass $(this) inside iteration of headers content
	function buildHeaders(element)
	{
		headers[headers.length] = element;
		cols[cols.length] = 
		{
				text : element.text(),
				dataField : element.text(),
				width: 150,
				cellsformat : cellFormat(element.attr('type'))
		};
	}

	function buildContent(element,row)
	{
		var theValue = element.text();

		if(headers[currentHeader].attr('type') == 'date')
		{
			var splitted = theValue.split(" ");
			if(splitted.length > 1)
				theValue = splitted[0]+'T'+splitted[1];
		}
		row[headers[currentHeader++].text()] = element.text();
	}


	function cellFormat(type)
	{
		if(type == 'date')
			return 'D';
		if(type == 'number')
			return 'f4';
		return null;
	}

	var pixels = headers.length * 150;
	
	var row ;
	rowset.children("row").each(
			function()
			{
				var node = $(this);
				row = {};
				node.children("value").each(
						function()
						{
							var innerNode = $(this);
							buildContent(innerNode,row);
						}
				);

				currentHeader = 0;
				tableData[tableData.length] = row;

			}
	);

	var source =
	{
			localdata: tableData,
			datatype: "array",
			sortcolumn: headers[0].text(),
			sortdirection: 'asc'
	};

	var dataAdapter = new $.jqx.dataAdapter(source);

	grid.jqxGrid(
			{
				height: 340,
				width: 690,
				source: dataAdapter,
				sortable: true,
				altrows: true,
				columnsresize: true,
				columns: cols
			});
	grid2.jqxGrid(
			{
				height: 340,
				width: 690,
				source: dataAdapter,
				sortable: true,
				altrows: true,
				columnsresize: true,
				columns: cols,
				groupable: true
			}
	);

	doDrill(grid);

	grid.bind('contextmenu', function (e) {
		return false;
	});
	grid2.bind('contextmenu', function (e) {
		return false;
	});


	


}

function eraseMenus()
{
	if(currentCol != undefined)
	{
		var i;
		for( i = 0; i< currentCol; i++)
		{
			$('#jqxMenu'+i).remove();
		}
	}
}

function isRightClick(event) {
	var rightclick;
	if (!event) var event = window.event;
	if (event.which) rightclick = (event.which == 3);
	else if (event.button) rightclick = (event.button == 2);
	return rightclick;
}

function doDrill(grid)
{
	eraseMenus();
	currentCol=0;
	grid.find('.jqx-grid-column-header').each(
			function()
			{
				var node = $(this);
				var header = headers[currentCol];
				var split = header.attr('ID').split("/");

				if(header.attr('ID').charAt(0) == 'L'){
					var currentID = "jqxMenu"+currentCol++;
					var menuDIV = $('<div></div>').attr("id",currentID);
					$.ajax({
						url: 'DrillServlet',
						data: {
							dimID: split[1],
							levelID: split[0]
						},
						success: function(data){
							var size = 0;
							var theData = $(data);
							theData.children().each(function(){
								if(0 == $(this).children().children().size())
									$(this).remove();
								else
									size++;
							});
							if(size != 0)
							{
								menuDIV.html(theData);
								jqxWidget.append(menuDIV);
								menuDIV = menuDIV.jqxMenu({ width: '120px', height: '56px', autoOpenPopup: false, mode: 'popup' });

								node.bind('mousedown', function (event) {
									var rightClick = isRightClick(event);
									if (rightClick) {
										var scrollTop = $(window).scrollTop();
										var scrollLeft = $(window).scrollLeft();
										menuDIV.jqxMenu('open', parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
										return false;
									}
								});
								node.attr('title', 'right-click for drill');
								
								menuDIV.bind('itemclick',function(event){
									var li = event.args;
									var upper = $(li).attr('upper');
									var lower = $(li).attr('lower');
									if(upper != null)
									{
										$.ajax({
											type : "GET",
											url : "DrillUpServlet",
											data: {
												originID: split[1],
												originLvl: split[0],
												levelID: upper
											},
											success : drill
										});
									}
									else if(lower != null)
									{
										$.ajax({
											type : "GET",
											url : "DrillDownServlet",
											data: {
												dimID: split[1],
												levelID: lower
											},
											success : drill
										});
									}
									
								});
							}
						}
					});
				}

			}
	);
}

function drill()
{
	$('#jqxTabs').jqxTabs('setContentAt', 0, doLoading());
	$('#jqxTabs').jqxTabs('setContentAt', 1, doLoading());
	
	buildTable();
}

function doLoading()
{
	return $('<img/>').attr('src','287.gif').attr('style', 'display: block; margin-left: auto;   margin-right: auto');
}

function buildDrill(node)
{	
	var currentID = "jqxMenu"+currentCol++;
	var menuDIV = $('<div></div>').attr("id",currentID);
	$.ajax({
		url: 'ajax.html',
		success: 
			function(data)
			{
			menuDIV.html(data);
			jqxWidget.append(menuDIV);
			menuDIV = menuDIV.jqxMenu({ width: '120px', height: '56px', autoOpenPopup: false, mode: 'popup', theme: getTheme() });

			node.bind('mousedown', function (event) {
				var rightClick = isRightClick(event);
				if (rightClick) {
					var scrollTop = $(window).scrollTop();
					var scrollLeft = $(window).scrollLeft();
					menuDIV.jqxMenu('open', parseInt(event.clientX) + 5 + scrollLeft, parseInt(event.clientY) + 5 + scrollTop);
					return false;
				}
			});
			node.attr('title', 'right-click to drill');

			}
	});
}

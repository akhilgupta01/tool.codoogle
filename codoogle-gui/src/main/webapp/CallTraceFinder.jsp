<%@page import="gupta.akhil.tools.common.config.SystemConfig"%>
	<div align="center" height="25%" >
	<form id="searchCallTraceForm">
		<table border="0" width="100%">
			<tr>
				<td>
					<table border="0">
						<tr>
							<td></td>
							<td width="1%"><img src="./images/company_logo.png" height="65px"></td>
							<td>
								<table border="0" style="background-color:#FFC421">
									<tr >
										<td></td>
										<td colspan="2" align="left">
											<select id="revision2">
												<% for(String revision: SystemConfig.getCallTraceIndexGroupNames()){%>
													<option value="<%=revision%>"><%=revision%></option> 
												<%}%>
											</select>
											<input type="radio" name="callTraceSearchOption" value="byContent" checked="true">Call Graph Containing</input>
											<input type="radio" name="callTraceSearchOption" value="byServer">By Server</input>
										</td>
										<td></td>
									</tr>
									<tr align="center">
										<td></td>
										<td colspan="2"><input type="text" id="callTraceSearchBox" size="120"/></td>
										<td><input type="button" value="Go.." onClick="findCallTraces();"/></td>
									</tr>
									<tr align="center">
										<td></td>
										<td colspan="2" align="left"><span id="messageBox2">Searches for call graphs containing a given text (e.g. getCustomer will search all calls invoking method "getCustomer")</span></td>
										<td></td>
									</tr>
								</table>
							</td>
							<td valign="top">
								<table border="0" width="100%" height="100%">
									<tr><td width="50%">&nbsp;</td> <td><img src="./images/company_logo.png" height="55px"></td></tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			</tr>
		</table>
	</form>
</div>
<hr>
<table id="callTraceResults" class="display" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th width="5%"></th>
			<th width="10%">Server Cluster</th>
			<th width="10%">Call Type</th>
			<th width="55%">Entry Method</th>
			<th width="5%">Total Snapshots</th>
			<th width="15%">Last Observation Time</th>
		</tr>
	</thead>
</table>

<script type="text/JavaScript">
var callTraceResults;

$(document).ready(function() {
	callTraceResults = $('#callTraceResults').DataTable( {
		"columns": [
			{
				"className":      'details-control',
				"orderable":      false,
				"data":           null,
				"defaultContent": ''
			},
			{ "data": "serverCluster" },
			{ "data": "callType" },
			{ "data": "entryPoint" },
			{ "data": "snapshotCount" },
			{ "data": "lastObservationTime" }
		],
		"order": [[1, 'asc']]
	} );
	$('#busyImg').hide();
	
	$('#revision2').val(getCookie("cRevision"));

	 // Add event listener for opening and closing details
	$('#callTraceResults tbody').on('click', 'td.details-control', function () {
		var tr = $(this).closest('tr');
		var row = callTraceResults.row( tr );
		var revision=$('#revision2').val();
 
		if ( row.child.isShown() ) {
			// This row is already open - close it
			row.child.hide();
			tr.removeClass('shown');
		}
		else {
			// Open this row
			$.get('./resources/calltrace/' + revision + '/' + row.node().id)
				.done(function(jsonData){
					row.child(getCallTrace(jsonData)).show();
					tr.addClass('shown');
					$("#" + "tree_" + jsonData.id).dynatree();
				});
		}
	} );
});
 
	$('input[name=callTraceSearchOption]').click(function(){
		if($(this).val() == "byContent"){
			$('#messageBox2').text("Searches for call graphs containing a given text (e.g. getCustomer will search all calls invoking method \"getCustomer\")");
		}else{
			$('#messageBox2').text("Searches for call graphs invoked on a given server cluster (e.g. Customer* will search all calls invoked on servers starting with name Customer");
		}
	});

function findCallTraces(){
	var searchTerm=$('#callTraceSearchBox').val();
	var revision=$('#revision2').val();
	setCookie("cRevision", revision);
	if(searchTerm==""){
		alert('No search term entered. Please enter something to search!');
		return; 
	}
	var callTraceSearchOption=$('input:radio[name=callTraceSearchOption]:checked').val();
	if(callTraceSearchOption == "byContent"){
		searchByContent(revision, searchTerm);
	}else{
		searchByServer(revision, searchTerm);
	}
}

function searchByContent(revision, searchTerm){
	$.get('./resources/calltrace/byContent/' + revision + '/' + searchTerm)
		.done(function(jsonData){displayResults(jsonData)});
		showBusy();	
}

function searchByServer(revision, searchTerm){
	$.get('./resources/calltrace/byServer/' + revision + '/' + searchTerm)
		.done(function(jsonData){displayResults(jsonData)});
		showBusy();		
}

function displayResults(jsonData){
	hideBusy();
	callTraceResults.rows().remove();
	for(var i=0;i<jsonData.length;i++){
		var resultRow = jsonData[i];
		row = callTraceResults.row.add(resultRow);
		row.node().id=resultRow.id;
		row.draw();
	}
}

function showBusy(){
	$('#busyImg').show();
}
function hideBusy(){
	$('#busyImg').hide();
}

/* Formatting function for row details - modify as you need */
function getCallTrace (d) {
	return '<pre><div id="tree_' + d.id +'">' + d.callGraph + '</div></pre>';
}
</script>

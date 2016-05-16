<%@page import="gupta.akhil.tools.common.config.SystemConfig"%>
<div align="center" height="25%" width="100%">
	<form id="searchForm">
		<table border="0" width="100%">
			<tr>
				<td>
					<table border="0">
						<tr>
							<td></td>
							<td width="1%"><img src="./images/company_logo.png" height="65px"></td>
							<td>
								<div id="searchBoxContainer" style="background-color:#FFC421">
									<div width="60%">Now use combination of fields to search an artifact. e.g. fileName:*.java && moduleName:srv.* && !contents:GLogger</div>
									<div width="60%">
											<select id="revision">
												<% for(String revision: SystemConfig.getCodeIndexGroupNames()){%>
													<option value="<%=revision%>"><%=revision%></option> 
												<%}%>
											</select>
											<input type="text" id="searchBox"/>
											<input type="button" value="Go.." onClick="findArtifacts();"/>
									</div>		
									<div width="60%">
										<span id="messageBox">&nbsp;</span>
									</div>	
								</div>
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
<div height="70%">
	<table id="searchResults_table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th width="10%"></th>
				<th  width="35%" valign="top" align="center">Artifact Name</th>
				<th  width="35%" valign="top" align="center">Artifact Path</th>
				<th  width="30%" valign="top" align="center">Module Name</th>
			</tr>
		</thead>
	</table>
</div>

	<script type="text/JavaScript">
		var resultsTable;
		$.fn.dataTable.TableTools.defaults.aButtons=["pdf","copy","csv","xls"];
		$(document).ready(function() {
			resultsTable = $('#searchResults_table').DataTable( {
				"columns": [
					{
						"className":      'details-control',
						"orderable":      false,
						"data":           null,
						"defaultContent": ''
					},
					{ "data": "fileName" },
					{ "data": "filePath" },
					{ "data": "moduleName" }
				],
				"order": [[1, 'asc']],
				"dom": 'T<"clear">lfrtip',
				"tableTools": {
					"sSwfPath": "//cdn.datatables.net/tabletools/2.2.4/swf/copy_csv_xls_pdf.swf"
				}
			} );
			$('#busyImg').hide();
			
			
			// Add event listener for opening and closing details
			$('#searchResults_table tbody').on('click', 'td.details-control', function () {
				var tr = $(this).closest('tr');
				var row = resultsTable.row( tr );
				var revision=$('#revision').val();
				window.open("./CodeViewer.jsp?revision=" + revision + "&id=" + row.node().id, row.node().id);
	 
				/*if ( row.child.isShown() ) {
					// This row is already open - close it
					row.child.hide();
					tr.removeClass('shown');
				}
				else {
					// Open this row
					$.get('./resources/searcher/artifact/' + row.node().id)
						.done(function(jsonData){
							row.child(getFileContents(jsonData)).show();
							tr.addClass('shown');
							$('#codeWidget').text(jsonData.contents);
							if(jsonData.contents.length < 32750){
								prettyPrint();
							}
						});
				}*/
			} );
		});
		
		$('#revision').val(getCookie("cRevision"));
		
		$(document).keypress(function(e) {
			  if(e.which == 13) {
				  findArtifacts();
			  }
		});
		
		function findArtifacts(){
			var searchTerm=$('#searchBox').val();
			var revision=$('#revision').val();
			setCookie("cRevision", revision);
			if(searchTerm==""){
				alert('No search term entered. Please enter something to search!');
				return; 
			}
			searchByQuery(revision, searchTerm);
		}
		
		function searchByQuery(revision, searchTerm){
			$.get('./resources/searcher/artifact/byQuery/' + revision + '/' + searchTerm)
				.done(function(jsonData){displayResults(jsonData)})
				.always(function(){hideBusy();});
				showBusy();
		}

		function displayResults(jsonData){
			resultsTable.clear();
			for(var i=0;i<jsonData.length;i++){
				var resultRow = jsonData[i];
				row = resultsTable.row.add(resultRow);
				row.node().id=resultRow.id;
				//row.draw();
			}
			resultsTable.draw();
		}
		
		function showBusy(){
			$('#busyImg').show();
		}
		function hideBusy(){
			$('#busyImg').hide();
		}
		

		
		function refresh(){
			alert('This will download all the artifacts from nexus and rebuild the index. Please wait for 15 minutes.');
			$.get('./resources/searcher/class/rebuildIndex');
		}
		
		function reFormat(word){
			return word.replace(/\//g,'~');
		}
		
		/* Formatting function for row details - modify as you need */
		function getFileContents (d) {
			return '<div width="80%" height="600px"><pre id="codeWidget" class="prettyprint linenums"></div>';
		}
	</script>

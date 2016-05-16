<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta http-equiv="content-type" content="text/html" />
	<meta charset="ISO-8859-1"/>
	<title>Code Analysis Tool</title>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="./js/datatable/1104/media/css/jquery.dataTables.css"> 
	<link rel="stylesheet" href="./js/prettify/google-code-prettify/prettify.css">
	<link rel="stylesheet" href="./js/dynatree/src/skin/ui.dynatree.css">
	<style type="text/css" class="init">
		td.details-control {
			background: url('images/plus.png') no-repeat center center;
			cursor: pointer;
		}
		tr.shown td.details-control {
			background: url('images/minus.png') no-repeat center center;
		}
		.ui-tabs {
			font-size: 12px;
			font-weight: bold;
		}
		.ui-tabs-panel {
			font-size: 12px;
			font-weight: normal;
		}
		.ui-tabs-anchor {
			font-size: 12px;
			font-weight: bold;
		}
		.ui-widget-header{
			background-color:#FFC421;
			background-image:none;
			font-weight: bold;
		}
		

		.container{
			background-color:#E8EFEF;
			min-width: 300px;
			min-height:150px;
			border: 1px solid black;
		}
		.container-title{
			background-color:#A7C4C5;
			text-align:center;
			color:black;
			font-weight: bold;
			padding: 2px 0px;
			border-bottom-width: 1px;
			border-bottom-style: solid;
			border-bottom-color: black;
		}

		.application{
			min-width: 50px;
			min-height: 50px;
			border: 1px solid black;
			margin: 10px 10px 10px 10px;
			font-weight:bold;
			display: block;
			align:center;
			background-color:#F2E5E6;
		}
		.app-title{
			background-color:#FFC082;
			text-align:center;
			color:black;
			padding: 2px 5px;
			border-bottom-width: 1px;
			border-bottom-style: solid;
			border-bottom-color: black;
			
		}
		
		.module{
			background-color:#FFF3DB;
			text-align:center;
			color:cyan;
			margin: 2px 2px;
			
			display: block;
			border: 1px solid black;
			align:center;
		}
		.module-title{
			background-color:#FDDC94;
			text-align:center;
			color:black;
			padding: 2px 5px;
			border-bottom-width: 1px;
			border-bottom-style: solid;
			border-bottom-color: black;
			
		}
		
		.ejb{
			background-color:#C2CE89;
			text-align:center;
			color:#88983F;
			font-weight:normal;
			
			display: block;
			border: 1px solid black;
			margin: 1px 1px;
		}
			
	</style>
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
	<script type="text/javascript" language="javascript" src="./js/datatable/1104/media/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" language="javascript" src="./js/prettify/google-code-prettify/prettify.js"></script>
	<script src="./js/dynatree/src/jquery.dynatree.js" type="text/javascript"></script>
	<script>
		function drawDeployment(){
			$('#Rmd').empty();
			$('#Cbe').empty();
			$('#Tsd').empty();
			$('#Bat').empty();
			moduleName = $('#searchText').val();
			$.get('http://codoogle:9200/deployment/service/_search?q=*' + moduleName + '*&size=20000&pretty')
				.done(function(jsonData){displayResults(jsonData)});
		}
		
		function displayResults(jsonData){
			hits = jsonData.hits.hits;
			for(var i=0;i<hits.length;i++){
				applicationName = hits[i]._source.applicationName;
				moduleName = hits[i]._source.moduleName;
				serverName = hits[i]._source.serverName;
				ejbName = hits[i]._source.ejbName;
				if(ejbName == null){
					ejbName = hits[i]._source.contextRoot.substring(1);
				}
				
				checkAndAddServer(serverName);
				checkAndAddApplication(serverName, applicationName);
				checkAndAddModule(serverName, applicationName, moduleName);
				checkAndAddEJB(serverName, applicationName, moduleName, ejbName);
			}
		}
		
		function toId(word){
			return word.replace(/\./g,'_');
		}
		
		function checkAndAddServer(serverName){
			if($('#' + toId(serverName)).length == 0){
				if(serverName.indexOf("Rmd") > -1){
					$('#Rmd').append("<td><div id='" + toId(serverName) + "' class='container'><div class='container-title'>" + serverName + "</div></div></td>");
				}else if(serverName.indexOf("Cbe") > -1){
					$('#Cbe').append("<td><div id='" + toId(serverName) + "' class='container'><div class='container-title'>" + serverName + "</div></div></td>");
				}else if(serverName.indexOf("Tsd") > -1){
					$('#Tsd').append("<td><div id='" + toId(serverName) + "' class='container'><div class='container-title'>" + serverName + "</div></div></td>");
				}else if(serverName.indexOf("Bat") > -1){
					$('#Bat').append("<td><div id='" + toId(serverName) + "' class='container'><div class='container-title'>" + serverName + "</div></div></td>");
				}
			}
		}

		function checkAndAddApplication(serverName, applicationName){
			serverId = toId(serverName);
			appId = toId(serverName) + '_' + toId(applicationName);
			if($('#' + appId).length == 0){
				$('#' + serverId).append("<div id='" + appId + "' class='application' align='center'><div class='app-title'>" + applicationName + "</div></div>");
			}
		}
		
		function checkAndAddModule(serverName, applicationName, moduleName){
			serverId = toId(serverName);
			appId = serverId + '_' + toId(applicationName);
			moduleId = appId + '_' + toId(moduleName);
			
			if($('#' + moduleId).length == 0){
				$('#' + appId).append("<div id='" + moduleId + "' class='module' align='center'><div class='module-title'>" + moduleName + "</div></div>");
			}
		}
		
		function checkAndAddEJB(serverName, applicationName, moduleName, ejbName){
			serverId = toId(serverName);
			appId = serverId + '_' + toId(applicationName);
			moduleId = appId + '_' + toId(moduleName);
			ejbId = moduleId + '_' + toId(ejbName);
			
			if($('#' + ejbId).length == 0){
				$('#' + moduleId).append("<div id='" + ejbId + "' class='ejb' align='center'>" + ejbName + "</div>");
			}
		}

	</script>
</head>
<body>
	<div width="100%">
		<table width="100%">
			<tr>
				<td width="80%" align="center"><input type="text" id="searchText" maxlength="100" size="120"/></td>
				<td width="20%" align="center"><input type="button" id="searchButton" onclick="drawDeployment()"/></td>
			</tr>
		</table>
	</div>
	
	<div width="100%" id="displayArea">
		<table><tr  bgcolor="#EBF1DE"><td valign="middle" align="center">RMD Domain</td><td id="Rmd" valign="top"></td></tr></table>
		<table><tr  bgcolor="#FDE9D9"><td valign="middle" align="center">CBE Domain</td><td id="Cbe" valign="top"></td></tr></table>
		<table><tr  bgcolor="#DDD9C4"><td valign="middle" align="center">TSD Domain</td><td id="Tsd" valign="top"></td></tr></table>
		<table><tr  bgcolor="#DAEEF3"><td valign="middle" align="center">BAT Domain</td><td id="Bat" valign="top"></td></tr></table>
	</div>
</body>
</html>
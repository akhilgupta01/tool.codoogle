<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta http-equiv="content-type" content="text/html" />
	<meta charset="ISO-8859-1"/>
	<title>Code Search & Analysis Tool</title>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="./js/datatable/1104/media/css/jquery.dataTables.css"> 
	<link rel="stylesheet" href="./js/prettify/google-code-prettify/prettify.css">
	<link rel="stylesheet" href="./js/dynatree/src/skin/ui.dynatree.css">
	<link rel="stylesheet" href="./js/tabletools/dataTables.tableTools.css">
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
		.ui-autocomplete.ui-widget {
			font-family: sans-serif;
			font-size: 12px;
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
		
		#searchBoxContainer{
			border-radius: 25px;
			text-align: center;
			padding: 10px; 
		}
		
		#searchBox{
			width:70%;
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
	<script type="text/javascript" language="javascript" src="./js/tabletools/dataTables.tableTools.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1','packages':['corechart']}]}"></script>
	<script>
		$(function() {
			$( "#tabs" ).tabs({
				beforeLoad: function( event, ui ) {
					ui.jqXHR.error(function() {
						ui.panel.html("Couldn't load this tab. We'll try to fix this as soon as possible. " + "If this wouldn't be a demo." );
					});
				}
			});
		});
		
		function setCookie(c_name,value,exdays){
			var exdate=new Date();
			exdate.setDate(exdate.getDate() + exdays);
			var c_value=escape(value) + ((exdays==null) ? "" : ("; expires="+exdate.toUTCString()));
			document.cookie=c_name + "=" + c_value;
	    }

	    function getCookie(c_name){
			var i,x,y,ARRcookies=document.cookie.split(";");
			for (i=0;i<ARRcookies.length;i++){
				x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
				y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
				x=x.replace(/^\s+|\s+$/g,"");
				if (x==c_name){
					return unescape(y);
				}
	     	}
	    }
	    
	    function callMe(){
            window.open("http://codoogle:5601/","_kibana");
		}

	</script>
</head>
<body>
	<div width="100%">
		<table width="100%"><tr>
				<td width="30%" align="left"><img id="busyImg" src="./images/loadingGif.gif" height="15" width="100"/></td>
				<td width="40%" align="center"><span style="color:#fA9422;font-size:25px"><b>Code Search & Analysis Tool</b></span></td>
				<td width="30%" align="right"><a href="mailTo:akhilgupta01@gmail.com">Suggestions?</a></td>
			</tr>
		</table>
	</div>
	<div id="tabs" height="100%">
		<ul align="right">
			<li><a href="./CodeSearch.jsp">Code Search</a></li>
			<li><a href="./CallTraceFinder.jsp">Call Trace Search</a></li>
		</ul>
	</div>
</body>
</html>
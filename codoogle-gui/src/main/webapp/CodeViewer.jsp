<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<style type="text/css" title="currentStyle">
			@import "./js/datatable/css/demo_page.css"; 
			@import "./js/datatable/css/demo_table.css";
			@import "./js/prettify/google-code-prettify/prettify.css";
		</style>
		<script type="text/javascript" language="javascript" src="./js/prettify/google-code-prettify/prettify.js"></script>
		<script type="text/javascript" language="javascript" src="./js/datatable/js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="./js/datatable/js/jquery.dataTables.js"></script>
		
	</head>	
	<body>
		<pre id="codeWidget" class="prettyprint linenums">
	</body>
	<script type="text/javascript">
		$.get('./resources/searcher/artifact/<%=request.getParameter("revision")%>/<%=request.getParameter("id")%>').done(function(jsonData){
			if(jsonData != null){
				window.document.title = jsonData.fileName;
				$('#codeWidget').text(jsonData.contents);
				if(jsonData.contents.length < 32750){
					prettyPrint();
				}
			}
		});
		
	</script>
</html>
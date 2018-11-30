<@compress single_line=true>
<#escape x as x?html>
<#include "../../common/macro.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <@meta/>
	<link href="/dist/static/css/core.css?{{webpack.hash}}" rel="stylesheet" type="text/css"/>
	<link href="/dist/static/css/project-index.css?{{webpack.hash}}" rel="stylesheet" type="text/css"/>
	<@init/>
</head>
<body>
	
	<div class="app-container"></div>
	
	<script type="text/javascript" src="/dist/static/js/vendor.js?{{webpack.hash}}"></script>
  	<script type="text/javascript" src="/dist/static/js/core.js?{{webpack.hash}}"></script>
  	<script type="text/javascript" src="/dist/static/js/project-index.js?{{webpack.hash}}"></script>
</body>
</html>
</#escape>
</@compress>

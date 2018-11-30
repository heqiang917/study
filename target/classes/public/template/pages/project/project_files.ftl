<@compress single_line=true>
<#escape x as x?html>
<#include "../../common/macro.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <@meta/>
	<link href="/dist/static/css/core.css?f91bca692ab4ec5f2e73" rel="stylesheet" type="text/css"/>
	<link href="/dist/static/css/project-files.css?f91bca692ab4ec5f2e73" rel="stylesheet" type="text/css"/>
	<@init/>
</head>
<body>
	
	<div class="app-container"></div>
	
	<script type="text/javascript" src="/dist/static/js/vendor.js?f91bca692ab4ec5f2e73"></script>
  	<script type="text/javascript" src="/dist/static/js/core.js?f91bca692ab4ec5f2e73"></script>
  	<script type="text/javascript" src="/dist/static/js/project-files.js?f91bca692ab4ec5f2e73"></script>
</body>
</html>
</#escape>
</@compress>

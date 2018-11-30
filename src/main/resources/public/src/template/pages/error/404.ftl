<@compress single_line=true>
<#escape x as x?html>
<#include "../../common/macro.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
  <@meta/>
  <link href="/dist/static/css/core.css?{{webpack.hash}}" rel="stylesheet" type="text/css"/>
  <link href="/dist/static/css/index.css?{{webpack.hash}}" rel="stylesheet" type="text/css"/>
  <style>
    .m-center{
      width:500px;
      position:absolute;
      left:50%;
      top:50%;
      margin:-150px 0 0 -250px;
    }
    .m-error-404 {
      width: 150px;
      height:150px;
      float:left;
      margin: 0px auto;
      background-image: url(/images/404.png);
      background-repeat: no-repeat;
      background-position: center;
      background-size: 150px;
    }
    .backbtn{
      width:130px;
      height:30px;
      color: #fff;
      font-size: 16px;
      border-radius: 3px;
      background-color: #45b0f5;
      padding: 6px 37px 5px;
    }
    .m-error-text{
      padding-top: 50px;
      width: 261px;
      height: 150px;
      float: left;
      margin-left: 60px;
    }
    .u-top{
      color:#2B7ACE;
      font-size:26px;
      line-height:35px;
      margin-bottom:5px;
    }
    .u-middle{
      font-size:14px;
      line-height:19px;
      margin-bottom:40px;
    }
  </style>
</head>
<body class="g-doc g-doc-bg">
  <div class="g-hd">
    <@header />
  </div>
  <div class="g-bd f-pr">
    <div class="m-center">
       <div class="m-error m-error-404">
       </div>
       <div class="m-error-text">
          <p class = "u-top">矮油~页面漂走了...</p>
          <p class = "u-middle">休息一下，继续前进吧！</p>
          <a class = "backbtn" href="/">回到首页</a>
       </div>
    </div>
     
  </div>
	<div class="g-ft">
		<@footer />
	</div>
  <#noescape>
    <script>
     
    </script>
  </#noescape>
</body>
</html>
</#escape>
</@compress>

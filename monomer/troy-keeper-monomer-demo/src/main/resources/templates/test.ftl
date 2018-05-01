<#assign base=request.contextPath />
<!DOCTYPE>
<html>
<head>
    <link rel="stylesheet" href="${base}/css/wro.css"/>
    <script src="${base}/js/test.js" type="text/javascript"></script>
    <title>
        freemarker
    </title>
</head>
<body>
<h1>Hello ${name} from
    resource freemark!</h1>
<br />
<h1>测试样式和JS路径</h1>
<div class="container">
    <form role="form">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username"/>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password"/>
        </div>
        <button type="button" class="btn btn-primary" onclick="test();">点我测试JS</button>
    </form>
</div>
</body>
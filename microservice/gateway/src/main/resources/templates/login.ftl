<html>
<head>
<link rel="stylesheet" href="content/css/wro.css"/>
</head>
<body>
<#if RequestParameters['error']??>
	<div class="alert alert-danger">
		There was a problem logging in. Please try again.
	</div>
</#if>
	<div class="container">
		<form role="form" action="uaa/login" method="post">
		  <div class="form-group">
		    <label for="username">Username:</label>
		    <input type="text" class="form-control" id="username" name="username"/>
		  </div>
		  <div class="form-group">
		    <label for="password">Password:</label>
		    <input type="password" class="form-control" id="password" name="password"/>
		  </div>

		  <button type="submit" class="btn btn-primary">Submit</button>
		</form>
	</div>
	<script src="content/js/wro.js" type="text/javascript"></script>
</body>
</html>
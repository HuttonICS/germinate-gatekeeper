<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
  ~ Copyright 2017 Information and Computational Sciences,
  ~ The James Hutton Institute.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~  http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>Germinate 3 Gatekeeper</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico"/>
	<script src="js/jquery-2.1.4.min.js"></script>
	<script src="js/toastr.min.js"></script>
	<script src="js/zxcvbn.js"></script>

	<script src="js/bootstrap.min.js"></script>

	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"/>
	<link type="text/css" rel="stylesheet" href="css/toastr.css"/>
	<link rel="stylesheet" href="css/bootstrap.min.css"/>

	<script src="gatekeeper/gatekeeper.nocache.js"></script>

	<script>
		$(document).ready(function () {
			$('[data-toggle=offcanvas]').click(function () {
				$('.row-offcanvas').toggleClass('active');
			});

			// Disable anchors with "disabled" class
			$('body').on('click', 'a[disabled]', function (event) {
				event.preventDefault();
			});

			// Disable anchors with "disabled" class
			$('body').on('click', 'a.disabled', function (event) {
				event.preventDefault();
			});
		});
	</script>
</head>

<body>

<header class="navbar navbar-default navbar-static-top" role="banner">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<%--<a href="./" class="navbar-brand">Germinate Gatekeeper</a>--%>
			<a href="#" class="navbar-brand">
				<img src="img/gatekeeper.svg" class="logo">
			</a>
		</div>
		<nav class="collapse navbar-collapse" role="navigation" id="top-navbar">

		</nav>
	</div>
</header>

<!-- Begin Body -->
<div class="container" id="container">
	<div class="row">
		<div class="col-xs-12 col-md-3" id="sidebar" style="display: none;">

			<div class="nav-sidebar" id="nav-sidebar">

			</div>

		</div>
		<div class="col-xs-12 col-md-12" id="dynamic-content">

			<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
			<noscript>
				<div class="panel panel-default">
					<div class="panel-heading">JavaScript</div>
					<div class="panel-body">
						<p>Your web browser must have JavaScript enabled in order for this application to display correctly.</p>
						<p>For guidance on how to enable JavaScript, please follow <a href="http://enable-javascript.com/" target="_blank">this link</a>.</p>
					</div>
				</div>
			</noscript>

		</div>
	</div>
</div>


<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

<!-- login fields used for auto completion of the browser (saved passwords) -->
<div style="display: none;">
	<form id="login-form" method="post" action="">
		<input type="text" id="login-form-username" name="gatekeeper-username" class="form-control" required="" autofocus=""/>
		<input type="password" id="login-form-password" name="gatekeeper-password" class="form-control" required=""/>
	</form>
</div>

<%@include file="footer.jsp" %>

</body>
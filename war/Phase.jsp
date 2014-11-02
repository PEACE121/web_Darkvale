<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ntnu.ui.JSP" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.ntnu.game.model.UserEntity" %>
<%@ page import="com.ntnu.game.model.UserDao" %>
<%@ page import="com.ntnu.ui.JSPCommonParts" %>
<%@ page import="com.ntnu.game.model.gameLogic.GameLogic" %>
<%@ page import="com.ntnu.game.model.gameLogic.finale.FinaleLogic" %>
<%@ page import="com.ntnu.game.model.gameLogic.finale.WordCount" %>

<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>

<html lang="en">
  
  <%= JSPCommonParts.INSTANCE.getHead() %>
  <%
UserDao dao = UserDao.INSTANCE;

UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

GameLogic game = GameLogic.getInstance();
FinaleLogic finale = game.getFinalGame();
%>

  <body style="background-color: black !important;">
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
     
     <style type="text/css">
    body {
    	height: 100%;
    	background-color: black important!; 
    }
	</style>
    
    <%= JSPCommonParts.INSTANCE.getHeader(JSP.PHASE, request.getRequestURI()) %>
 <div class="row">
  <div class="col-md-3"><img class="img-responsive"  src='<%= finale.getCardFile(user.getUserId()) %>' /></div>
  <div class="col-md-3"><img class="img-responsive" src='<%= finale.getTCardFile(user.getUserId()) %>' /></div>
  <div class="col-md-2"><div style="text-align:center;"><img class="img-responsive" src='/images/phase<%=finale.currentPhase()%>.png' /></div></div>
  
  <div class="col-md-4"><div style="text-align:center; padding-left: 20%; padding-top: 30%; padding-bottom: 55%; padding-right:20%;background-image: url(/images/news<%=finale.currentPhase()%>.png);  background-size: 100% 100%;">
  	<h1>News</h1>
  	<%= finale.getSpecialInformation(user.getUserId())%> <br>
  	<% for (int i = 0; i < finale.getGeneralInformation().size(); i++) { 
           String info = finale.getGeneralInformation().get(i) + "<br>";
         %>
        <%= info %> 	
    <% } %>
  </div>
</div>
</div>


<div class="row">
  <div class="col-md-12">
		<div style="text-align:center;">
			Your tasks: 
		</div>
	</div>
</div>
<%= finale.getTaskRoleText(user.getUserId())%>

<% if(finale.currentPhase() ==  "Day") {%>
<div class="row">
  <div class="col-md-12">
		<div style="text-align:center;">
			Your tasks for the current minigame: 
		</div>
	</div>
</div>
<%= finale.getMinigameTaskRoleText(user.getUserId())%>
<% } %>
	  	

<div class="row">
  <div class="col-md-10"><h1><div id="countdown">
		 		</div><!-- /#Countdown Div -->
		 		</h1>
      <script type="text/javascript">
		// variables for time units
		var days, hours, minutes, seconds;
		 
		// get tag element
		var countdown = document.getElementById('countdown');
		 
		var completeTime = <%=finale.phaseTime()%> /1000; 
		// update the tag with id "countdown" every 1 second
		myTimer();
		setInterval(function() {myTimer()}, 1000);
		
		function myTimer() {
		if(completeTime <= 0) {
				window.location = "<%=JSP.SLIDE%>";
			}
		    var seconds_left = completeTime;
		    completeTime -= 1;
		 
		    // do some time calculations
		    // days = parseInt(seconds_left / 86400);
		    // seconds_left = seconds_left % 86400;
		     
		    // hours = parseInt(seconds_left / 3600);
		    // seconds_left = seconds_left % 3600;
		     
		    minutes = parseInt(seconds_left / 60);
		    seconds = parseInt(seconds_left % 60);
		     
		    // format countdown string + set tag value
		    countdown.innerHTML = '<div style="text-align:center;"><span class="minutes">'+ minutes + ' <b>Minutes</b></span> <span class="seconds">' + seconds + ' <b>Seconds</b></span></div>';
		    //'<span class="days">' + days +  ' <b>Days</b></span> <span class="hours">' + hours + ' <b>Hours</b></span> <span class="minutes">'
		    //+ minutes + ' <b>Minutes</b></span> '; 
		    }
			</script>
		</div>
		<div class="col-md-2">
			<h1><form name="readyForm" action="/userEarlierReady" method="post">Ready? <input <%= (finale.isUserReady(user.getUserId())? "checked='checked'":"")%> type="checkbox" name="ready" onclick="this.form.submit();"></input></form></h1> 
		</div>
</div>

	
		<script type="text/javascript">
	var initialGameState = "<%=game.getStringRepresentationOfState()%>";
	setInterval(function() {getCurrentGameState(initialGameState)}, 1000);
	
	function getCurrentGameState(initalState)
	{
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
	    	if(initalState != xmlhttp.responseText) {
				window.location = "<%=JSP.PHASE%>";
			}
	    }
	  }
	xmlhttp.open("GET","/gameState",true);
	xmlhttp.send();
	}
	</script>
	
<% if(finale.currentPhase() ==  "Reflection") {%>
	<script type="text/javascript">
	setInterval(function() {getReflectionContent()}, 1000);
	
	function getReflectionContent()
	{
	var xmlhttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
	    	document.getElementById('reflectionChat').innerHTML = xmlhttp.responseText;
	    }
	  }
	xmlhttp.open("GET","/reflectionChat",true);
	xmlhttp.send();
	}
	</script>
	<%}%>
      
  </body>
</html>
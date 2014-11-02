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

<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>

<html lang="en">
  
  <%= JSPCommonParts.INSTANCE.getHead() %>
  <%
UserDao dao = UserDao.INSTANCE;

UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

if(GameLogic.getInstance() == null) { %>
	alert("Game instance is null");
 <% }

FinaleLogic finale = GameLogic.getInstance().getFinalGame();
%>

  <body>
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
    
     <style type="text/css">
    body {
    	height: 100%;
    	background-color: transparent; 
    }
    html {
    min-height: 100%;
    background-image: url('/images/phase<%=finale.currentPhase()%>.png');
    background-color: black;
    background-repeat: no-repeat;
    background-size: auto 80%;
    background-position: center;
	}
	</style>
    
    <%= JSPCommonParts.INSTANCE.getHeader(JSP.SLIDE, request.getRequestURI()) %>
    <% if(finale!= null && finale.slideTime() < 0) {%>
		<script type="text/javascript">window.location = "<%=JSP.PHASE%>";</script>
	<% }%>
      <h1><div style='position:absolute; bottom:0' id="countdown">
		 		</div><!-- /#Countdown Div -->
		 		</h1>
      <script type="text/javascript">
		// variables for time units
		var days, hours, minutes, seconds;
		 
		// get tag element
		var countdown = document.getElementById('countdown');
		 
		var completeTime = <%=(finale!= null?finale.slideTime():10000)%> /1000; 
		// update the tag with id "countdown" every 1 second
		myTimer();
		setInterval(function() {myTimer()}, 1000);
		
		function myTimer() {
		if(completeTime <= 0) {
				window.location = "<%=JSP.PHASE%>";
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
		    countdown.innerHTML = '<div>Time to start: <span class="minutes">'+ minutes + ' <b>Minutes</b></span> <span class="seconds">' + seconds + ' <b>Seconds</b></span></div>';
		    //'<span class="days">' + days +  ' <b>Days</b></span> <span class="hours">' + hours + ' <b>Hours</b></span> <span class="minutes">'
		    //+ minutes + ' <b>Minutes</b></span> '; 
		    }
			</script>
 
  </body>
</html>
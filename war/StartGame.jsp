<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ntnu.ui.JSP" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.ntnu.game.model.UserEntity" %>
<%@ page import="com.ntnu.game.model.UserDao" %>
<%@ page import="com.ntnu.game.model.gameLogic.GameLogic" %>
<%@ page import="com.ntnu.game.model.gameLogic.EGameState" %>
<%@ page import="com.ntnu.ui.JSPCommonParts" %>

<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>

<html lang="en">
  
  <%= JSPCommonParts.INSTANCE.getHead() %>
  <%
UserDao dao = UserDao.INSTANCE;

GameLogic game = GameLogic.getInstance();

UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
            
if (user != null){
	dao.register();
} 

List<UserEntity> users = dao.listUsers();
%>

  <body>
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
    
    <%= JSPCommonParts.INSTANCE.getHeader(JSP.GAME, request.getRequestURI()) %>
    
    <style type="text/css">
    body {
    	height: 100%;
    	background-color: transparent; 
    }
    html {
    min-height: 100%;
    background-image: url('/images/clocktower.jpg');
    background-color: black;
    background-repeat: no-repeat;
    background-size: 40%;
    background-position: right 50px;
	}
	</style>
	
	<!-- <button style="margin-left: auto; -->
    <!-- margin-right: auto;" type="submit" class="btn btn-lg btn-danger">Start the Game</button> -->
 	<% if(game.isRunning()) {%>
		<script type="text/javascript">window.location = "<%=JSP.SLIDE%>";</script>
	<% }%>
	<h1><div id="countdown">
		 		</div><!-- /#Countdown Div -->
		 		</h1>
 	 <% if ( game.isReady() ) {%>
		 		
		<script type="text/javascript">
		// variables for time units
		var days, hours, minutes, seconds;
		 
		// get tag element
		var countdown = document.getElementById('countdown');
		 
		var completeTime = <%=game.getTime()%> /1000; 
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
		    countdown.innerHTML = 'Time to start: <span class="minutes">'+ minutes + ' <b>Minutes</b></span> <span class="seconds">' + seconds + ' <b>Seconds</b></span>';
		    //'<span class="days">' + days +  ' <b>Days</b></span> <span class="hours">' + hours + ' <b>Hours</b></span> <span class="minutes">'
		    //+ minutes + ' <b>Minutes</b></span> '; 
		    }
			</script>
		 	<% } else {%>
		 	<script type="text/javascript">countdown.innerHTML = '<%=game.playersNeeded()%> players needed to start the game!';</script>
		 	<% }%>
 	
 	
 	<h1 style="width: 60%;margin-left: auto;
    margin-right: auto;">Active Users</h1>
<div style="width: 50%;" class="panel panel-default">
	<table class='table table-bordered table-hover table-striped tablesorter'>
      <TR>
         <TH class='fa fa-sort'></TH>
         <TH class='fa fa-sort'>Nickname</TH>
         <TH class='fa fa-sort'>Ready?</TH>
      </TR>
		<% for (int i = 0; i < users.size(); i++) { 
           UserEntity userEntity = users.get(i);
        %>
                <TR>
                    <TD> <img src='<%= userEntity.getUserImage() %>' class='img-responsive' style='height:20px;;'> </TD>
                    <TD> <%= userEntity.getName() %> </TD>
                    <TD> <form name="readyForm" action="/userReady" method="post"><input <%= (user==null || !userEntity.getGoogleUserId().equals(user.getUserId())? "disabled='disabled'":"")%> <%= (game.isUserReady(userEntity.getGoogleUserId())? "checked='checked'":"")%> type="checkbox" name="ready" onclick="this.form.submit();"></input></form> </TD>
                </TR>
                
		<% } %>
	</table>
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
				window.location = "<%=JSP.GAME%>";
			}
	    }
	  }
	xmlhttp.open("GET","/gameState",true);
	xmlhttp.send();
	}
	</script>
	
  </body>
</html>
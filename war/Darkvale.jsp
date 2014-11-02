<%@ page import="com.ntnu.ui.JSPCommonParts" %>
<%@ page import="com.ntnu.ui.JSP" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.ntnu.game.model.UserEntity" %>
<!DOCTYPE html>

<%

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserEntity userEntity = null;
		
		String url = userService.createLoginURL(JSP.MAIN);
		String urlLinktext = "Login";
		if (user != null)
		{
			url = userService.createLogoutURL(JSP.MAIN);
			urlLinktext = "Logout";
		}
			
%>

<html lang="en">

  <%= JSPCommonParts.INSTANCE.getHead() %>
  

  <body style="background:black !important">
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
    
    <%= JSPCommonParts.INSTANCE.getHeader(JSP.MAIN, request.getRequestURI()) %>
    
     <style type="text/css">
    body {
    	height: 100%;
    	background-color: transparent; 
    }
    html {
    background-image: url('images/titlewallpaper02.jpg');
    background-color: black;
    background-repeat: no-repeat;
    background-size: 100%;
	}
	</style>
      
      	<button style="position:absolute; left:20%; top:20%" class="btn btn-default" onclick="window.location.href='<%=url%>'" >
      		<%=urlLinktext%>
		</button>
		
  </body>
</html>
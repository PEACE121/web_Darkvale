<%@ page import="java.util.List" %>
<%@ page import="com.ntnu.ui.JSP" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.ntnu.game.model.UserEntity" %>
<%@ page import="com.ntnu.game.model.UserDao" %>
<%@ page import="com.ntnu.ui.JSPCommonParts" %>

<!DOCTYPE html>

<html lang="en">

  <%= JSPCommonParts.INSTANCE.getHead() %>
    <%
UserDao dao = UserDao.INSTANCE;

UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
UserEntity userEntity = null;
            
if (user != null){
	userEntity = dao.getUserById(user.getUserId());
} 
%>

  <body style="background:black !important">
   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
    
    <%= JSPCommonParts.INSTANCE.getHeader(JSP.PROFILE, request.getRequestURI()) %>
    <form name="profile" action="/userInformation" method="post">
	    <div class="row">
	  		<div class="col-md-3">
	  			Username: 
	  		</div>
	  		<div class="col-md-9">
	      		<input style="width:100%" class="form-control" <%= (userEntity==null? "disabled='disabled'":"")%> type="text" name="username" value="<%= (userEntity==null? "'":userEntity.getName())%>"></input>
	     	</div>
	     </div>
	    <div class="row">
	  		<div class="col-md-3">
	  			Profile Image: 
	  		</div>
	  		<div class="col-md-9">
	      		<input style="width:100%" class="form-control" <%= (userEntity==null? "disabled='disabled'":"")%> type="text" name="profileImage" value="<%= (userEntity==null? "'":userEntity.getUserImage())%>"></input>
	     	</div>
	     </div>
	     <div class="row">
	     	<div class="col-md-12">
	     		<input class="btn btn-default" type="submit" value="Change">
	     	</div>
	     </div>
     </form> 
  </body>
</html>
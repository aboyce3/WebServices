package messageboardapp.dropwizard.resources;

import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.sessions.Session;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;

@Path("/Login")
@Produces(MediaType.TEXT_HTML)
@Timed
@ExceptionMetered
public class Login{
	Hub hub;
    public Login(Hub hub) {
        super();
        this.hub = hub;
    }

    @GET
	public String login(@Session HttpSession session) {
		  if(session.getAttribute("userName") != null) { 
			  return hub.hub(session);
		  }
		 
		return "<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>MessageBoard Login</title>\r\n" + 
	    		"</head>\r\n" + 
	    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/style.css\" />\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"		<form method=\"post\" action=\"ValidateLogin\">\r\n" + 
	    		"			<table>\r\n"+
	    		"				<tr>\r\n" + 
	    		"					<td><a href=\"CreateAccount\"><b>Create an account</b></a></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><b>User Name</b></td>\r\n" + 
	    		"					<td><input type=\"text\" name=\"userName\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><b>Password</b></td>\r\n" + 
	    		"					<td><input type=\"password\" name=\"password\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><input type=\"submit\" value=\"Login\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><b>Project by Andrew Boyce</b></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"			</table>\r\n" + 
	    		"		</form>\r\n" + 
	    		"	</div>\r\n" + 
	    		"</body>\r\n" + 
	    		"</html>";
	}

	//protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}

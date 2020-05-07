package messageboardapp.dropwizard.resources;

import javax.servlet.http.HttpSession;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.jersey.sessions.Session;

@Path("/CreateAccount")
@Produces(MediaType.TEXT_HTML)
public class CreateAccount{
       
	Hub hub;
    public CreateAccount(Hub hub) {
        super();
        this.hub = hub;
    }

	@GET
	public String create(@Session HttpSession session) {
			  if(session.getAttribute("userName") != null) { 
				  return hub.hub(session);
			  }
		return "<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Create An Account</title>\r\n" + 
	    		"</head>\r\n" + 
	    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"assets/style.css\" />\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"		<form method=\"post\" action=\"AccountCreation\">\r\n" + 
	    		"			<table>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td>User Name</td>\r\n" + 
	    		"					<td><input type=\"text\" name=\"userName\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td>Email</td>\r\n" + 
	    		"					<td><input type=\"text\" name=\"email\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td>Password</td>\r\n" + 
	    		"					<td><input type=\"password\" name=\"password\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td>Re-Enter Password</td>\r\n" + 
	    		"					<td><input type=\"password\" name=\"password2\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><input type=\"submit\" value=\"Submit\"></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"			</table>\r\n" + 
	    		"		</form>\r\n" + 
	    		"	</div>\r\n" + 
	    		"</body>\r\n" + 
	    		"</html>";
	}

}

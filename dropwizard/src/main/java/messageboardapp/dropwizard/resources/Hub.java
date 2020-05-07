package messageboardapp.dropwizard.resources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.jersey.sessions.Session;
import messageboardapp.dropwizard.core.Message;
import messageboardapp.dropwizard.db.MessageDAO;

@Path("/Hub")
public class Hub{
    Messages messages;
    MessageDAO messageDAO;
    public Hub(MessageDAO messageDAO) {
    	super();
    	messages = new Messages(messageDAO);
    	this.messageDAO = messageDAO;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
	public String hub(@Session HttpSession session) {
    	Login login = new Login(this);
	    if(session.getAttribute("userName") == null) return login.login(session);
	    String s = (String) session.getAttribute("userName");
		return "<!DOCTYPE html>\r\n" + 
	    		"<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Hub</title>\r\n" + 
	    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"assets/style.css\" />\r\n" + 
	    		"</head>\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"<form action=\"Logout\" method=\"post\">\r\n" + 
	    		"		<br/>	<input type=\"submit\" value=\"Log Out\">			\r\n" + 
	    		"		</form>\r\n" + 
	    		"<textarea rows=\"30\" cols=\"80\">\r\n" + 
	    		"Welcome to the Message Board: "+s+"\n"+allMessages()+
	    		"</textarea>"+
	    		"<form action=\"Hub\" method=\"post\">\r\n" + 
	    		"			<table style=\"with: 50%\">\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><input type=\"text\" size=\"72\" name=\"textEntry\" /></td>\r\n" + 
	    		"                    <td><input type=\"submit\" value=\"Submit\" /></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"            </form>"+
	    		"	</div>\r\n" + 
	    		"</body>\r\n" + 
	    		"</html>";
	}
    
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String update(@FormParam("textEntry") String entry, @Session HttpSession session) {
    	if(entry != null) {
  		  LocalDateTime myDateObj = LocalDateTime.now();
  		  DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
  		  String formattedDate = myDateObj.format(myFormatObj); Message m = new
  		  Message((String)session.getAttribute("userName"),entry,formattedDate);
  		  messageDAO.insert(m.getUserName(), m.getMessage(), m.getDate()); 
  		  return hub(session); 
  		  }
    	return hub(session); 
    }
    
    public String allMessages() {
    	List<Message> list = messageDAO.getAll();
    	String allMessages = "";
    	for(Object message : list) {
    		Message m = (Message) message;
    		allMessages += m.getUserName() + ": " + m.getMessage() + "\n";
    	}
    	return allMessages;
    }
    
  }

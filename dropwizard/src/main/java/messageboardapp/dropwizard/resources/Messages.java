package messageboardapp.dropwizard.resources;

import java.time.LocalDateTime;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import messageboardapp.dropwizard.core.Message;
import messageboardapp.dropwizard.db.MessageDAO;

@Path("/Messages")
@Produces({ MediaType.APPLICATION_JSON })
public class Messages {
	MessageDAO messageDAO;

	public Messages(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	@GET
	public List<Message> getMessages() {
		return messageDAO.getAll();
	}

	@GET
	@Path("/{username}")
	public String getMessageByUser(@PathParam("username") String username) {
		List<Message> all = messageDAO.getAll();
		ArrayList<Message> specific = new ArrayList<>();
		for(Object message : all) {
			Message m = (Message) message;
			if(m.getUserName().contentEquals(username)) {
				specific.add(m);
			}
		}
		return specific.toString() + "\n";
	}
	
	@DELETE
	@Path("/{username}/{message}")
	public String delete(@PathParam("username") String username, @PathParam("message") String message) {
		try {
			messageDAO.delete(username, message);
			return "Success \n";
		} catch (Exception e) {
			return e.getMessage() + "\n";
		}
	}

	@PUT
	@Path("/{username}/{message}/{newMessage}")
	public String put(@PathParam("username") String username, @PathParam("message") String message, @PathParam("newMessage") String newMessage) {
		messageDAO.update(username,message,newMessage);
		ArrayList<Message> updatedMessages = new ArrayList<>();
		for(Object messages : messageDAO.getAll()) {
			Message m = (Message) messages;
			if(m.getUserName().contentEquals(username) && m.getMessage().contentEquals(newMessage)) {
				updatedMessages.add(m);
			}
		}
		return updatedMessages.toString()+"\n";
		
	}
	
	@POST
	@Path("/{username}/{message}")
	public String insert(@PathParam("username") String username, @PathParam("message") String message) {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = myDateObj.format(myFormatObj);
		this.messageDAO.insert(username, message, formattedDate);
		return new Message(username,message,formattedDate).toString()+"\n";
	}

}

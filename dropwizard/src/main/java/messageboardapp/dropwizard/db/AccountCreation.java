package messageboardapp.dropwizard.db;

import javax.servlet.http.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.dropwizard.jersey.sessions.Session;
import messageboardapp.dropwizard.resources.ErrorCreating;
import messageboardapp.dropwizard.resources.Hub;

import java.util.regex.Pattern;

@Path("/AccountCreation")
public class AccountCreation {
	Hub hub;
	MessageDAO messageDAO;

	public AccountCreation(Hub hub, MessageDAO messageDAO){
		super();
		this.hub = hub;
		this.messageDAO = messageDAO;
	}

	@POST
	public String add(@FormParam("userName") @NotEmpty String userName, @FormParam("password") @NotNull String password,
			@FormParam("password2") @NotNull String password2, @FormParam("email") @NotNull String email,
			@Session HttpSession session) {
		ErrorCreating error = new ErrorCreating(hub);
		if (userName == null || "".equals(userName) || password == null || "".equals(password) || password2 == null
				|| "".equals(password2)) {
			return error.errorCreating(session);
		} else if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?"
				+ "^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
				+ "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
				+ "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
				+ "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)) {
			return error.errorCreating(session);
		} else if (password == null || "".equals(password) || !password.equals(password2)) {
			return error.errorCreating(session);
		} else {
			if(!messageDAO.exists(userName)) {
			session.setAttribute("userName", userName);
			session.setMaxInactiveInterval(10 * 60);
			return hub.hub(session);
			}else {
				return error.errorCreating(session);
			}
		}
	}

}

package messageboardapp.dropwizard.db;

import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.dropwizard.jersey.sessions.Session;
import messageboardapp.dropwizard.resources.ErrorLogin;
import messageboardapp.dropwizard.resources.Hub;

@Path("/ValidateLogin")
public class ValidateLogin {
	MessageDAO messageDAO;
	Hub hub;
	public ValidateLogin(MessageDAO messageDAO, Hub hub) {
		super();
		this.messageDAO = messageDAO;
		this.hub = hub;
	}

	@POST
	public String validate(@FormParam("userName") String userName, @FormParam("password") String password,@Session HttpSession session) {
		if (messageDAO.exists(userName)) {
			session.setAttribute("userName", userName);
			session.setMaxInactiveInterval(10 * 60);
			return hub.hub(session);
		} else {
			ErrorLogin error = new ErrorLogin(hub);
			return error.errorLogin(session);
		}
	}
}

package messageboardapp.dropwizard.resources;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.dropwizard.jersey.sessions.Session;

@Path("Logout")
public class Logout{
 Hub hub;
	
    public Logout(Hub hub) {
        super();
        this.hub = hub;
    }

    @POST
	protected String logout(@Session HttpSession session) {
		session.invalidate();
        Login login = new Login(hub);
        return login.login(session);
	}

}

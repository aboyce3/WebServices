package messageboardapp.dropwizard;

import javax.sql.DataSource;

import org.eclipse.jetty.server.session.SessionHandler;
import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import messageboardapp.dropwizard.core.MessageMapper;
import messageboardapp.dropwizard.db.AccountCreation;
import messageboardapp.dropwizard.db.MessageDAO;
import messageboardapp.dropwizard.db.ValidateLogin;
import messageboardapp.dropwizard.resources.CreateAccount;
import messageboardapp.dropwizard.resources.ErrorCreating;
import messageboardapp.dropwizard.resources.ErrorLogin;
import messageboardapp.dropwizard.resources.Hub;
import messageboardapp.dropwizard.resources.Login;
import messageboardapp.dropwizard.resources.Logout;
import messageboardapp.dropwizard.resources.Messages;

public class MessageBoardApplication extends Application<MessageBoardConfiguration> {

	public static void main(final String[] args) throws Exception {
		new MessageBoardApplication().run(new String[] { "server", "config.yml" });
	}

	@Override
	public String getName() {
		return "Message Board";
	}

	@Override
	public void initialize(final Bootstrap<MessageBoardConfiguration> bootstrap) {

		bootstrap.addBundle(new AssetsBundle("/assets/"));
	}

	@Override
	public void run(MessageBoardConfiguration configuration, Environment environment) throws Exception {
		final DataSource dataSource = configuration.getDataSourceFactory().build(environment.metrics(), "sql");
		DBI dbi = new DBI(dataSource);
		final MessageDAO messageDAO = dbi.onDemand(MessageDAO.class);
		Hub hub = new Hub(messageDAO);
		environment.jersey().register(new Messages(messageDAO));
		environment.jersey().register(new Login(hub));
		environment.jersey().register(new CreateAccount(hub));
		environment.jersey().register(new ErrorCreating(hub));
		environment.jersey().register(new AccountCreation(hub, messageDAO));
		environment.jersey().register(new ValidateLogin(messageDAO, hub));
		environment.jersey().register(new MessageMapper());
		environment.jersey().register(new ErrorLogin(hub));
		environment.jersey().register(hub);
		environment.jersey().register(new Logout(hub));
		environment.servlets().setSessionHandler(new SessionHandler());
	}
}

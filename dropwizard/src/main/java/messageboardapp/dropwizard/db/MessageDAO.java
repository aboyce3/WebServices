package messageboardapp.dropwizard.db;

import messageboardapp.dropwizard.core.*;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface MessageDAO {
	
	@SqlQuery("select * from chat")
	@Mapper(MessageMapper.class)
	public List<Message> getAll();
	
	@SqlUpdate("insert into chat (username, message, date) values (:name, :message, :date)")
	void insert(@Bind("name") String name,@Bind("message") String message,@Bind("date") String date);
	
	@SqlUpdate("UPDATE chat SET message = :newMessage WHERE username = :username and message = :message")
	void update( @Bind("username") String username, @Bind("message") String message, @Bind("newMessage") String newMessage);
	
	@SqlUpdate("DELETE FROM chat WHERE username = :username and message = :message")
	void delete(@Bind("username") String username, @Bind("message") String message);
	
	@SqlUpdate("insert into users (username, password, email) values (:username, :password, :email)")
	void insertUser(@Bind("username") String username,@Bind("password") String password,@Bind("email") String email);
	
	@SqlQuery("select COUNT(*) from users where username = :username")
	boolean exists( @Bind("username") String username);
}

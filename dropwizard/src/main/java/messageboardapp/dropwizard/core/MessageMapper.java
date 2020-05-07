package messageboardapp.dropwizard.core;


import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements ResultSetMapper<Message>
{
    public Message map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        return new Message(resultSet.getString("username"), resultSet.getString("message"), resultSet.getString("date"));
    }
}
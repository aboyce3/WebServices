import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Messages {

	ArrayList<Message> messages;

	public Messages() {
		messages = initMessages();
	}

	public String messages() {
		messages = initMessages();
		String s = "\n";
		if (messages != null && messages.size() > 0) {
			for (int i = messages.size() - 1; i > -1; i--) {
				Message m = messages.get(i);
				if (m == null)
					break;
				else
					s += "\n" + m + "\n";
			}
		}
		return s;
	}

	public ArrayList<Message> initMessages() {
		ArrayList<Message> temp = new ArrayList<>();
		Connection con = null;
		String lookup = "SELECT * FROM chat;";
		Statement statement = null;
		ResultSet results = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			statement = con.createStatement();
			results = statement.executeQuery(lookup);
			while (results.next()) {
				String username = results.getString("username");
				String message = results.getString("message");
				String date = results.getString("date");
				temp.add(new Message(username, message, date));
			}
			return temp;
		} catch (SQLException | ClassNotFoundException e) {
			return null;
		}
	}

	public void add(Message m) {
		messages.add(m);
	}

	public ArrayList<Message> messagesJson() {
		return this.messages;
	}

	public String toHTML() {
		String log = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>Chat Log</title>\r\n" + "</head>\r\n" + "<body>\r\n";

		for (Message m : messages) {
			log += "<br>" + m.toString() + " (" + m.date + ")" + "</br>";
		}

		log += "</body>\r\n" + "</html>";
		return log;
	}

	public String toJSON() {
		boolean b = false;
		String response = "[";
		for (Message m : messages) {
			response += m.toJSON() + ",";
			b = true;
		}
		if (b)
			response = response.substring(0, response.length() - 1);
		response += "]";
		return response;
	}

	public boolean sizeEqual() {
		Connection con = null;
		String lookup = "SELECT count(*) FROM chat;";
		Statement statement = null;
		ResultSet results = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			statement = con.createStatement();
			results = statement.executeQuery(lookup);
			results.next();
			int count = results.getInt(1);
			if (messages.size() == count)
				return true;
			else
				return false;
		} catch (SQLException | ClassNotFoundException e) {
			return false;
		}
	}

	private void deleteFromDB(Message m) {
		Connection con = null;
		String delete = "DELETE FROM chat WHERE username = '" + m.userName + "' AND message = '" + m.message + "';";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			Statement stmt = con.createStatement();
			stmt.executeUpdate(delete);
		} catch (SQLException | ClassNotFoundException e) {
		}
	}

	private void editOccurencesInDB(Message m, String newMessage) {
		Connection con = null;
		String update = "UPDATE chat SET message = '" + newMessage + "' " + "WHERE message = '" + m.message
				+ "' AND username = '" + m.userName + "';";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			Statement stmt = con.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException | ClassNotFoundException e) {
		}
	}

	private void addToDB(Message m) throws SQLException {
		Connection con = null;
		String insert = "INSERT into chat " + "(username, message, date)" + "VALUES " + "('" + m.userName + "','"
				+ m.message + "','" + m.date + "')";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insert);
		} catch (SQLException | ClassNotFoundException e) {
		}

	}

	public String getAll() {
		if (!sizeEqual())
			messages = initMessages();
		return toJSON();
	}

	public String getByName(String userName, String message) {
		if (!sizeEqual())
			messages = initMessages();
		Boolean found = false;
		String output = "[";
		try {
			for (Message m : messages) {
				if (m.getUserName().contentEquals(userName) && m.getMessage().contentEquals(message)) {
					output += m.toJSON() + ", ";
					found = true;
				}
			}
			if (found == false)
				return "No Entry Found";
			output = output.substring(0, output.lastIndexOf(", "));
			output += "]";
			return output;
		} catch (Exception e) {
			return "invalid syntax";
		}
	}

	public String deleteMessage(String userName, String message) {
		boolean alreadyDeleted = false;
		if (!sizeEqual())
			messages = initMessages();
		for (Message m : messages) {
			if (m.getUserName().contentEquals(userName) && m.getMessage().contentEquals(message)) {
				messages.remove(m);
				if (alreadyDeleted == false) {
					deleteFromDB(m);
					alreadyDeleted = true;
				}
			}
		}
		if (alreadyDeleted)
			return "Successfully Deleted.";
		else
			return "Unsuccessful Deletion";
	}

	protected String postMessage(String userName, String message) {
		if (!sizeEqual())
			messages = initMessages();
		try {
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = myDateObj.format(myFormatObj);
			Message m = new Message(userName, message, formattedDate);
			add(m);
			addToDB(m);
			return m.toJSON();
		} catch (Exception e) {
			return "Could not add the message.";
		}
	}

	public String modifyMessage(String userName, String message, String newMessage) {
		String output = "[";
		boolean alreadyEdited = false;
		if (!sizeEqual())
			messages = initMessages();
		try {
			for (Message m : messages) {
				if (m.getUserName().contentEquals(userName) && m.getMessage().contentEquals(message)) {
					if (alreadyEdited == false) {
						editOccurencesInDB(m, newMessage);
					}
					m.message = newMessage;
					output += m.toJSON() + ", ";
				}
			}
			output = output.substring(0, output.lastIndexOf(", "));
			output += "]";
			if (alreadyEdited)
				return output;
			else
				return "No message found.";
		} catch (Exception e) {
			return "No message found.";
		}
	}

}

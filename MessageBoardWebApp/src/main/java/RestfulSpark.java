import static spark.Spark.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class RestfulSpark {

	  
	public static void main(String[] args) {
		get("/Hub/chat", (request, response) -> {
		Messages messages = new Messages();
		response.type("application/json; charset=UTF-8");
		return messages.toJSON();
			});
		get("/Hub", (request, response) -> {
	        String s = request.session().attribute("username");
		response.type("text/html");
	        Messages messages = new Messages();
	    		if(request.session().attribute("username") == null) response.redirect("/Login");
	    if(request.queryParams("textEntry") != null) {
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String formattedDate = myDateObj.format(myFormatObj);
	 	Message m = new Message(s,request.queryParams("textEntry"),formattedDate);
	    messages.add(m);
	    addDB(s,request.queryParams("textEntry"),formattedDate);
	    response.redirect("/Hub");
	    }
	    request.attribute("textEntry", null);
	    return "<!DOCTYPE html>\r\n" + 
	    		"<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Hub</title>\r\n" + 
	    		"<link rel=\"stylesheet\" href=\"https://raw.githack.com/aboyce3/WebServices/master/MessageBoardWebApp/style.css\" />\r\n" +
	    		"</head>\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"<form action=\"Logout\" method=\"get\">\r\n" + 
	    		"		<br/>	<input type=\"submit\" value=\"Log Out\">			\r\n" + 
	    		"		</form>\r\n" + 
	    		"<form action=\"/Hub/chat\" method=\"get\">\r\n" + 
	    		"		<br/>	<input type=\"submit\" value=\"Chat Log\">			\r\n" + 
	    		"		</form>\r\n" + 
	    		"<textarea rows=\"30\" cols=\"80\">\r\n" + 
	    		"Welcome to the message board: "+s+
	    		messages.messages()
	    		+
	    		"</textarea>"+
	    		"<form action=\"Hub\" method=\"get\">\r\n" + 
	    		"			<table style=\"with: 50%\">\r\n" + 
	    		"				<tr>\r\n" + 
	    		"					<td><input type=\"text\" size=\"72\" name=\"textEntry\" /></td>\r\n" + 
	    		"                    <td><input type=\"submit\" value=\"Submit\" /></td>\r\n" + 
	    		"				</tr>\r\n" + 
	    		"            </form>"+
	    		"	</div>\r\n" + 
	    		"</body>\r\n" + 
	    		"</html>";
	    	});
		get("/AccountCreation", (request, response) -> {
			String userName = request.queryParams("userName");
			String password = request.queryParams("password");
			String password2 = request.queryParams("password2");
			String email = request.queryParams("email");
			Connection con;
			String insert = "INSERT into users " + "(username, password, email)" + "VALUES " + "('" + userName + "','"
					+ password + "','" + email + "')";

			String lookup = "SELECT * FROM messageboard.users WHERE username = '" + userName + "'";
			Statement statement = null;
			ResultSet result = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
				statement = con.createStatement();
				result = statement.executeQuery(lookup);
				while (result.next()) {
					String uName = result.getString("username");
					if (userName.contentEquals(uName))
						response.redirect("/ErrorCreating");
					return "";
				}
			} catch (SQLException | ClassNotFoundException e) {
				System.out.println(e);
				e.printStackTrace();
			}
			response.type("text/html");
			if (userName == null || "".equals(userName) || password == null || "".equals(password) || password2 == null
					|| "".equals(password2)) {
				response.redirect("ErrorCreating");
			} else if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?"
					+ "^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
					+ "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
					+ "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
					+ "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)) {
				response.redirect("/ErrorCreating");
				return "";
			} else if (password == null || "".equals(password) || !password.equals(password2)) {
				response.redirect("/ErrorCreating");
			} else {
				try {
					if (statement != null) {
						statement.executeUpdate(insert);
						request.session().attribute("username", userName);
						request.session().maxInactiveInterval(9999);
						response.redirect("/Hub");
						return"";
					} else
						response.redirect("/ErrorCreating");
					return "";
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			response.redirect("/ErrorCreating");
			return "";
		    
		});
		get("/ErrorCreating", (request, response) -> {
			response.type("text/html");
			if(request.session().attribute("userName") != null) {
				response.redirect("/Hub");
				return "";
		    }
			return"<html>\r\n" + 
    		"<head>\r\n" + 
    		"<meta charset=\"ISO-8859-1\">\r\n" + 
    		"<title>Create An Account</title>\r\n" + 
    		"</head>\r\n" + 
    		"<link rel=\"stylesheet\" href=\"https://raw.githack.com/aboyce3/WebServices/master/MessageBoardWebApp/style.css\" />\r\n" + 
    		"<body>\r\n" + 
    		"	<div id=\"wrapper\">\r\n" + 
    		"		<form method=\"get\" action=\"AccountCreation\">\r\n" + 
    		"			<table>\r\n" + 
    		"			<font color=\"red\">Invalid credentials or this user already\r\n" + 
    		"		exists!</font>\r\n" + 
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
		    
		});
		get("/CreateAnAccount", (request, response) -> {
			response.type("text/html");
			if(request.session().attribute("userName") != null) {
				response.redirect("Hub");
				return "";
		    }
			return "<html>\r\n" + 
		    		"<head>\r\n" + 
		    		"<meta charset=\"ISO-8859-1\">\r\n" + 
		    		"<title>Create An Account</title>\r\n" + 
		    		"</head>\r\n" + 
		    		"<link rel=\"stylesheet\" href=\"https://raw.githack.com/aboyce3/WebServices/master/MessageBoardWebApp/style.css\" />\r\n" + 
		    		"<body>\r\n" + 
		    		"	<div id=\"wrapper\">\r\n" + 
		    		"		<form method=\"get\" action=\"AccountCreation\">\r\n" + 
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
		    
		});
		get("/ErrorLogin", (request, response) -> {
			response.type("text/html");
			if(request.session().attribute("userName") != null) {
				response.redirect("Hub");
				return "";
		    }
			return "<html>\r\n" + 
		    		"<head>\r\n" + 
		    		"<meta charset=\"ISO-8859-1\">\r\n" + 
		    		"<title>MessageBoard Login</title>\r\n" + 
		    		"</head>\r\n" + 
		    		"<link rel=\"stylesheet\" href=\"https://raw.githack.com/aboyce3/WebServices/master/MessageBoardWebApp/style.css\" />\r\n" + 
		    		"<body>\r\n" + 
		    		"	<div id=\"wrapper\">\r\n" + 
		    		"		<form method=\"get\" action=\"ValidateLogin\">\r\n" + 
		    		"			<table>\r\n"+"<font color=\"red\">Invalid credentials!</font>" +
		    		"				<tr>\r\n" + 
		    		"					<td><a href=\"CreateAnAccount\"><b>Create an account</b></a></td>\r\n" + 
		    		"				</tr>\r\n" + 
		    		"				<tr>\r\n" + 
		    		"					<td><b>User Name</b></td>\r\n" + 
		    		"					<td><input type=\"text\" name=\"userName\"></td>\r\n" + 
		    		"				</tr>\r\n" + 
		    		"				<tr>\r\n" + 
		    		"					<td><b>Password</b></td>\r\n" + 
		    		"					<td><input type=\"password\" name=\"password\"></td>\r\n" + 
		    		"				</tr>\r\n" + 
		    		"				<tr>\r\n" + 
		    		"					<td><input type=\"submit\" value=\"Login\"></td>\r\n" + 
		    		"					<!-- <td><b>Stay logged in?<input type=\"checkbox\"\r\n" + 
		    		"							name=\"remember\" value=\"true\" /></b></td>-->\r\n" + 
		    		"				</tr>\r\n" + 
		    		"				<tr>\r\n" + 
		    		"					<td><b>Project by Andrew Boyce</b></td>\r\n" + 
		    		"				</tr>\r\n" + 
		    		"			</table>\r\n" + 
		    		"		</form>\r\n" + 
		    		"	</div>\r\n" + 
		    		"</body>\r\n" + 
		    		"</html>";
		    
		});
		get("/Login", (request, response) -> {
			response.type("text/html");
			if(request.session().attribute("username") != null) {
				response.redirect("Hub");
				return "";
		    }
			return "<html>\r\n" + 
    		"<head>\r\n" + 
    		"<meta charset=\"ISO-8859-1\">\r\n" + 
    		"<title>MessageBoard Login</title>\r\n" + 
    		"</head>\r\n" + 
    		"<link rel=\"stylesheet\" href=\"https://raw.githack.com/aboyce3/WebServices/master/MessageBoardWebApp/style.css\" />\r\n" + 
    		"<body>\r\n" + 
    		"	<div id=\"wrapper\">\r\n" + 
    		"		<form method=\"get\" action=\"ValidateLogin\">\r\n" + 
    		"			<table>\r\n"+
    		"				<tr>\r\n" + 
    		"					<td><a href=\"CreateAnAccount\"><b>Create an account</b></a></td>\r\n" + 
    		"				</tr>\r\n" + 
    		"				<tr>\r\n" + 
    		"					<td><b>User Name</b></td>\r\n" + 
    		"					<td><input type=\"text\" name=\"userName\"></td>\r\n" + 
    		"				</tr>\r\n" + 
    		"				<tr>\r\n" + 
    		"					<td><b>Password</b></td>\r\n" + 
    		"					<td><input type=\"password\" name=\"password\"></td>\r\n" + 
    		"				</tr>\r\n" + 
    		"				<tr>\r\n" + 
    		"					<td><input type=\"submit\" value=\"Login\"></td>\r\n" + 
    		"				</tr>\r\n" + 
    		"				<tr>\r\n" + 
    		"					<td><b>Project by Andrew Boyce</b></td>\r\n" + 
    		"				</tr>\r\n" + 
    		"			</table>\r\n" + 
    		"		</form>\r\n" + 
    		"	</div>\r\n" + 
    		"</body>\r\n" + 
    		"</html>";
		    
		});
		get("/Logout", (request, response) -> {
			request.session().invalidate();
	        response.redirect("/Login");
	        return "";
		});
		get("/Messages/", (request, response) -> {
			Messages messages = new Messages();
			response.type("application/json");
			return messages.getAll();
		});
		get("/Messages/:username/:message", (request, response) -> {
			Messages messages = new Messages();
			response.type("application/json");
			return messages.getByName(request.params(":username"), request.params(":message"));
		});
		delete("/Messages/:username/:message", (request, response) -> {
			Messages messages = new Messages();
			response.type("application/json");
			return messages.deleteMessage(request.params(":username"), request.params(":message"));
		});
		post("/Messages/:username/:message", (request, response) -> {
			Messages messages = new Messages();
			response.type("application/json");
			return messages.postMessage(request.params(":username"), request.params(":message"));
		});
		put("/Messages/:username/:message/:newMessage", (request, response) -> {
			Messages messages = new Messages();
			response.type("application/json");
			return messages.modifyMessage(request.params(":username"), request.params(":message"), request.params(":newMessage"));
		});
		get("/ValidateLogin", (request,response) -> {
			String userName = request.queryParams("userName");
			String password = request.queryParams("password");
			Connection con = null;
			String lookup = "SELECT * FROM messageboard.users WHERE username = '" + userName + "' AND password = '"
					+ password + "';";
			Statement statement = null;
			ResultSet results = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
				statement = con.createStatement();
				results = statement.executeQuery(lookup);
				while (results.next()) {
					String uName = results.getString("username");
					String pass = results.getString("password");
					if (userName.contentEquals(uName) && password.contentEquals(pass)) {
						request.session().attribute("username", userName);
						request.session().maxInactiveInterval(99999);;
						response.redirect("/Hub");
						return "";
					}
				}
				response.redirect("/ErrorLogin");
				return "";
			} catch (SQLException | ClassNotFoundException e) {
				response.redirect("ErrorLogin");
				return "";
			}
		});
	}

	private static void addDB(String userName, String message, String date){
		Connection con = null;
		String insert = "INSERT INTO chat(username, message, date)" +
				"VALUES ('"+userName+"', '"+message+"', '"+date+"')";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root", "password");
			Statement st = con.createStatement();
		    st.executeUpdate(insert);
		    con.close();
		} catch (SQLException | ClassNotFoundException e) {
			return;
		}
	}
}

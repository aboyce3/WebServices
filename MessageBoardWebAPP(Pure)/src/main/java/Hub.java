import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Hub extends HttpServlet {
	private static final long serialVersionUID = 1L;
    Messages messages;
	
    public Hub() {
    	super();
    	messages = new Messages();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    HttpSession session = request.getSession();
	    		if(session.getAttribute("userName") == null) response.sendRedirect("Login");
	    if(request.getParameter("textEntry") != null) {
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String formattedDate = myDateObj.format(myFormatObj);
	 	Message m = new Message((String)session.getAttribute("userName"),request.getParameter("textEntry"),formattedDate);
	    messages.add(m);
	    response.sendRedirect("Hub");
	    }
	    response.setIntHeader("Refresh", 6);
	    request.removeAttribute("textArea");
	    String s = (String) session.getAttribute("userName");
	    out.println("<!DOCTYPE html>\r\n" + 
	    		"<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Hub</title>\r\n" + 
	    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />\r\n" + 
	    		"</head>\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"<form action=\"Logout\" method=\"post\">\r\n" + 
	    		"		<br/>	<input type=\"submit\" value=\"Log Out\">			\r\n" + 
	    		"		</form>\r\n" + 
	    		"<form action=\"Hub\" method=\"post\">\r\n" + 
	    		"		<br/>	<input type=\"submit\" value=\"Chat Log\">			\r\n" + 
	    		"		</form>\r\n" + 
	    		"<textarea rows=\"30\" cols=\"80\">\r\n" + 
	    		"Welcome to the char bar: "+s+
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
	    		"</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    HttpSession session = request.getSession();
		if(session.getAttribute("userName") == null) response.sendRedirect("Login");
		out.println(messages.toString());
	}
}

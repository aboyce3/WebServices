import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateAccount() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HttpSession session = request.getSession();
		 if(session.getAttribute("userName") != null)response.sendRedirect("Hub");
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println(
	    		"<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Create An Account</title>\r\n" + 
	    		"</head>\r\n" + 
	    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />\r\n" + 
	    		"<body>\r\n" + 
	    		"	<div id=\"wrapper\">\r\n" + 
	    		"		<form method=\"post\" action=\"AccountCreation\">\r\n" + 
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
	    		"</html>");
	   out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}

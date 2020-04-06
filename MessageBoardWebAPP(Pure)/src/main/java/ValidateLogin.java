import java.io.IOException;

import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ValidateLogin")
public class ValidateLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;


	public ValidateLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
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
					HttpSession session = request.getSession();
					session.setAttribute("userName", userName);
					session.setMaxInactiveInterval(600);
					response.sendRedirect("Hub");
					return;
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			response.sendRedirect("ErrorLogin");
			return;
		}
		response.sendRedirect("ErrorLogin");
	}

}

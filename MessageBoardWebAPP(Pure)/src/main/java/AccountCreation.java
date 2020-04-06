import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.regex.Pattern;
import java.sql.*;

/**
 * Servlet implementation class AccountCreation
 */

public class AccountCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountCreation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("userName").trim();
		String password = request.getParameter("password").trim();
		String password2 = request.getParameter("password2").trim();
		String email = request.getParameter("email");
		Connection con;
		String insert = "INSERT into users " + "(username, password, email)" + "VALUES " + "('" + userName + "','"
				+ password + "','" + email + "')";

		String lookup = "SELECT * FROM messageboard.users WHERE username = '" + userName + "'";
		Statement statement = null;
		ResultSet result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messageboard", "root","password");
			statement = con.createStatement();
			result = statement.executeQuery(lookup);
			while (result.next()) {
				String uName = result.getString("username");
				if (userName.contentEquals(uName))
					response.sendRedirect("ErrorCreating");
				return;
			}
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		response.setContentType("text/html");
		if (userName == null || "".equals(userName) || password == null || "".equals(password) || password2 == null
				|| "".equals(password2)) {
			response.sendRedirect("ErrorCreating");
		} else if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?"
				+ "^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
				+ "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
				+ "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
				+ "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)) {
			response.sendRedirect("ErrorCreating");
			return;
		} else if (password == null || "".equals(password) || !password.equals(password2)) {
			response.sendRedirect("ErrorCreating");
		} else {
			try {
				if (statement != null) {
					statement.executeUpdate(insert);
					HttpSession session = request.getSession();
					session.setAttribute("userName", userName);
					session.setMaxInactiveInterval(10*60);
					response.sendRedirect("Hub");
					return;
				} else
					response.sendRedirect("ErrorCreating");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

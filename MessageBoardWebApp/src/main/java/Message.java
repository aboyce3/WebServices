public class Message {
	String message;
	String userName;
	String date;

	public Message(String u, String m, String d) {
		message = m;
		userName = u;
		date = d;
	}

	public String getMessage() {
		return this.message;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getDate() {
		return this.date;
	}

	public String toString() {
		return userName + ": " + message;
	}

	public String toJSON() {
		return "{" + "\"User\":" + "\"" + userName + "\"" + ",\"Message\":" + "\"" + message + "\"" + ",\"Date\":"
				+ "\"" + date + "\"" + "}";
	}
}

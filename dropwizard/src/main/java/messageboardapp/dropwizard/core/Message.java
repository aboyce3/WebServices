package messageboardapp.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
public class Message {

	@NotNull
	@JsonProperty
	String message;

	@NotNull
	@JsonProperty
	String userName;

	@NotNull
	@JsonProperty
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

	@Override
    public String toString() {
        return "Message{" +
                "message=" + userName +
                ", name='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

import java.util.ArrayList;

public class Messages {
	ArrayList<Message> messages;

	public Messages() {
		messages = new ArrayList<>();
	}

	public String messages(){
		String s = "\n";
		if(messages != null && messages.size() > 0) {
		for(Message m : messages) {
			if(m == null)break;
			else {
				s += "\n"+m+"\n";
			}
		}
		}
		return s;
	}
	
	public void add(Message m) {
		messages.add(m);
	}
	
	@Override
	public String toString() {
		String log = "<!DOCTYPE html>\r\n" + 
	    		"<html>\r\n" + 
	    		"<head>\r\n" + 
	    		"<meta charset=\"ISO-8859-1\">\r\n" + 
	    		"<title>Chat Log</title>\r\n" + 
	    		"</head>\r\n" +
	    		"<body>\r\n";

		for (Message m : messages) {
			log += "<br>" + m.toString()+ " (" +m.date+")" +"</br>";
		}

		log += "</body>\r\n" + "</html>";
		return log;
	}
}

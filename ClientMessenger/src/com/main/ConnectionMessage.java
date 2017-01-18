package com.main;

import java.io.Serializable;
public class ConnectionMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public String type, sender, content, recipient;

	public ConnectionMessage(String type, String sender, String content, String recipient) {
		this.type = type;
		this.sender = sender;
		this.content = content;
		this.recipient = recipient;
	}
   	@Override
	public String toString() {
		return "{type='" + type + "', sender='" + sender + "', content='"
				+ content + "', recipient='" + recipient + "'}";
	}
}

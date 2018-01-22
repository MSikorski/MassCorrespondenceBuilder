package org.mateuszsikorski.masscorrespondencebuilder.correspondence;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 7558894618138909469L;

	private final String paragraph1;
	private final String paragraph2;
	private final String paragraph3;
	
	public Message(String paragraph1, String paragraph2, String paragraph3) {
		this.paragraph1 = paragraph1;
		this.paragraph2 = paragraph2;
		this.paragraph3 = paragraph3;
	}

	public String getParagraph1() {
		return paragraph1;
	}

	public String getParagraph2() {
		return paragraph2;
	}

	public String getParagraph3() {
		return paragraph3;
	}

	@Override
	public String toString() {
		return "Message [paragraph1=" + paragraph1 + ", paragraph2=" + paragraph2 + ", paragraph3=" + paragraph3 + "]";
	}
	
}

package org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl;

import java.io.Serializable;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;

public class PreCorrespondence implements Serializable{

	private static final long serialVersionUID = -6311937162394030156L;
	
	private final Recipient recipient;
	private final Message message;
	
	public PreCorrespondence(Recipient recipient, Message message) {
		this.recipient = recipient;
		this.message = message;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public Message getMessage() {
		return message;
	}
	
}

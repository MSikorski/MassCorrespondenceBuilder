package org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl;

import java.io.Serializable;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Correspondence;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Format;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Sender;

public class CorrespondenceImpl implements Serializable, Correspondence{

	private final Sender sender;
	private final Recipient recipient;
	private final Format format;
	private final Message message;

	private static final long serialVersionUID = 978450222046767645L;
	
	public CorrespondenceImpl(Sender sender, Recipient recipient, 
								Format format, Message message) {
		this.sender = sender;
		this.recipient = recipient;
		this.format = format;
		this.message = message;
	}
	
	@Override
	public Sender getSender() {
		return sender;
	}

	@Override
	public Recipient getRecipient() {
		return recipient;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Correspondence [sender=" + sender + ", recipient=" + recipient + ", format=" + format + ", message="
				+ message + "]";
	}
	
}

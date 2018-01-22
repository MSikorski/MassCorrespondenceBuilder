package org.mateuszsikorski.masscorrespondencebuilder.correspondence;

public interface Correspondence {

	Sender getSender();

	Recipient getRecipient();

	Format getFormat();

	Message getMessage();

}
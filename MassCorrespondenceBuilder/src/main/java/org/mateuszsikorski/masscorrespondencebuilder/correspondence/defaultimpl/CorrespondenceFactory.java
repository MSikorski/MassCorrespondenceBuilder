package org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Correspondence;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Format;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Sender;
import org.springframework.stereotype.Component;

@Component
public class CorrespondenceFactory {

	public Correspondence getInstance(Sender sender, Recipient recipient, 
										Format format, Message message) {
		
		Correspondence correspondence = new CorrespondenceImpl(sender, recipient, format, message);
		return correspondence;
	}
	
}

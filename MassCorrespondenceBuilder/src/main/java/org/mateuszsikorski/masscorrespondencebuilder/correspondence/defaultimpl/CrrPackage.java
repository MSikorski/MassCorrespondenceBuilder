package org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Sender;

public class CrrPackage implements Serializable {
	
	private static final long serialVersionUID = -6285159074156048644L;
	
	private final int packageSize = 100;
	
	private final Sender sender;
	private List<PreCorrespondence> preCorrespondenceList;
	
	public CrrPackage(Sender sender) {
		this.sender = sender;
		preCorrespondenceList = new ArrayList<>();
	}
	
	public void addCrr(Recipient recipient, Message message) {
		PreCorrespondence temp = new PreCorrespondence(recipient, message);
		preCorrespondenceList.add(temp);
	}
	
	public int getSpaceLeft() {
		return (packageSize - preCorrespondenceList.size());
	}

	public Sender getSender() {
		return sender;
	}

	public List<PreCorrespondence> getPreCorrespondenceList() {
		return preCorrespondenceList;
	}
	
	
}

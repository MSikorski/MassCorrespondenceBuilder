package org.mateuszsikorski.masscorrespondencebuilder.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Sender;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Validator;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.CrrPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Component
@Scope("prototype")
public class DefaultHandlerImpl extends DefaultHandler {

	private CrrPackage crrP;
	private List<CrrPackage> crrPBufor;

	private boolean stopProcessing = false;
	private Exception exception = null;

	private Validator validator;

	private boolean crrPackage = false;
	private boolean crrNumber = false;

	private boolean senderInfo = false;
	private boolean senderSendingDate = false;
	private boolean senderCompanyName = false;
	private boolean senderCompanyStreet = false;
	private boolean senderCompanyPostalCode = false;
	private boolean senderCompanyCity = false;
	private boolean senderCompanyDetail = false;

	private boolean crrList = false;

	private boolean crrInfo = false;

	private boolean recipientInfo = false;
	private boolean recipientFirstName = false;
	private boolean recipientLastName = false;
	private boolean recipientStreet = false;
	private boolean recipientPostalCode = false;
	private boolean recipientCity = false;

	private boolean messageInfo = false;
	private boolean message1 = false;
	private boolean message2 = false;
	private boolean message3 = false;

	private int crrNr = 0;
	private int counter = 0;

	private Sender sender;

	private String tempSendingDate;
	private String tempCompanyName;
	private String tempCompanyStreet;
	private String tempCompanyPostalCode;
	private String tempCompanyCity;
	private String tempCompanyDetail;
	private String tempRecipientFirstName;
	private String tempRecipientLastName;
	private String tempRecipientStreet;
	private String tempRecipientPostalCode;
	private String tempRecipientCity;
	private String tempMessage1;
	private String tempMessage2;
	private String tempMessage3;

	private Recipient tempRecipient;
	private Message tempMessage;

	@Autowired
	public DefaultHandlerImpl(Validator validator) {
		this.validator = validator;
		crrPBufor = new ArrayList<>();
	}

	private void addToBufor(CrrPackage crrP) {
		crrPBufor.add(crrP);
	}

	public boolean isItemInBufor() {
		return !crrPBufor.isEmpty();
	}

	public CrrPackage getItemFromBufor() {
		if (isItemInBufor()) {
			CrrPackage temp = crrPBufor.get(0);
			crrPBufor.remove(0);
			return temp;
		} else
			return null;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (!stopProcessing) {

			switch (qName.toUpperCase()) {

			case "CRRPACKAGE": {
				crrPackage = true;
				break;
			}
			case "CRRNUMBER": {
				crrNumber = true;
				break;
			}

			case "SENDERINFO": {
				if (crrPackage) {
					senderInfo = true;
					break;
				} else {
					exception = new SAXException("Found tag senderinfo outside the crrpackage tag");
					stopProcessing();
				}

			}
			case "SENDINGDATE": {
				senderSendingDate = true;
				break;
			}
			case "COMPANYNAME": {
				senderCompanyName = true;
				break;
			}
			case "COMPANYSTREET": {
				senderCompanyStreet = true;
				break;
			}
			case "COMPANYPOSTALCODE": {
				senderCompanyPostalCode = true;
				break;
			}
			case "COMPANYCITY": {
				senderCompanyCity = true;
				break;
			}
			case "COMPANYDETAIL": {
				senderCompanyDetail = true;
				break;
			}

			case "CRRLIST": {
				crrList = true;
				break;
			}

			case "CRRINFO": {
				crrInfo = true;
				break;
			}
			case "RECIPIENTINFO": {
				recipientInfo = true;
				break;
			}
			case "RECIPIENTFIRSTNAME": {
				recipientFirstName = true;
				break;
			}
			case "RECIPIENTLASTNAME": {
				recipientLastName = true;
				break;
			}
			case "RECIPIENTSTREET": {
				recipientStreet = true;
				break;
			}
			case "RECIPIENTPOSTALCODE": {
				recipientPostalCode = true;
				break;
			}
			case "RECIPIENTCITY": {
				recipientCity = true;
				break;
			}
			case "MESSAGEINFO": {
				messageInfo = true;
				break;
			}
			case "MESSAGE1": {
				message1 = true;
				break;
			}
			case "MESSAGE2": {
				message2 = true;
				break;
			}
			case "MESSAGE3": {
				message3 = true;
				break;
			}

			default: {
				exception = new SAXException("Found unknown tag " + qName);
				stopProcessing();

			}

			}

		}

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(!stopProcessing) {
		
		if(qName.equalsIgnoreCase("SENDERINFO")) {
			
			if(senderSendingDate || senderCompanyName || senderCompanyStreet || 
				senderCompanyPostalCode || senderCompanyCity || senderCompanyDetail || 
				tempSendingDate == null || tempCompanyName == null || tempCompanyStreet == null || 
				tempCompanyPostalCode == null || tempCompanyCity == null ) {
				exception =  new SAXException("File broken: invalid sender data");
				stopProcessing();
			} 
			else sender = new Sender(tempSendingDate, tempCompanyName, tempCompanyStreet, 
							tempCompanyPostalCode, tempCompanyCity, tempCompanyDetail);
			
			if(!validator.validate(sender)) {
				exception = new SAXException("Bussiness logic impossible with " + sender);
				stopProcessing();
			}
			senderInfo = false;
		}
		
		if(qName.equalsIgnoreCase("RECIPIENTINFO")) {
			
			if(recipientFirstName || recipientLastName || recipientStreet || 
					recipientPostalCode || recipientCity)  {
				exception = new SAXException("File broken: invalid recipientInfo with data \n"
						+ tempRecipientFirstName + "," + tempRecipientLastName + ", "
						+ tempRecipientStreet + ", " + tempRecipientPostalCode + ", "
						+ tempRecipientCity);
				stopProcessing();
				}
			if(tempRecipientFirstName == null || tempRecipientLastName == null || 
					tempRecipientStreet == null || tempRecipientPostalCode == null || 
					tempRecipientCity == null) {
				exception = new SAXException("File broken: invalid recipientInfo missing data \n"
						+ tempRecipientFirstName + "," + tempRecipientLastName + ", "
						+ tempRecipientStreet + ", " + tempRecipientPostalCode + ", "
						+ tempRecipientCity);
				stopProcessing();
				}
			
			tempRecipient = new Recipient(tempRecipientFirstName, tempRecipientLastName, 
						tempRecipientStreet, tempRecipientPostalCode, tempRecipientCity);
			
			if(!validator.validate(tempRecipient))
				throw new SAXException("Bussiness logic impossible with " + tempRecipient);
			
			recipientInfo = false;
			
		}
		
		if(qName.equalsIgnoreCase("MESSAGEINFO")) {
			
			if(message1 || message2 || message3 || 
				tempMessage1 == null || tempMessage2 == null || tempMessage3 == null) {
				exception = new SAXException("File broken: invalid messageInfo with data \n"
						+ message1 + "\n" + message2 + "\n" + message3);
				stopProcessing();
			}
			
			tempMessage = new Message(tempMessage1, tempMessage2, tempMessage3);
			
			if(!validator.validate(tempMessage)) {
				exception = new SAXException("Bussines logic impossible with message to " + tempRecipient);
				stopProcessing();
			}
			
			messageInfo = false;
		}
		
		if(qName.equalsIgnoreCase("CRRINFO")) {
			
			if(recipientInfo || messageInfo) {
				exception = new SAXException("File broken: invalid crrinfo with data \n"
						+ tempRecipient + "\n" + tempMessage);
				stopProcessing();
			}
			
			if(crrP == null) {
				crrP = new CrrPackage(sender);
			} else if(crrP.getSpaceLeft() < 1) {
				addToBufor(crrP);
				crrP = new CrrPackage(sender);
			}
			
			crrP.addCrr(tempRecipient, tempMessage);
			counter++;
			
			cleanTempData();
			
			crrInfo = false;
		}
		
		if(qName.equalsIgnoreCase("CRRLIST")) {
			
			if(crrInfo) {
				exception = new SAXException("File broken: was looking for </crrinfo> found </crrlist>");
				stopProcessing();
			}
			
			crrList = false;
			
			addToBufor(crrP);
			
		}
		
		if(qName.equalsIgnoreCase("CRRPACKAGE")) {
			
			if(crrList) {
				exception = new SAXException("File broken: was looking for </crrlist> found </crrpackage>");
				stopProcessing();
			}
			
			crrPackage = false;
		}
		
		}
			
	}

	public void characters(char ch[], int start, int length) throws SAXException {

		if (crrNumber && crrPackage) {
			crrNr = Integer.parseInt(new String(ch, start, length));
			crrNumber = false;
		}

		if (senderInfo) {

			if (senderSendingDate) {
				tempSendingDate = new String(ch, start, length);
				senderSendingDate = false;
			}

			if (senderCompanyName) {
				tempCompanyName = new String(ch, start, length);
				senderCompanyName = false;
			}

			if (senderCompanyStreet) {
				tempCompanyStreet = new String(ch, start, length);
				senderCompanyStreet = false;
			}

			if (senderCompanyPostalCode) {
				tempCompanyPostalCode = new String(ch, start, length);
				senderCompanyPostalCode = false;
			}

			if (senderCompanyCity) {
				tempCompanyCity = new String(ch, start, length);
				senderCompanyCity = false;
			}

			if (senderCompanyDetail) {
				tempCompanyDetail = new String(ch, start, length);
				senderCompanyDetail = false;
			}
		}

		if (crrList && crrInfo) {

			if (recipientInfo) {

				if (recipientFirstName) {
					tempRecipientFirstName = new String(ch, start, length);
					recipientFirstName = false;
				}
				if (recipientLastName) {
					tempRecipientLastName = new String(ch, start, length);
					recipientLastName = false;
				}
				if (recipientStreet) {
					tempRecipientStreet = new String(ch, start, length);
					recipientStreet = false;
				}
				if (recipientPostalCode) {
					tempRecipientPostalCode = new String(ch, start, length);
					recipientPostalCode = false;
				}
				if (recipientCity) {
					tempRecipientCity = new String(ch, start, length);
					recipientCity = false;
				}
			}

			if (messageInfo) {

				if (message1) {
					tempMessage1 = new String(ch, start, length);
					message1 = false;
				}
				if (message2) {
					tempMessage2 = new String(ch, start, length);
					message2 = false;
				}
				if (message3) {
					tempMessage3 = new String(ch, start, length);
					message3 = false;
				}
			}

		}

	}

	private void cleanTempData() {

		tempSendingDate = null;
		tempCompanyName = null;
		tempCompanyStreet = null;
		tempCompanyPostalCode = null;
		tempCompanyCity = null;
		tempCompanyDetail = null;
		tempRecipientFirstName = null;
		tempRecipientLastName = null;
		tempRecipientStreet = null;
		tempRecipientPostalCode = null;
		tempRecipientCity = null;
		tempMessage1 = null;
		tempMessage2 = null;
		tempMessage3 = null;

		tempRecipient = null;
		tempMessage = null;
	}

	public void stopProcessing() {
		this.stopProcessing = true;
	}

	public Exception getException() {
		return exception;
	}
}

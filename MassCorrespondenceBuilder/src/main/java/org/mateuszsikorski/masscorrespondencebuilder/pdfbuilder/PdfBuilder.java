package org.mateuszsikorski.masscorrespondencebuilder.pdfbuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Correspondence;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Format;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Message;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Recipient;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Sender;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.CorrespondenceFactory;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.CrrPackage;
import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.PreCorrespondence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Component
@Scope("prototype")
public class PdfBuilder implements Runnable{
	
	private String destination;
	private CrrPackage crrP;
	private Deserializer deserializer;
	
	private CorrespondenceFactory crrF;
	private Correspondence correspondence;
	private Sender sender;
	
	private String fileName;
	private String serializedFileName;
	
	private Font detailFont;
	
	private boolean isFinished = false;
	
	public void setSerializedFilename (String fileName) {
		this.serializedFileName = fileName;
	}
	
	@Override
	public void run() {
		
		try {
			crrP = deserializer.deserialize(serializedFileName);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			processPackage();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		isFinished = true;
	}
	
	public void processPackage() throws IOException, DocumentException {
		
		sender = crrP.getSender();
		Recipient recipient;
		Message message;
		Format format  = new Format("", 0, 0);
				
		for(PreCorrespondence temp : crrP.getPreCorrespondenceList()) {
			recipient = temp.getRecipient();
			message = temp.getMessage();
			correspondence = crrF.getInstance(sender, recipient, format, message);
			buildPdf();
		}
	}
	
	public void buildPdf() throws IOException, DocumentException{
		
		Document document = new Document();
		fileName = generateFileName();
		PdfWriter.getInstance(document, new FileOutputStream(fileName))
				.setPageEvent(new FooterPageEvent(correspondence.getSender().getCompanyDetail()));
		
		
		
		document.open();
		
		List<Paragraph> paragraphs = renderParagraphs();
		
		document.addCreationDate();
		
		for(Paragraph tempPara : paragraphs) {
			document.add(tempPara);
		}
		
		
		document.close();
		
	}
	
	private List<Paragraph> renderParagraphs() {

		List<Paragraph> paragraphs = new ArrayList<>();
		
		paragraphs.add(renderMailData());
		paragraphs.add(renderSenderData());
		paragraphs.add(renderRecipientData());
		paragraphs.add(renderIntroduction());
		paragraphs.add(renderExplication());
		paragraphs.add(renderConclusion());
		//paragraphs.add(renderFooter());
		
		return paragraphs;
	}
	
	private Paragraph renderMailData() {
		
		Sender sender = correspondence.getSender();
		
		Chunk mailDetail = new Chunk(sender.getCompanyCity() + ", " 
							+ sender.getSendingDate());
		
		mailDetail.setFont(loadDetailFont());
		
		Paragraph mailData = new Paragraph();
		
		mailData.add(mailDetail);
		mailData.setAlignment(Element.ALIGN_RIGHT);
		mailData.setSpacingAfter(25f);
		
		return mailData;
		
	}
	
	private Paragraph renderSenderData() {
		
		Sender sender = correspondence.getSender();
		Chunk senderDetail = new Chunk(sender.getCompanyName() + "\n" 
								+ sender.getCompanyStreet() + "\n"
								+ sender.getCompanyPostalCode() + ", " 
								+ sender.getCompanyCity());
		
		senderDetail.setFont(loadDetailFont());
		
		Paragraph senderData = new Paragraph();
		
		senderData.add(senderDetail);
		senderData.setAlignment(Element.ALIGN_LEFT);
		senderData.setSpacingAfter(25f);
		
		return senderData;
	}
	
	private Paragraph renderRecipientData() {
		
		Recipient recipient = correspondence.getRecipient();
		
		Chunk recipientDetail = new Chunk(recipient.getFirstName() + " "
									+ recipient.getLastName() + "\n"
									+ recipient.getStreet() + "\n"
									+ recipient.getPostalCode() + ", "
									+ recipient.getCity());
		
		recipientDetail.setFont(loadDetailFont());
		
		Paragraph recipientData = new Paragraph();
		
		recipientData.add(recipientDetail);
		recipientData.setAlignment(Element.ALIGN_RIGHT);
		recipientData.setSpacingAfter(35f);
		
		return recipientData;
	}
	
	private Paragraph renderIntroduction() {
		
		Message message = correspondence.getMessage();
		
		Paragraph introduction = new Paragraph(message.getParagraph1());
		
		introduction.setIndentationLeft(15f);
		introduction.setSpacingBefore(25f);
		introduction.setSpacingAfter(25f);
		
		return introduction;
	}
	
	private Paragraph renderExplication() {
		
		Message message = correspondence.getMessage();
		
		Paragraph explication = new Paragraph(message.getParagraph2());
		
		explication.setIndentationLeft(15f);
		explication.setSpacingAfter(25f);
		
		return explication;
	}
	
	private Paragraph renderConclusion() {
		
		Message message = correspondence.getMessage();
		
		Paragraph conclusion = new Paragraph(message.getParagraph3());
		
		conclusion.setIndentationLeft(15f);
		conclusion.setSpacingAfter(25f);
		
		return conclusion;
	}
	/*
	private Paragraph renderFooter() {
		
		Sender sender = correspondence.getSender();
		
		Chunk footerText = new Chunk(sender.getCompanyDetail());
		
		footerText.setFont(loadFooterFont());
		
		Paragraph footer = new Paragraph();
		
		footer.add(footerText);
		
		footer.setFont(loadFooterFont());
		
		return footer;
	}*/
	
	private String generateFileName() {
		
		String fileName = destination + "/";
		fileName += sender.getSendingDate();
		new File(fileName).mkdir();
		fileName += "/" + sender.getCompanyName().replaceAll("\\s", "") + ".";
		fileName += correspondence.getRecipient().getFirstName();
		fileName += correspondence.getRecipient().getLastName();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date date = new Date();
		fileName += dateFormat.format(date) + ".pdf";
		
		return fileName;
	}
	
	private Font loadDetailFont() {
		
		if(detailFont == null) {
			detailFont = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
			return detailFont;
		} else return detailFont;
	}
	
	@Autowired
	public void setCrrF(CorrespondenceFactory crrF) {
		this.crrF = crrF;
	}
	
	@Autowired
	public void setDeserializer(Deserializer deserializer) {
		this.deserializer = deserializer;
	}
	
	@Autowired
	public void setDestination(String locC) {
		this.destination = locC;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public String getSerializedFileName() {
		return serializedFileName;
	}

}

package org.mateuszsikorski.masscorrespondencebuilder.xmlparser;

import java.io.IOException;

import javax.xml.parsers.SAXParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Component
@Scope("prototype")
public class XmlParser implements Runnable {
	
	private SAXParser saxParser;
	private DefaultHandler handler;
	
	private final String locA;
	private final String locB;
	private String filePath; 
	private String fileName;
	
	private boolean isRunning = true;
	private boolean ending = false;
	
	private Exception e = null;
	
	@Autowired
	public XmlParser(SAXParser saxParser, DefaultHandler handler, 
						String locA, String locB) {
		this.saxParser = saxParser;
		this.handler = handler;
		this.locA = locA;
		this.locB = locB;
	}
	
	

	public void setFilePath(String fileName) {
		this.fileName = fileName;
		this.filePath = locA + "/" + fileName;
	}

	@Override
	public void run() {
		
		BuforService buforService = new BuforService(handler, this, locB);
		Thread buforThread = new Thread(buforService);
		buforThread.start();
	
		try {
			parseXml();
		} catch (SAXException | IOException e1) {
			this.e = e1;
		}
		
		if(((DefaultHandlerImpl) handler).getException() != null) {
			this.e = ((DefaultHandlerImpl) handler).getException();
		}
			
		
		isRunning = false;
		
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(ending) { // ustawiane przez bufor gdy wyciagnie wszystkie dane
				break;
			}
		}
		
	}
	
	public void parseXml() throws SAXException, IOException {
		saxParser.parse(filePath, handler);
	}
	
	public void setEnding() {
		ending = true;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean isFinished() {
		return ending;
	}
	
	public Exception getException() {
		return e;
	}
	
	public String getPath() {
		return filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	
}

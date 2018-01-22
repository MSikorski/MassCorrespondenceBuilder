package org.mateuszsikorski.masscorrespondencebuilder.spring;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.Validator;
import org.mateuszsikorski.masscorrespondencebuilder.eventlistener.KeyEvent;
import org.mateuszsikorski.masscorrespondencebuilder.filesystemspy.Queue;
import org.mateuszsikorski.masscorrespondencebuilder.pdfbuilder.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.SAXException;

@Configuration
public class Config {

	
	// prosze sobie zadeklarowac lokalizacje
	private final String locA="C:/Users/happy/Desktop/ppuc/locA";
	private final String locB="C:/Users/happy/Desktop/ppuc/locB";
	private final String locC="C:/Users/happy/Desktop/ppuc/locC";
	private final String logLoc="C:/Users/happy/Desktop/ppuc/applogs";
	
	private final int xmlThreads=2;
	private final int pdfThreads=4;
	
	@Bean(name="xmlThreads")
	public int getXmlThreads() {
		return xmlThreads;
	}
	
	@Bean(name="pdfThreads")
	public int getPdfThreads() {
		return pdfThreads;
	}
	
	@Bean(name="logLoc")
	public String getLogLoc() {
		return logLoc;
	}
	
	@Bean(name="locA")
	public String locA() {
		return locA;
	}
	
	@Bean(name="locB")
	public String locB() {
		return locB;
	}
	
	@Bean(name="locC")
	public String locC() {
		return locC;
	}
	
	@Bean(name="pathA")
	@Autowired
	public Path getPathA(String locA) {
		Path pathA = Paths.get(locA);
		return pathA;
	}
	
	@Bean(name="pathB")
	@Autowired
	public Path getPathB(String locB) {
		Path pathB = Paths.get(locB);
		return pathB;
	}
	
	@Bean(name="pathC")
	@Autowired
	public Path getPathC(String locC) {
		Path pathC = Paths.get(locC);
		return pathC;
	}
	
	@Bean(name="watchServiceA")
	public WatchService getWatchServiceA() {
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return watchService;
	}
	
	@Bean(name="watchServiceB")
	public WatchService getWatchServiceB() {
		WatchService watchService = null;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return watchService;
	}
	
	@Bean(name="watchKeyA")
	@Autowired
	public WatchKey getWatchKeyA(WatchService watchServiceA, Path pathA) {
		WatchKey watchKeyA = null;
		try {
			watchKeyA = pathA.register(watchServiceA, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return watchKeyA;
	}
	
	@Bean(name="watchKeyB")
	@Autowired
	public WatchKey getWatchKeyB(WatchService watchServiceB, Path pathB) {
		WatchKey watchKeyB = null;
		try {
			watchKeyB = pathB.register(watchServiceB, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return watchKeyB;
	}
	
	@Bean(name="Queue")
	public Queue getQueue() {
		return new Queue();
	}
	
	@Bean(name="saxParserFactory")
	public SAXParserFactory getSaxFactory() {
		return SAXParserFactory.newInstance();
	}
	
	@Bean(name="saxParser")
	@Scope("prototype")
	@Autowired
	public SAXParser getSaxParser(SAXParserFactory saxParserFactory) {
		SAXParser saxParser = null;
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return saxParser;
	}
	
	@Bean(name="deserializer")
	@Scope("prototype")
	@Autowired
	public Deserializer getDeserializer(String locB) {
		Deserializer deserializer = new Deserializer(locB);
		return deserializer;
	}
	
	@Bean(name="fileLoger")
	@Autowired
	public Logger getFileLogger(String logLoc) {
		Logger logger = Logger.getLogger("FileLoger");
		FileHandler fh = null;
		try {
			fh = new FileHandler(logLoc);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		return logger;
	}
	
	@Bean(name="keyEvent")
	public KeyEvent getKeyEvent() {
		return new KeyEvent();
	}
	
	@Bean(name="validator")
	public Validator getValidator() {
		return new Validator();
	}
	
	
}

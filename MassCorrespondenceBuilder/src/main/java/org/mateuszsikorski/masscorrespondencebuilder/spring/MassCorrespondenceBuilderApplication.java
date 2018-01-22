package org.mateuszsikorski.masscorrespondencebuilder.spring;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.mateuszsikorski.masscorrespondencebuilder.eventlistener.KeyEvent;
import org.mateuszsikorski.masscorrespondencebuilder.filesystemspy.Queue;
import org.mateuszsikorski.masscorrespondencebuilder.filesystemspy.SpyDirectoriesService;
import org.mateuszsikorski.masscorrespondencebuilder.pdfbuilder.PdfBuilder;
import org.mateuszsikorski.masscorrespondencebuilder.xmlparser.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "org.mateuszsikorski.masscorrespondencebuilder")
public class MassCorrespondenceBuilderApplication {

	private static ApplicationContext context;

	private static PdfBuilder[] pdfBuilders;
	private static XmlParser[] xmlParsers;

	private static Thread[] pdfBuilderThreads;
	private static Thread[] parserThreads;

	private static boolean[] pdfThreadsAvaibility;
	private static boolean[] xmlThreadsAvaibility;

	private static int pdfThreads;
	private static int xmlThreads;
	
	private static String locB;

	private static Logger fileLogger;
	
	private static KeyEvent keyEvent;

	public static void main(String[] args) {

		SpringApplication.run(MassCorrespondenceBuilderApplication.class, args);
		
		startUp();

		SpyDirectoriesService spy = context.getBean(SpyDirectoriesService.class);
		Queue queue = context.getBean(Queue.class);

		Thread spyThread = new Thread(spy);
		spyThread.start();

		boolean loop = true;

		while (loop) {

			manageThreads();

			if (queue.isItemInQueueA() && isXmlThreadAvailable()) {

				Path path = queue.getFirstPathFromQueueA();
				String fileName = path.toString();

				int index = getFreeXmlThreadIndex();
				xmlParsers[index] = context.getBean(XmlParser.class);
				xmlParsers[index].setFilePath(fileName);
				xmlThreadsAvaibility[index] = false;
				parserThreads[index] = new Thread(xmlParsers[index]);
				parserThreads[index].start();
			}

			if (queue.isItemInQueueB() && isPdfThreadAvailable()) {

				Path path = queue.getFirstPathFromQueueB();
				String fileName = path.toString();

				int index = getFreePdfThreadIndex();

				pdfBuilders[index] = context.getBean(PdfBuilder.class);
				pdfBuilders[index].setSerializedFilename(fileName);
				pdfThreadsAvaibility[index] = false;
				pdfBuilderThreads[index] = new Thread(pdfBuilders[index]);
				pdfBuilderThreads[index].start();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(keyEvent.isEnding()) {
				spy.stopSpy();
				loop = false;
			}
			
		}

	}

	private static void manageThreads() {

		for (int i = 0; i < xmlParsers.length; i++) {
			if (xmlParsers[i] != null) {
				if (xmlParsers[i].isFinished()) {
					
					String filePath = xmlParsers[i].getPath();
					if(xmlParsers[i].getException() != null) {
						
						Exception e = xmlParsers[i].getException();
						String fileName = xmlParsers[i].getFileName();
						String log = "Error while parsing xml " + filePath + "\n" + e.toString();
						fileLogger.info(log);
						String newFileName = "ERROR." + fileName;
						Path path = Paths.get(filePath);
						try {
							Files.move(path, path.resolveSibling(newFileName));
						} catch (IOException e1) {
							log = "Error while renaming damaged xml " + filePath + "\n"
									+ e1.toString();
							fileLogger.info(log);
						}
					} else {
						try {
							Files.delete(Paths.get(filePath));
						} catch (IOException e) {
							String log = "Error while deleting processed xml " + filePath + "\n"
									+ e.toString();
							fileLogger.info(log);
						}
					}
					xmlParsers[i] = null;
					parserThreads[i] = null;
					xmlThreadsAvaibility[i] = true;
				}
			}
		}

		for (int i = 0; i < pdfBuilders.length; i++) {
			if (pdfBuilders[i] != null) {
				if (pdfBuilders[i].isFinished()) {
					String serializedFilePath = locB + "/" + pdfBuilders[i].getSerializedFileName();
					try {
						Files.delete(Paths.get(serializedFilePath));
					} catch (IOException e) {
						String log = "Error while deleting processed .ser " + serializedFilePath + "\n"
								+ e.toString();
						fileLogger.info(log);
					}
					
					
					pdfBuilders[i] = null;
					pdfBuilderThreads[i] = null;
					pdfThreadsAvaibility[i] = true;
				}
			}
		}

	}

	private static int getFreeXmlThreadIndex() {
		if (isXmlThreadAvailable()) {
			for (int i = 0; i < xmlThreadsAvaibility.length; i++) {
				if (xmlThreadsAvaibility[i]) {
					return i;
				} else continue;
			}

		}
		return -1; // Nigdy nie zwroci -1, gdyz warunkiem wywolania tej metody jest dostepnosc wolnego watku
	}

	private static int getFreePdfThreadIndex() {
		if (isPdfThreadAvailable()) {
			for (int i = 0; i < pdfThreadsAvaibility.length; i++) {
				if (pdfThreadsAvaibility[i]) {
					return i;
				} else
					continue;
			}

		}
		return -1; // Nigdy nie zwroci -1, gdyz warunkiem wywolania tej metody jest dostepnosc wolnego watku
	}

	private static boolean isXmlThreadAvailable() {
		for (boolean temp : xmlThreadsAvaibility) {
			if (temp)
				return true;
		}
		return false;
	}

	private static boolean isPdfThreadAvailable() {
		for (boolean temp : pdfThreadsAvaibility) {
			if (temp)
				return true;
		}
		return false;
	}

	private static void startUp() {

		pdfBuilders = new PdfBuilder[pdfThreads];
		xmlParsers = new XmlParser[xmlThreads];
		pdfBuilderThreads = new Thread[pdfThreads];
		parserThreads = new Thread[xmlThreads];

		pdfThreadsAvaibility = new boolean[pdfThreads];
		xmlThreadsAvaibility = new boolean[xmlThreads];

		for(int i=0; i<xmlThreads; i++)
			xmlParsers[i] = null;
		
		for(int i=0; i<pdfThreads; i++)
			pdfThreadsAvaibility[i] = true;

		for(int i=0; i<xmlThreads; i++)
			xmlThreadsAvaibility[i] = true;

	}

	@Autowired
	public void context(ApplicationContext context) {
		MassCorrespondenceBuilderApplication.context = context;
	}

	@Autowired
	public void setXmlThreads(int xmlThreads) {
		MassCorrespondenceBuilderApplication.xmlThreads = xmlThreads;
	}

	@Autowired
	public void setPdfThreads(int pdfThreads) {
		MassCorrespondenceBuilderApplication.pdfThreads = pdfThreads;
	}
	
	@Autowired
	public void setFileLogger(Logger fileLogger) {
		MassCorrespondenceBuilderApplication.fileLogger = fileLogger;
	}
	
	@Autowired
	public void setLocB(String locB) {
		MassCorrespondenceBuilderApplication.locB = locB;
	}
	
	@Autowired
	public void setKeyEvent(KeyEvent keyEvent) {
		MassCorrespondenceBuilderApplication.keyEvent = keyEvent;
	}
}

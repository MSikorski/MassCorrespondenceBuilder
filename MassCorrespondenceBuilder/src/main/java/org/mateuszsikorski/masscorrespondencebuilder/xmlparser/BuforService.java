package org.mateuszsikorski.masscorrespondencebuilder.xmlparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.CrrPackage;
import org.xml.sax.helpers.DefaultHandler;

public class BuforService implements Runnable {

	private final String locB;

	private DefaultHandlerImpl handler;
	private XmlParser parser;

	private boolean run = true;

	public BuforService(DefaultHandler handler, XmlParser parser, String locB) {
		this.handler = (DefaultHandlerImpl) handler;
		this.parser = parser;
		this.locB = locB;
	}

	@Override
	public void run() {
		while (run) {
			checkIfNeeded();
			if (handler.isItemInBufor()) {
				CrrPackage crrP = handler.getItemFromBufor();
				serialize(crrP);
			}
		
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		parser.setEnding();
	}

	private void checkIfNeeded() {
		if(!parser.isRunning()) {
			if(!handler.isItemInBufor()) {
				run = false;
			}
		}
	}

	private void serialize(CrrPackage crrP) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		Date date = new Date();
		String fileName = locB + "/" + dateFormat.format(date) + ".ser";

		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(crrP);
			out.close();
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

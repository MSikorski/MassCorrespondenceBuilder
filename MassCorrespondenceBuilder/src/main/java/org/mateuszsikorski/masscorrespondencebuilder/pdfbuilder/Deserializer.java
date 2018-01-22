package org.mateuszsikorski.masscorrespondencebuilder.pdfbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.mateuszsikorski.masscorrespondencebuilder.correspondence.defaultimpl.CrrPackage;

public class Deserializer {
	
	private final String locB;
	
	public Deserializer(String locB) {
		this.locB = locB;
	}

	public CrrPackage deserialize(String fileName) throws IOException, ClassNotFoundException {
		
		CrrPackage crrP;
		String path = locB + "/" + fileName;
		
		FileInputStream fileIn = new FileInputStream(path);
		ObjectInputStream in = new ObjectInputStream(fileIn);
			
		crrP = (CrrPackage) in.readObject();
		
		in.close();
		fileIn.close();
	
		return crrP;
	}
	
}

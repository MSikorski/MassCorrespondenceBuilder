package org.mateuszsikorski.masscorrespondencebuilder.pdfbuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class FooterPageEvent extends PdfPageEventHelper {

	private final String footer;

	public void onEndPage(PdfWriter writer, Document document) {
		
		Chunk temp = new Chunk(footer);
		temp.setFont(new Font(FontFamily.TIMES_ROMAN, 8.0f, Font.BOLD, BaseColor.BLACK));
		
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(temp), 110, 30, 0);
	}
	
	public FooterPageEvent(String footer) {
		this.footer = footer;
	}
}
